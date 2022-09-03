package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri

data class StreamScreenState(
    val streamId: String,
    val streamUri: Uri?,
    val availableStreamLinks: Map<String, String>,
) {

    companion object {
        fun test() = StreamScreenState(
            streamId = "1234567890",
            streamUri = Uri.EMPTY,
            availableStreamLinks = mapOf(
                "360p" to "qwerty",
                "480p" to "qwerty",
                "720p" to "qwerty",
            ),
        )
    }
}
