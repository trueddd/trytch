package com.github.trueddd.trytch.player

import com.google.android.exoplayer2.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Player.playbackStateFlow(): Flow<PlaybackState> {
    return callbackFlow {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                trySend(PlaybackState(isBuffering = playbackState == Player.STATE_BUFFERING, isPlaying = isPlaying))
            }
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                trySend(PlaybackState(isBuffering = playbackState == Player.STATE_BUFFERING, isPlaying = isPlaying))
            }
        }
        addListener(listener)
        awaitClose {
            removeListener(listener)
        }
    }
}

data class PlaybackState(
    val isBuffering: Boolean,
    val isPlaying: Boolean,
)
