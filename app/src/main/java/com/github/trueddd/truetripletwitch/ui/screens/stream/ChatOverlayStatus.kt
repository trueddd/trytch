package com.github.trueddd.truetripletwitch.ui.screens.stream

import androidx.compose.runtime.Stable
import com.github.trueddd.truetripletwitch.settings.StreamSettings

data class ChatOverlayStatus(
    val enabled: Boolean,
    val opacity: Float,
    val shiftX: Float,
    val shiftY: Float,
    val size: Size,
) {

    sealed class Size(val widthDp: Int, val heightDp: Int) {

        object Small : Size(96, 196)
        object Normal : Size(128, 220)
        object Large : Size(164, 300)

        @Stable
        val order: Int
            get() = ChatOverlaySizes.indexOf(this)
    }

    companion object {

        fun test() = ChatOverlayStatus(
            enabled = true,
            opacity = 0.5f,
            shiftX = 0f,
            shiftY = 0f,
            size = Size.Normal,
        )
    }
}

val ChatOverlaySizes = listOf(
    ChatOverlayStatus.Size.Small,
    ChatOverlayStatus.Size.Normal,
    ChatOverlayStatus.Size.Large
)

fun chatOverlaySizeFrom(index: Int): ChatOverlayStatus.Size? {
    return ChatOverlaySizes.getOrNull(index)
}

fun StreamSettings.overlaySize(): ChatOverlayStatus.Size {
    return ChatOverlaySizes.firstOrNull {
        it.widthDp == chatOverlayWidthDp && it.heightDp == chatOverlayHeightDp
    } ?: ChatOverlayStatus.Size.Normal
}
