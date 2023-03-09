package com.github.trueddd.truetripletwitch.ui.screens.splash

import com.github.trueddd.truetripletwitch.ui.StatelessViewModel
import com.github.trueddd.twitch.TwitchClient

class SplashViewModel(
    private val twitchClient: TwitchClient,
) : StatelessViewModel() {

    suspend fun clearStreams() {
        twitchClient.clearStreams()
    }
}
