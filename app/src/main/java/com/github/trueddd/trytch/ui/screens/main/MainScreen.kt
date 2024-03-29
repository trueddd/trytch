package com.github.trueddd.trytch.ui.screens.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.github.trueddd.trytch.BuildConfig
import com.github.trueddd.trytch.navigation.IntentHandler
import com.github.trueddd.trytch.navigation.Routing
import com.github.trueddd.trytch.ui.theme.AppTheme
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

    private fun openSelfStream() {
        if (BuildConfig.DEBUG) {
            mainViewModel.stateFlow.value.user?.login?.let {
                backStack.push(Routing.Stream(it))
            }
        }
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
            onProfileButtonLongClicked = { openSelfStream() },
            onStreamClicked = { backStack.push(Routing.Stream(it.userName)) },
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(state = MainScreenState.test())
}

@Preview
@Composable
private fun MainScreenNoStreamsPreview() {
    MainScreen(state = MainScreenState.noStreamsTest())
}

@Preview
@Composable
private fun MainScreenNoUserPreview() {
    MainScreen(state = MainScreenState.noUserTest())
}

@Composable
private fun MainScreen(
    state: MainScreenState,
    onLoginButtonClicked: () -> Unit = {},
    onProfileButtonClicked: () -> Unit = {},
    onProfileButtonLongClicked: () -> Unit = {},
    onStreamClicked: (Stream) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.Primary)
    ) {
        Toolbar(
            state,
            Modifier.fillMaxWidth(),
            onLoginButtonClicked,
            onProfileButtonClicked,
            onProfileButtonLongClicked,
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.user == null -> {
                    NoUser(modifier = Modifier.align(Alignment.Center))
                }
                state.streams.isEmpty() && !state.streamsLoading -> {
                    NoStreams(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    Streams(
                        streams = state.streams,
                        onStreamClicked = onStreamClicked,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            if (state.streamsLoading) {
                CircularProgressIndicator(
                    color = AppTheme.Accent,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}
