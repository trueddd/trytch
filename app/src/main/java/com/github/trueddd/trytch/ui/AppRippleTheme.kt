package com.github.trueddd.trytch.ui

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import com.github.trueddd.trytch.ui.theme.AppTheme

object AppRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() = AppTheme.Accent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        pressedAlpha = 0.30f,
        focusedAlpha = 0.12f,
        draggedAlpha = 0.08f,
        hoveredAlpha = 0.04f
    )
}
