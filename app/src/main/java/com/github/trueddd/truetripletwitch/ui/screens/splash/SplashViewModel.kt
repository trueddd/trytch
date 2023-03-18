package com.github.trueddd.truetripletwitch.ui.screens.splash

import com.github.trueddd.truetripletwitch.ui.StatelessViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager

class SplashViewModel(
    private val twitchStreamsManager: TwitchStreamsManager,
    private val twitchBadgesManager: TwitchBadgesManager,
) : StatelessViewModel() {

    suspend fun clearStreams() {
        twitchStreamsManager.clearStreams()
    }

    fun updateChatBadges() = twitchBadgesManager.updateBadges()
}
