package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.Lifecycle
import com.bumble.appyx.core.lifecycle.asFlow
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.truetripletwitch.ui.modifyIf
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StreamScreen(
    private val streamViewModel: StreamViewModel,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        LaunchedEffect(Unit) {
            lifecycle.asFlow()
                .filter { it != Lifecycle.State.RESUMED }
                .distinctUntilChanged()
                .onEach { streamViewModel.player.pause() }
                .launchIn(this)
        }
        val state by streamViewModel.stateFlow.collectAsState()
        StreamScreen(state, streamViewModel.player)
    }
}

@Preview
@Composable
fun StreamScreen(
    @PreviewParameter(provider = StreamScreenStateProvider::class)
    state: StreamScreenState,
    player: ExoPlayer? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Box(
            modifier = Modifier
                .modifyIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    this
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                }
                .modifyIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    this.fillMaxSize()
                }
                .background(MaterialTheme.colorScheme.error)
        ) {
            PlayerContainer(player, state.playerStatus)
        }
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Chat(state.chatStatus)
        }
    }
}

class StreamScreenStateProvider : PreviewParameterProvider<StreamScreenState> {
    override val values = sequenceOf(StreamScreenState.test())
}
