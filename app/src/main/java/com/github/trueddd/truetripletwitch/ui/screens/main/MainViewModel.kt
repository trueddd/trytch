package com.github.trueddd.truetripletwitch.ui.screens.main

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.TwitchUserManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random

class MainViewModel(
    private val twitchUserManager: TwitchUserManager,
    private val twitchStreamsManager: TwitchStreamsManager,
) : StatefulViewModel<MainScreenState>() {

    private var authState: String = ""

    override fun initialState() = MainScreenState.default()

    fun getLinkForLogin(): String {
        authState = Random.Default.nextLong().toString()
        return twitchUserManager.getAuthLink(authState)
    }

    fun login(intent: Intent) {
        val fragment = intent.data?.fragment ?: return
        val response = fragment.split("&")
            .associate { it.split("=").let { (name, value) -> name to value } }
        if (response["state"] == authState) {
            val accessToken = response["access_token"] ?: return
            twitchUserManager.login(accessToken)
                .onStart { updateState { it.copy(userLoading = true) } }
                .onCompletion { updateState { it.copy(userLoading = false) } }
                .launchIn(viewModelScope)
        }
    }

    fun logout() {
        twitchUserManager.logout()
            .onStart { updateState { it.copy(userLoading = true) } }
            .onCompletion { updateState { it.copy(userLoading = false) } }
            .launchIn(viewModelScope)
    }

    init {
        twitchUserManager.userFlow
            .onEach { user -> updateState { it.copy(user = user) } }
            .launchIn(viewModelScope)
        twitchStreamsManager.followedStreamsFlow
            .onEach { streams -> updateState { it.copy(streams = streams) } }
            .launchIn(viewModelScope)
    }

    fun updateStreams() {
        twitchStreamsManager.updateFollowedStreams()
            .onStart { updateState { it.copy(streamsLoading = true) } }
            .onEach { Log.d("Streams", it.toString()) }
            .onCompletion { updateState { it.copy(streamsLoading = false) } }
            .launchIn(viewModelScope)
    }
}
