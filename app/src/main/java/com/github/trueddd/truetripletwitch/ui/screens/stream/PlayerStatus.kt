package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import com.github.trueddd.twitch.data.StreamInfo
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

data class PlayerStatus(
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val streamLinks: List<StreamInfo>,
    val streamUri: Uri?,
    val aspectRatio: AspectRatio,
    val selectedStream: String?,
) {

    sealed class AspectRatio(val value: Int) {
        object Zoom : AspectRatio(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
        object Fit : AspectRatio(AspectRatioFrameLayout.RESIZE_MODE_FIT)

        operator fun not() = if (this is Zoom) Fit else Zoom
    }

    companion object {
        fun default() = PlayerStatus(
            isPlaying = false,
            isBuffering = false,
            streamLinks = emptyList(),
            streamUri = null,
            aspectRatio = AspectRatio.Zoom,
            selectedStream = null,
        )
        fun test() = PlayerStatus(
            isPlaying = true,
            isBuffering = false,
            streamLinks = listOf(
                StreamInfo("1080p", "link"),
                StreamInfo("720p", "link"),
                StreamInfo("480p", "link"),
                StreamInfo("360p", "link"),
                StreamInfo("144p","link"),
            ),
            streamUri = Uri.EMPTY,
            aspectRatio = AspectRatio.Fit,
            selectedStream = "1080p",
        )
    }
}
