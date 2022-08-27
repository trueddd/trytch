package com.github.trueddd.truetripletwitch.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.trueddd.truetripletwitch.ui.MainScreenState

@Preview
@Composable
fun MainScreen(
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

