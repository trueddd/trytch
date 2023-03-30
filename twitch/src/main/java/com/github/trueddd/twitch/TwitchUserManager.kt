package com.github.trueddd.twitch

import com.github.trueddd.twitch.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TwitchUserManager {

    val userFlow: StateFlow<User?>

    suspend fun checkIfLoggedIn(): Boolean

    fun getAuthLink(state: String): String

    fun login(accessToken: String): Flow<Unit>

    fun logout(): Flow<Unit>
}
