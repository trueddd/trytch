package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri

sealed class PlayerEvent {

    abstract fun applyTo(streamScreenState: StreamScreenState): StreamScreenState

    data class AspectRatioChange(val newValue: PlayerStatus.AspectRatio) : PlayerEvent() {

        override fun applyTo(streamScreenState: StreamScreenState): StreamScreenState {
            val playerStatus = streamScreenState.playerStatus.copy(aspectRatio = newValue)
            return streamScreenState.copy(playerStatus = playerStatus)
        }
    }

    data class StreamQualityChange(val newValue: String) : PlayerEvent() {

        override fun applyTo(streamScreenState: StreamScreenState): StreamScreenState {
            val playerStatus = streamScreenState.playerStatus.copy(
                selectedStream = newValue,
                streamUri = streamScreenState.playerStatus
                    .streamLinks.firstOrNull { it.quality == newValue }
                    ?.let { Uri.parse(it.url) },
            )
            return streamScreenState.copy(playerStatus = playerStatus)
        }
    }
}
