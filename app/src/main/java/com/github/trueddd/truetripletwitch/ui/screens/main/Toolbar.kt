package com.github.trueddd.truetripletwitch.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.trueddd.truetripletwitch.R
import com.github.trueddd.truetripletwitch.ui.TiltedRectangle
import com.github.trueddd.truetripletwitch.ui.buildImageRequest

@Preview
@Composable
private fun ToolbarPreview() {
    Toolbar(state = MainScreenState.test())
}

@Preview
@Composable
private fun NoUserToolbarPreview() {
    Toolbar(state = MainScreenState.default())
}

@Composable
fun Toolbar(
    state: MainScreenState,
    modifier: Modifier = Modifier,
    onLoginButtonClicked: () -> Unit = {},
    onProfileButtonClicked: () -> Unit = {},
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
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.main_login),
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onProfileButtonClicked() }
                ) {
                    Text(text = state.user.displayName)
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = buildImageRequest(state.user.profileImageUrl),
                        contentDescription = state.user.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(2f)
                            .clip(TiltedRectangle(20f))
                            .background(Color.Gray)
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
