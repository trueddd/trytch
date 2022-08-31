package com.github.trueddd.truetripletwitch.ui.screens.main

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
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

    fun login(intent: Intent) {
        val fragment = intent.data?.fragment ?: return
        val response = fragment.split("&")
            .associate { it.split("=").let { (name, value) -> name to value } }
        if (response["state"] == authState) {
            val accessToken = response["access_token"] ?: return
            twitchClient.login(accessToken)
                .onStart { updateState { it.copy(userLoading = true) } }
                .onCompletion { updateState { it.copy(userLoading = false) } }
                .launchIn(viewModelScope)
        }
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
        twitchClient.userFlow
            .flatMapLatest { user ->
                if (user == null) {
                    flowOf(emptyList())
                } else {
                    twitchClient.getFollowedStreams()
                }
            }
            .onEach { streams -> updateState { it.copy(streams = streams) } }
            .launchIn(viewModelScope)
    }
}
