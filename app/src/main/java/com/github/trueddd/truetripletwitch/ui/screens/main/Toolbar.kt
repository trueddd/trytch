package com.github.trueddd.truetripletwitch.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
private fun ToolbarPreview() {
    Toolbar(state = MainScreenState.test())
}

@Composable
fun Toolbar(
    state: MainScreenState,
    modifier: Modifier = Modifier,
    onLoginButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (state.user == null) {
                Button(
                    onClick = onLoginButtonClicked,
                    enabled = !state.userLoading,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Login"
                    )
                }
            } else {
                Text(text = state.user.displayName)
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onLogoutButtonClicked,
                    enabled = !state.userLoading,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                    )
                }
            }
        }
        if (state.userLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
