package com.github.trueddd.truetripletwitch.ui.screens.stream

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.trueddd.truetripletwitch.R
import com.github.trueddd.truetripletwitch.ui.theme.HalfTransparentBlack
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

@Preview(widthDp = 400, heightDp = 250)
@Composable
fun PlayerContainerPreview() {
    PlayerContainer(defaultControlsVisibility = true)
}

// todo: implement controls events
@Composable
fun PlayerContainer(
    player: ExoPlayer? = null,
    playerStatus: PlayerStatus = PlayerStatus.test(),
    defaultControlsVisibility: Boolean = false,
) {
    var controlsVisible by rememberSaveable { mutableStateOf(defaultControlsVisibility) }
    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(3_000L)
            controlsVisible = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { controlsVisible = !controlsVisible }
    ) {
        if (player != null && playerStatus.streamUri != null) {
            Player(player)
        }
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxSize()
        ) {
            PlayerControls(playerStatus)
        }
    }
}

@Composable
fun Player(player: ExoPlayer) {
    // todo: implement player controls
    // todo: respond to lifecycle events (disconnect from chat)
    AndroidView(
        factory = {
            StyledPlayerView(it).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                this.player = player
            }
        },
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
fun PlayerControls(playerStatus: PlayerStatus) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HalfTransparentBlack)
    ) {
        val playButtonResource = remember(playerStatus.isPlaying) {
            when (playerStatus.isPlaying) {
                true -> R.drawable.ic_pause_48
                false -> R.drawable.ic_play_arrow_48
            }
        }
        Image(
            painter = painterResource(playButtonResource),
            contentDescription = if (playerStatus.isPlaying) "Pause" else "Play",
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_settings_48),
            contentDescription = "Settings",
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.BottomEnd)
                .size(24.dp)
        )
    }
}
