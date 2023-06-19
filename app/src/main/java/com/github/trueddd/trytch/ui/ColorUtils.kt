package com.github.trueddd.trytch.ui

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
