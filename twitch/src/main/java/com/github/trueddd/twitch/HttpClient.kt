package com.github.trueddd.twitch

import com.github.trueddd.twitch.db.TwitchDao
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import okhttp3.logging.HttpLoggingInterceptor

internal const val SKIP_AUTH_HEADER = "SKIP_AUTH"

internal fun createHttpClient(twitchDao: TwitchDao): HttpClient {
    return HttpClient(OkHttp) {
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
            if (url.pathSegments.lastOrNull() == "revoke") {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
            } else {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
        install(Auth) {
            bearer {
                loadTokens { twitchDao.getUserToken()?.let { BearerTokens(it, "") } }
                sendWithoutRequest { !it.headers.contains(SKIP_AUTH_HEADER) }
            }
        }
    }
}
