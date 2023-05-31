package com.github.trueddd.truetripletwitch.settings

data class StreamSettings(
    val preferredQuality: String?,
    val chatOverlayEnabled: Boolean,
    val chatOverlayOpacity: Float,
    val chatOverlayShiftX: Float,
    val chatOverlayShiftY: Float,
) {

    companion object {
        fun default() = StreamSettings(
            preferredQuality = null,
            chatOverlayEnabled = false,
            chatOverlayOpacity = 0.5f,
            chatOverlayShiftX = 0f,
            chatOverlayShiftY = 0f,
        )
    }
}
