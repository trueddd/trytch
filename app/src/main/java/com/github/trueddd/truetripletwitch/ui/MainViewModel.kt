package com.github.trueddd.truetripletwitch.ui

import androidx.lifecycle.viewModelScope
import com.github.trueddd.twitch.TwitchClient
import com.github.trueddd.twitch.data.User
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random

class MainViewModel(
    private val twitchClient: TwitchClient,
) : StatefulViewModel<MainScreenState>() {

    var authState: String = ""
        private set

    override fun initialState() = MainScreenState.default()

    fun getLinkForLogin(): String {
        authState = Random.Default.nextLong().toString()
        return twitchClient.getAuthLink(authState)
    }

    fun login(accessToken: String) {
        twitchClient.login(accessToken)
            .onStart { updateState { it.copy(userLoading = true) } }
            .onCompletion { updateState { it.copy(userLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun logout() {
        twitchClient.logout()
            .onStart { updateState { it.copy(userLoading = true) } }
            .onCompletion { updateState { it.copy(userLoading = false) } }
            .launchIn(viewModelScope)
    }

    init {
        twitchClient.userFlow
            .onEach { user -> updateState { it.copy(user = user) } }
            .launchIn(viewModelScope)
    }
}

data class MainScreenState(
    val user: User?,
    val userLoading: Boolean,
) {

    companion object {

        fun test() = MainScreenState(
            User(
                id = "0",
                login = "truetripled",
                displayName = "truetripled",
                email = "qwe123@gmail.com",
                profileImageUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/c0fb8aca-3fc7-41f9-b336-1c39b5dc3afc-profile_image-300x300.png"
            ),
            false,
        )

        fun default() = MainScreenState(
            user = null,
            userLoading = false,
        )
    }
}
