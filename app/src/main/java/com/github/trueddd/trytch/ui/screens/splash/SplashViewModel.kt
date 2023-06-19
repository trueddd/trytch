package com.github.trueddd.trytch.ui.screens.splash

import android.util.Log
import com.github.trueddd.trytch.ui.StatelessViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.TwitchUserManager
import com.github.trueddd.twitch.emotes.EmoteUpdateOption
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class SplashViewModel(
    private val twitchStreamsManager: TwitchStreamsManager,
    private val twitchBadgesManager: TwitchBadgesManager,
    private val twitchUserManager: TwitchUserManager,
    private val emotesProvider: EmotesProvider,
) : StatelessViewModel() {

    private companion object {
        const val TAG = "SplashViewModel"
    }

    var initialized: Boolean = false
        private set

    suspend fun initialize() = supervisorScope {
        Log.d(TAG, "Starting init on ${this@SplashViewModel}")
        launch { updateChatBadges() }
        launch { emotesProvider.update(EmoteUpdateOption.Global) }
        launch { clearStreams() }
    }.invokeOnCompletion { error ->
        initialized = error == null
    }

    private suspend fun clearStreams() {
        twitchStreamsManager.clearStreams()
    }

    private suspend fun updateChatBadges() {
        if (twitchUserManager.checkIfLoggedIn()) {
            twitchBadgesManager.updateBadges()
        }
    }
}
