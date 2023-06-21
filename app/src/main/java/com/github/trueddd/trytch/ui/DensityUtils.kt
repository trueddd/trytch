package com.github.trueddd.trytch.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextUnit.toDp(): Dp {
    return with(LocalDensity.current) {
        this@toDp.toDp()
    }
}
