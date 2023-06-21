package com.github.trueddd.trytch.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

@Composable
fun buildImageRequest(model: Any, crossfade: Boolean = true): ImageRequest {
    return ImageRequest.Builder(LocalContext.current)
        .data(model)
        .crossfade(crossfade)
        .build()
}
