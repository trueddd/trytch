package com.github.trueddd.truetripletwitch.ui.screens.stream

data class ChatOverlayStatus(
    val enabled: Boolean,
    val opacity: Float,
    val shiftX: Float,
    val shiftY: Float,
) {

    companion object {

        fun test() = ChatOverlayStatus(
            enabled = true,
            opacity = 0.5f,
            shiftX = 0f,
            shiftY = 0f,
        )
    }
}
