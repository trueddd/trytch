package com.github.trueddd.truetripletwitch.settings

import com.github.trueddd.truetripletwitch.ui.screens.stream.ChatOverlayStatus

data class StreamSettings(
    val preferredQuality: String?,
    val chatOverlayEnabled: Boolean,
    val chatOverlayOpacity: Float,
    val chatOverlayShiftX: Float,
    val chatOverlayShiftY: Float,
    val chatOverlayHeightDp: Int,
    val chatOverlayWidthDp: Int,
) {

    constructor(
        preferredQuality: String?,
        chatOverlayEnabled: Boolean?,
        chatOverlayOpacity: Float?,
        chatOverlayShiftX: Float?,
        chatOverlayShiftY: Float?,
        chatOverlayHeightDp: Int?,
        chatOverlayWidthDp: Int?,
    ) : this(
        preferredQuality,
        chatOverlayEnabled ?: false,
        chatOverlayOpacity ?: 0.5f,
        chatOverlayShiftX ?: 0f,
        chatOverlayShiftY ?: 0f,
        chatOverlayHeightDp ?: ChatOverlayStatus.Size.Normal.heightDp,
        chatOverlayWidthDp ?: ChatOverlayStatus.Size.Normal.widthDp,
    )

    companion object {
        fun default() = StreamSettings(
            preferredQuality = null,
            chatOverlayEnabled = false,
            chatOverlayOpacity = 0.5f,
            chatOverlayShiftX = 0f,
            chatOverlayShiftY = 0f,
            chatOverlayHeightDp = ChatOverlayStatus.Size.Normal.heightDp,
            chatOverlayWidthDp = ChatOverlayStatus.Size.Normal.widthDp,
        )
    }
}
