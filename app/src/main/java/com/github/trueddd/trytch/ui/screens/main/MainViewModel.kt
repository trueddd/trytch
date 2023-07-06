package com.github.trueddd.trytch.ui.screens.main

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.trytch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.TwitchUserManager
import com.github.trueddd.twitch.chat.ChatManager
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes

class MainViewModel(
    private val twitchUserManager: TwitchUserManager,
    private val twitchStreamsManager: TwitchStreamsManager,
    private val twitchBadgesManager: TwitchBadgesManager,
    private val chatManager: ChatManager,
) : StatefulViewModel<MainScreenState>() {

    private companion object {
        val MinimumTimeToRefreshStreams = 1.minutes
    }

    private var authState: String = ""

    private var streamsLastUpdate: Long? = null

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
                .onCompletion {
                    updateState { it.copy(userLoading = false) }
                    twitchBadgesManager.updateBadges()
                }
                .launchIn(viewModelScope)
        }
    }

    init {
        twitchUserManager.userFlow
            .onEach { user -> updateState { it.copy(user = user) } }
            .launchIn(viewModelScope)
        twitchUserManager.userFlow
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { chatManager.initialize() }
            .launchIn(viewModelScope)
        twitchStreamsManager.followedStreamsFlow
            .onEach { streams -> updateState { it.copy(streams = streams.toImmutableList()) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun shouldUpdateStreams(): Boolean {
        return streamsLastUpdate?.let {
            System.currentTimeMillis() - it > MinimumTimeToRefreshStreams.inWholeMilliseconds
        } ?: true
    }

    fun updateStreams() {
        twitchStreamsManager.updateFollowedStreams()
            .onStart {
                streamsLastUpdate = System.currentTimeMillis()
                updateState { it.copy(streamsLoading = true) }
            }
            .onEach { result ->
                Log.d("Streams", result.toString())
                updateState { it.copy(streamsLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}
