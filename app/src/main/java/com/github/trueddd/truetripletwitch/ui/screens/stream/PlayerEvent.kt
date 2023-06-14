package com.github.trueddd.truetripletwitch.ui.screens.stream

sealed class PlayerEvent {

    data class AspectRatioChange(val newValue: PlayerStatus.AspectRatio) : PlayerEvent()

    data class StreamQualityChange(val newValue: String) : PlayerEvent()
}
