package com.github.trueddd.truetripletwitch.ui.screens.stream

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.trueddd.truetripletwitch.R
import com.github.trueddd.truetripletwitch.ui.theme.HalfTransparentBlack
import com.github.trueddd.twitch.data.Stream
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@Preview(widthDp = 400, heightDp = 250)
@Composable
fun PlayerContainerPreview() {
    PlayerContainer(
        stream = Stream.test(),
        defaultControlsVisibility = true,
        playerEventsFlow = MutableSharedFlow(),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerContainer(
    player: ExoPlayer? = null,
    stream: Stream? = null,
    playerStatus: PlayerStatus = PlayerStatus.test(),
    defaultControlsVisibility: Boolean = false,
    playerEventsFlow: MutableSharedFlow<PlayerEvent>,
) {
    var controlsVisible by remember { mutableStateOf(defaultControlsVisibility) }
    var playerZoom by remember { mutableStateOf(1f) }
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
            .pointerInput(Unit) {
                val zoomRange = 0.9f..1.1f
                detectTransformGestures(
                    onGesture = { _, _, zoom, _ ->
                        if (playerZoom == zoomRange.start && zoom < 1.0f) {
                            return@detectTransformGestures
                        }
                        if (playerZoom == zoomRange.endInclusive && zoom > 1.0f) {
                            return@detectTransformGestures
                        }
                        val newZoom = playerZoom * zoom
                        playerZoom = newZoom.coerceIn(zoomRange)
                        when (newZoom) {
                            zoomRange.endInclusive -> PlayerStatus.AspectRatio.Zoom
                            zoomRange.start -> PlayerStatus.AspectRatio.Fit
                            else -> null
                        }?.let { playerEventsFlow.tryEmit(PlayerEvent.AspectRatioChange(it)) }
                    },
                )
            }
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { controlsVisible = !controlsVisible },
                onDoubleClick = { playerEventsFlow.tryEmit(PlayerEvent.AspectRatioChange(!playerStatus.aspectRatio)) },
            )
    ) {
        if (player != null && playerStatus.streamUri != null) {
            Player(player, playerStatus.aspectRatio)
        }
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .align(Alignment.TopCenter)
        ) {
            PlayerControls(
                playerStatus,
                playPauseClicked = {
                    if (playerStatus.isPlaying) {
                        player?.pause()
                    } else {
                        player?.seekToDefaultPosition()
                        player?.play()
                    }
                },
            )
        }
        AnimatedVisibility(
            visible = controlsVisible && stream != null,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .align(Alignment.BottomCenter)
        ) {
            stream?.let { StreamInfo(stream = it) }
        }
    }
}

// todo: add streamer avatar, viewer count, tags, etc.
@Composable
private fun StreamInfo(
    stream: Stream
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = stream.title,
                fontSize = 16.sp,
            )
            Text(
                text = stream.userName,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun Player(
    player: ExoPlayer,
    aspectRatio: PlayerStatus.AspectRatio,
) {
    // todo: implement player controls
    // todo: respond to lifecycle events (disconnect from chat)
    AndroidView(
        factory = {
            StyledPlayerView(it).apply {
                hideController()
                useController = false
                resizeMode = aspectRatio.value
                this.player = player
                keepScreenOn = true
            }
        },
        update = {
            it.resizeMode = aspectRatio.value
        },
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
fun PlayerControls(
    playerStatus: PlayerStatus,
    playPauseClicked: () -> Unit,
) {
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
                .clickable { playPauseClicked() }
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
