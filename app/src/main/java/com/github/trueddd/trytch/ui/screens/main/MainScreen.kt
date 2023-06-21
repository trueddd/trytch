package com.github.trueddd.trytch.ui.screens.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.github.trueddd.trytch.navigation.IntentHandler
import com.github.trueddd.trytch.navigation.Routing
import com.github.trueddd.twitch.data.Stream

class MainScreen(
    private val mainViewModel: MainViewModel,
    private val backStack: BackStack<Routing>,
    buildContext: BuildContext,
) : Node(buildContext), IntentHandler {

    private fun login() {
        integrationPoint.activityStarter.startActivity {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(mainViewModel.getLinkForLogin())
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        mainViewModel.login(intent)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val state by mainViewModel.stateFlow.collectAsState()
        LaunchedEffect(state.user) {
            if (state.user != null && mainViewModel.shouldUpdateStreams()) {
                mainViewModel.updateStreams()
            }
        }
        MainScreen(
            state = state,
            onLoginButtonClicked = ::login,
            onProfileButtonClicked = { backStack.push(Routing.Profile) },
            onStreamClicked = { backStack.push(Routing.Stream(it.userName)) },
        )
    }
}

@Preview
@Composable
private fun MainScreen(
    @PreviewParameter(MainScreenStateParameters::class)
    state: MainScreenState,
    onLoginButtonClicked: () -> Unit = {},
    onProfileButtonClicked: () -> Unit = {},
    onStreamClicked: (Stream) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Toolbar(
            state,
            Modifier.fillMaxWidth(),
            onLoginButtonClicked,
            onProfileButtonClicked,
        )
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.user != null) {
                Streams(
                    streams = state.streams,
                    onStreamClicked = onStreamClicked,
                )
            }
            if (state.streamsLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

class MainScreenStateParameters : PreviewParameterProvider<MainScreenState> {
    override val values: Sequence<MainScreenState>
        get() = sequenceOf(MainScreenState.test())
}
