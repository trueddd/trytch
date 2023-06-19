package com.github.trueddd.trytch.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.buildImageRequest

class ProfileScreen(
    private val profileViewModel: ProfileViewModel,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val screenState by profileViewModel.stateFlow.collectAsState()
        LaunchedEffect(Unit) {
            profileViewModel.initProfileScreen()
        }
        ProfileScreen(
            screenState = screenState,
            onBackButtonClicked = { profileViewModel.navigateBack() },
            onLogoutButtonClicked = { profileViewModel.logout() },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

class ProfileScreenStateProvider : PreviewParameterProvider<ProfileScreenState> {
    override val values = sequenceOf(ProfileScreenState.test())
}

@Preview(showSystemUi = true)
@Composable
private fun ProfileScreen(
    @PreviewParameter(provider = ProfileScreenStateProvider::class)
    screenState: ProfileScreenState,
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box {
                AsyncImage(
                    model = screenState.user.offlineImageUrl,
                    contentDescription = "${screenState.user.displayName} offline image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .align(Alignment.TopCenter)
                        .background(Color.Gray)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .clickable { onBackButtonClicked() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    AsyncImage(
                        model = buildImageRequest(screenState.user.profileImageUrl),
                        contentDescription = "${screenState.user.displayName} avatar",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .border(4.dp, Color.Gray, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .background(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = screenState.user.displayName,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = screenState.user.description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
        Button(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, Color.Red),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onClick = onLogoutButtonClicked,
        ) {
            Text(
                text = stringResource(R.string.profile_logout).uppercase(),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }
    }
}
