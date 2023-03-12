package com.github.trueddd.truetripletwitch.ui

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.parseHexColor(): Color? {
    return try {
        Color(toColorInt())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
