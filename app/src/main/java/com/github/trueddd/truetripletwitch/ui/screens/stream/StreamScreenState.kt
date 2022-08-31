package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri

data class StreamScreenState(
    val streamId: String,
    val streamUri: Uri?,
) {

    companion object {
        fun test() = StreamScreenState(
            streamId = "1234567890",
            streamUri = Uri.EMPTY,
        )
    }
}
