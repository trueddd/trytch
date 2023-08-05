package com.github.trueddd.trytch.ui.screens.stream.page

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

sealed class PageTab(
    @StringRes
    val nameResId: Int,
) {

    @Composable
    abstract fun Content()
}
