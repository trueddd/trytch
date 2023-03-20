package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri

data class PlayerStatus(
    val isPlaying: Boolean,
    val streamLinks: Map<String, String>,
    val streamUri: Uri?,
) {

    companion object {
        fun default() = PlayerStatus(
            isPlaying = false,
            streamLinks = emptyMap(),
            streamUri = null,
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
        )
    }
}
