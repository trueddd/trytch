package com.github.trueddd.truetripletwitch.ui.screens.splash

import com.github.trueddd.truetripletwitch.ui.StatelessViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.TwitchUserManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val twitchStreamsManager: TwitchStreamsManager,
    private val twitchBadgesManager: TwitchBadgesManager,
    private val twitchUserManager: TwitchUserManager,
) : StatelessViewModel() {

    suspend fun initialize() = supervisorScope {
        launch { delay(1.5.seconds) }
        launch { updateChatBadges() }
        launch { clearStreams() }
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
