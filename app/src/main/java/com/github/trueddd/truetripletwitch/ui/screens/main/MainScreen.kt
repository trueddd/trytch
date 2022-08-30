package com.github.trueddd.truetripletwitch.ui.screens.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.truetripletwitch.ui.MainScreenState
import com.github.trueddd.truetripletwitch.ui.MainViewModel
import com.github.trueddd.truetripletwitch.ui.navigation.IntentHandler

class MainScreen(
    buildContext: BuildContext,
    private val mainViewModel: MainViewModel,
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
        MainScreen(
            state = state,
            onLoginButtonClicked = ::login,
            onLogoutButtonClicked = { mainViewModel.logout() },
        )
    }
}

@Preview
@Composable
private fun MainScreen(
    @PreviewParameter(MainScreenStateParameters::class)
    state: MainScreenState,
    onLoginButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Toolbar(state, onLoginButtonClicked, onLogoutButtonClicked)
        if (state.user != null) {
            Streams(streams = state.streams)
        }
    }
}

class MainScreenStateParameters : PreviewParameterProvider<MainScreenState> {
    override val values: Sequence<MainScreenState>
        get() = sequenceOf(MainScreenState.test())
}

