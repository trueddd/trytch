package com.github.trueddd.trytch.ui

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.github.trueddd.trytch.ui.screens.stream.PlayerEvent
import com.github.trueddd.trytch.ui.screens.stream.PlayerStatus

fun Modifier.modifyIf(condition: Boolean, modifierBlock: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.modifierBlock()
    } else {
        this
    }
}

fun Modifier.detectPlayerZoom(playerEvents: (PlayerEvent) -> Unit): Modifier {
    return this.pointerInput(Unit) {
        val zoomRange = 0.9f..1.1f
        var playerZoom = 1f
        detectTransformGestures(
            onGesture = { _, _, zoom, _ ->
                if (playerZoom == zoomRange.start && zoom < 1.0f) {
                    return@detectTransformGestures
                }
                if (playerZoom == zoomRange.endInclusive && zoom > 1.0f) {
                    return@detectTransformGestures
                }
                val newZoom = playerZoom * zoom
                playerZoom = newZoom.coerceIn(zoomRange)
                when (newZoom) {
                    zoomRange.endInclusive -> PlayerStatus.AspectRatio.Zoom
                    zoomRange.start -> PlayerStatus.AspectRatio.Fit
                    else -> null
                }?.let { playerEvents(PlayerEvent.AspectRatioChange(it)) }
            },
        )
    }
}
