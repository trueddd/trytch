package com.github.trueddd.truetripletwitch.ui.screens.stream

sealed class PlayerEvent {

    abstract fun applyTo(streamScreenState: StreamScreenState): StreamScreenState

    data class AspectRatioChange(val newValue: PlayerStatus.AspectRatio) : PlayerEvent() {

        override fun applyTo(streamScreenState: StreamScreenState): StreamScreenState {
            val playerStatus = streamScreenState.playerStatus.copy(aspectRatio = newValue)
            return streamScreenState.copy(playerStatus = playerStatus)
        }
    }
}
