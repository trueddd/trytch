package com.github.trueddd.truetripletwitch.ui.screens.stream

data class ChatOverlayStatus(
    val enabled: Boolean,
    val opacity: Float,
) {

    companion object {

        fun test() = ChatOverlayStatus(
            enabled = true,
            opacity = 0.5f,
        )

        fun default() = ChatOverlayStatus(
            enabled = true,
            opacity = 0.5f,
        )
    }
}
