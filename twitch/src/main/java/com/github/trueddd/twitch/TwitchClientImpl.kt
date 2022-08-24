package com.github.trueddd.twitch

import android.content.Context
import com.github.trueddd.mylibrary.BuildConfig
import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.data.UserRequestType
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.db.TwitchDatabase
import com.github.trueddd.twitch.dto.TwitchResponse
import com.github.trueddd.twitch.dto.TwitchTokens
import com.github.trueddd.twitch.dto.TwitchUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor

class TwitchClientImpl(
    context: Context,
) : TwitchClient {

    private val database = TwitchDatabase.create(context)
    private val twitchDao: TwitchDao
        get() = database.twitchDao()

    private val httpClient by lazy {
        HttpClient(OkHttp) {
            engine { 
                addInterceptor(
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
            install(ContentNegotiation) {
                gson {
                }
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
//            install(Auth) {
//                bearer {
//                    loadTokens { twitchDao.getUserToken()?.let { BearerTokens(it, "") } }
//                    refreshTokens {
//                        try {
//                            client.post("https://id.twitch.tv/oauth2/token") {
//                                contentType(ContentType.Application.FormUrlEncoded)
//
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

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
                header(HttpHeaders.Authorization, "OAuth $accessToken")
            }.body<TwitchTokens>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun login(accessToken: String): Flow<Unit> {
        return flow<Unit> {
            val twitchTokens = validateToken(accessToken) ?: return@flow
            val twitchUser = getTwitchUser(UserRequestType.Token(accessToken)) ?: return@flow
            twitchDao.insertUserInfo(twitchUser.toUser(), twitchTokens.toTokens(accessToken))
        }.flowOn(Dispatchers.IO)
    }

    override fun logout() {
        GlobalScope.launch(Dispatchers.IO) {
            twitchDao.deleteUser()
        }
    }
}
