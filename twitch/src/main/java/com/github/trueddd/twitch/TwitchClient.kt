package com.github.trueddd.twitch

import android.content.Context
import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.db.TwitchDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TwitchClient {

    companion object {
        fun create(context: Context): TwitchClient {
            val database = TwitchDatabase.create(context)
            return TwitchClientImpl(
                twitchDao = database.twitchDao(),
                httpClient = createHttpClient(database.twitchDao()),
            )
        }
    }

    val userFlow: StateFlow<User?>

    fun getAuthLink(state: String): String

    fun login(accessToken: String): Flow<Unit>

    fun logout(): Flow<Unit>
}
