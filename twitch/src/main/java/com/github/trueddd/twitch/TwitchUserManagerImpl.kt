package com.github.trueddd.twitch

import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.data.UserRequestType
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.TwitchTokens
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal class TwitchUserManagerImpl(
    private val twitchDao: TwitchDao,
    private val httpClient: HttpClient,
) : TwitchUserManager {

    private val twitchApiWrapper by lazy {
        TwitchApiWrapper(httpClient)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override val userFlow: StateFlow<User?> by lazy {
        twitchDao.getUserFlow().stateIn(GlobalScope, SharingStarted.Eagerly, null)
    }

    override suspend fun checkIfLoggedIn(): Boolean {
        return withContext(Dispatchers.IO) {
            twitchDao.getUser() != null
        }
    }

    override fun getAuthLink(state: String): String {
        val scopes = listOf(
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

    private suspend fun validateToken(accessToken: String): TwitchTokens? {
        return try {
            httpClient.get(Url("https://id.twitch.tv/oauth2/validate")) {
                header(HttpHeaders.Authorization, "OAuth $accessToken")
            }.body<TwitchTokens>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun login(accessToken: String) = flow<Unit> {
        val twitchTokens = validateToken(accessToken) ?: return@flow
        val twitchUser = twitchApiWrapper.getTwitchUsers(UserRequestType.Token(accessToken))
            ?.firstOrNull() ?: return@flow
        twitchDao.insertUserInfo(
            twitchUser.toUser().copy(current = true),
            twitchTokens.toTokens(accessToken),
        )
    }.flowOn(Dispatchers.IO)

    override fun logout() = flow<Unit> {
        withContext(Dispatchers.IO) {
            twitchDao.clearCurrentUser()
        }
    }
}
