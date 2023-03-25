package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

data class PlayerStatus(
    val isPlaying: Boolean,
    val streamLinks: Map<String, String>,
    val streamUri: Uri?,
    val aspectRatio: AspectRatio,
) {

    sealed class AspectRatio(val value: Int) {
        object Zoom : AspectRatio(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
        object Fit : AspectRatio(AspectRatioFrameLayout.RESIZE_MODE_FIT)

        operator fun not() = if (this is Zoom) Fit else Zoom
    }

    companion object {
        fun default() = PlayerStatus(
            isPlaying = false,
            streamLinks = emptyMap(),
            streamUri = null,
            aspectRatio = AspectRatio.Zoom,
        )
        fun test() = PlayerStatus(
            isPlaying = true,
            streamLinks = mapOf(
                "144p" to "link",
                "360p" to "link",
                "480p" to "link",
                "720p" to "link",
                "1080p" to "link",
            ),
            streamUri = Uri.EMPTY,
            aspectRatio = AspectRatio.Fit,
        )
    }
}
