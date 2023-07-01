package com.github.trueddd.trytch.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.TiltedRectangle
import com.github.trueddd.trytch.ui.buildImageRequest
import com.github.trueddd.trytch.ui.theme.AppTheme

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
            .background(AppTheme.Primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.user == null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Button(
                    onClick = onLoginButtonClicked,
                    enabled = !state.userLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.Accent,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.main_login),
                        fontSize = 14.sp,
                        color = AppTheme.SecondaryText,
                    )
                }
            }
            AnimatedVisibility(
                visible = state.user != null,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                val user = state.user!!
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onProfileButtonClicked() }
                ) {
                    Text(
                        text = user.displayName,
                        color = AppTheme.AccentText,
                    )
                    AsyncImage(
                        model = buildImageRequest(user.profileImageUrl),
                        contentDescription = user.displayName,
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
