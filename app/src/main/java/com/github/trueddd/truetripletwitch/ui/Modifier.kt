package com.github.trueddd.truetripletwitch.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*

fun Modifier.modifyIf(condition: Boolean, modifierBlock: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.modifierBlock()
    } else {
        this
    }
}

fun Modifier.shimmerBackground(shape: Shape = RectangleShape): Modifier = composed {
    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 400f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1500, easing = LinearEasing),
            RepeatMode.Restart,
        )
    )
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.6f),
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation, translateAnimation),
        end = Offset(translateAnimation + 200f, translateAnimation + 200f),
        tileMode = TileMode.Mirror,
    )
    return@composed then(background(brush, shape))
}
