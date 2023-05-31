package com.github.trueddd.truetripletwitch.settings

data class StreamSettings(
    val preferredQuality: String?,
    val chatOverlayEnabled: Boolean,
    val chatOverlayOpacity: Float,
) {

    companion object {
        fun default() = StreamSettings(
            preferredQuality = null,
            chatOverlayEnabled = false,
            chatOverlayOpacity = 0.5f,
        )
    }
}
