package com.github.trueddd.truetripletwitch.player

import com.google.android.exoplayer2.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Player.playbackStateFlow(): Flow<Boolean> {
    return callbackFlow {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                trySend(isPlaying)
            }
        }
        addListener(listener)
        awaitClose {
            removeListener(listener)
        }
    }
}
