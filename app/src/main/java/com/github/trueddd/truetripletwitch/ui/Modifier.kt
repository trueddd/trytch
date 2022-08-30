package com.github.trueddd.truetripletwitch.ui

import androidx.compose.ui.Modifier

fun Modifier.modifyIf(condition: Boolean, modifierBlock: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.modifierBlock()
    } else {
        this
    }
}
