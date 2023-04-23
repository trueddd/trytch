package com.github.trueddd.truetripletwitch

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

val LocalImageLoader = staticCompositionLocalOf<ImageLoader> { error("No image loader found") }

@Composable
fun createImageLoader(): ImageLoader {
    return ImageLoader.Builder(LocalContext.current)
        .crossfade(false)
        .components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
}
