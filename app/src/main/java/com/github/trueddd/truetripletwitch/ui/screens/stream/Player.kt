package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.trueddd.truetripletwitch.R
import com.github.trueddd.truetripletwitch.ui.detectPlayerZoom
import com.github.trueddd.truetripletwitch.ui.theme.HalfTransparentBlack
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.data.Stream
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Preview(widthDp = 400, heightDp = 250)
@Composable
fun PlayerContainerPreview() {
    PlayerContainer(
        stream = Stream.test(),
        player = null,
        playerStatus = PlayerStatus.test(),
        chatOverlayStatus = ChatOverlayStatus.test(),
        chatStatus = ChatStatus.test(),
        defaultControlsVisibility = true,
        defaultSettingsVisibility = false,
    )
}

@Preview(widthDp = 400, heightDp = 250)
@Composable
fun PlayerContainerPreviewWithSettings() {
    PlayerContainer(
        stream = Stream.test(),
        player = null,
        playerStatus = PlayerStatus.test(),
        chatOverlayStatus = ChatOverlayStatus.test(),
        chatStatus = ChatStatus.test(),
        defaultControlsVisibility = true,
        defaultSettingsVisibility = true,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerContainer(
    stream: Stream?,
    player: ExoPlayer?,
    playerStatus: PlayerStatus,
    chatOverlayStatus: ChatOverlayStatus,
    chatStatus: ChatStatus,
    modifier: Modifier = Modifier,
    chatOverlayChecked: (Boolean) -> Unit = {},
    chatOverlayOpacityChanged: (Float) -> Unit = {},
    playerEvents: (PlayerEvent) -> Unit = {},
    defaultControlsVisibility: Boolean = false,
    defaultSettingsVisibility: Boolean = false,
) {
    var controlsVisible by remember { mutableStateOf(defaultControlsVisibility) }
    var settingsVisible by remember { mutableStateOf(defaultSettingsVisibility) }
    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(3.seconds)
            controlsVisible = false
        }
    }
    Box(
        modifier = modifier
            .background(Color.Black)
            .detectPlayerZoom(playerEvents)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (settingsVisible) {
                        settingsVisible = false
                    } else {
                        controlsVisible = !controlsVisible
                    }
                },
                onDoubleClick = { playerEvents(PlayerEvent.AspectRatioChange(!playerStatus.aspectRatio)) },
            )
    ) {
        if (player != null && playerStatus.streamUri != null) {
            Player(
                player,
                playerStatus.aspectRatio,
                Modifier.fillMaxSize(),
            )
        }
        ChatOverlay(
            chatOverlayStatus = chatOverlayStatus,
            chatStatus = chatStatus,
        )
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.background(HalfTransparentBlack))
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
                settingsClicked = { settingsVisible = !settingsVisible },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        if (playerStatus.isBuffering) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
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
        AnimatedVisibility(
            visible = settingsVisible,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        ) {
            SettingsPanel(
                playerStatus = playerStatus,
                chatOverlayStatus = chatOverlayStatus,
                chatOverlayChecked = chatOverlayChecked,
                onQualityClicked = {
                    settingsVisible = false
                    Log.d("Quality", "Changed to $it")
                    playerEvents(PlayerEvent.StreamQualityChange(it))
                },
                chatOverlayOpacityChanged = chatOverlayOpacityChanged,
            )
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
    modifier: Modifier = Modifier,
) {
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
        modifier = modifier,
    )
}

@Composable
fun PlayerControls(
    playerStatus: PlayerStatus,
    playPauseClicked: () -> Unit,
    settingsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        val playButtonResource = remember(playerStatus.isPlaying) {
            when (playerStatus.isPlaying) {
                true -> R.drawable.ic_pause_48
                false -> R.drawable.ic_play_arrow_48
            }
        }
        if (!playerStatus.isBuffering) {
            Image(
                painter = painterResource(playButtonResource),
                contentDescription = if (playerStatus.isPlaying) "Pause" else "Play",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .clickable { playPauseClicked() }
            )
        }
        Image(
            painter = painterResource(R.drawable.ic_settings_48),
            contentDescription = "Settings",
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.BottomEnd)
                .size(24.dp)
                .clickable { settingsClicked() }
        )
    }
}
