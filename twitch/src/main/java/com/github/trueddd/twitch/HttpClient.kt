package com.github.trueddd.twitch

import com.github.trueddd.mylibrary.BuildConfig
import com.github.trueddd.twitch.db.TwitchDao
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

@OptIn(ExperimentalSerializationApi::class)
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
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            request.apply {
                if (url.pathSegments.lastOrNull() == "revoke") {
                    header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
                } else {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                if (host == "api.twitch.tv") {
                    header("Client-Id", BuildConfig.twitchClientId)
                    twitchDao.getUserToken()?.let {
                        header(HttpHeaders.Authorization, "Bearer $it")
                    }
                }
            }.let { execute(it) }
        }
    }
}
