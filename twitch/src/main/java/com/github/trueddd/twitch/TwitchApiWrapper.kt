package com.github.trueddd.twitch

import com.github.trueddd.twitch.data.UserRequestType
import com.github.trueddd.twitch.dto.TwitchResponse
import com.github.trueddd.twitch.dto.TwitchUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Url

internal class TwitchApiWrapper(
    private val httpClient: HttpClient,
) {

    suspend fun getTwitchUsers(userRequestType: UserRequestType): List<TwitchUser>? {
        return try {
            httpClient.get(Url("https://api.twitch.tv/helix/users")) {
                when (userRequestType) {
                    is UserRequestType.Id -> {
                        userRequestType.value.forEach {
                            parameter("id", it)
                        }
                    }
                    is UserRequestType.Token -> {
                        bearerAuth(userRequestType.value)
                    }
                }
            }.body<TwitchResponse<List<TwitchUser>>>().data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
