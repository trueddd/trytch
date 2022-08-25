package com.github.trueddd.twitch

import com.github.trueddd.mylibrary.BuildConfig
import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.data.UserRequestType
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.TwitchResponse
import com.github.trueddd.twitch.dto.TwitchTokens
import com.github.trueddd.twitch.dto.TwitchUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
internal class TwitchClientImpl(
    private val twitchDao: TwitchDao,
    private val httpClient: HttpClient,
) : TwitchClient {

    override val userFlow: StateFlow<User?>
        get() = twitchDao.getUserFlow().stateIn(GlobalScope, SharingStarted.Lazily, null)

    override fun getAuthLink(state: String): String {
        val scopes = listOf(
            "user:read:email",
            "user:read:follows",
            "chat:read",
            "chat:edit",
        )
        return URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "id.twitch.tv",
            pathSegments = listOf("oauth2", "authorize"),
            parameters = Parameters.build {
                append("client_id", BuildConfig.twitchClientId)
                append("response_type", "token")
                append("redirect_uri", "https://truetripletwitch/login")
                append("scope", scopes.joinToString(" "))
                append("state", state)
            }
        ).buildString()
    }

    private suspend fun getTwitchUser(userRequestType: UserRequestType): TwitchUser? {
        return try {
            httpClient.get(Url("https://api.twitch.tv/helix/users")) {
                when (userRequestType) {
                    is UserRequestType.Id -> {
                        header(HttpHeaders.Authorization, "Bearer ${twitchDao.getUserToken()!!}")
                        parameter("id", userRequestType.value)
                    }
                    is UserRequestType.Token -> {
                        header(HttpHeaders.Authorization, "Bearer ${userRequestType.value}")
                    }
                }
                header(SKIP_AUTH_HEADER, true)
                header("Client-Id", BuildConfig.twitchClientId)
            }.body<TwitchResponse<List<TwitchUser>>>().data.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun validateToken(accessToken: String): TwitchTokens? {
        return try {
            httpClient.get(Url("https://id.twitch.tv/oauth2/validate")) {
                header(SKIP_AUTH_HEADER, true)
                header(HttpHeaders.Authorization, "OAuth $accessToken")
            }.body<TwitchTokens>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun login(accessToken: String) = flow<Unit> {
        val twitchTokens = validateToken(accessToken) ?: return@flow
        val twitchUser = getTwitchUser(UserRequestType.Token(accessToken)) ?: return@flow
        twitchDao.insertUserInfo(twitchUser.toUser(), twitchTokens.toTokens(accessToken))
    }.flowOn(Dispatchers.IO)

    override fun logout() = flow<Unit> {
        withContext(Dispatchers.IO) {
            twitchDao.deleteUser()
        }
    }
}
