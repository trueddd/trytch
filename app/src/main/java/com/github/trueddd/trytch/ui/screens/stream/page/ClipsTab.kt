package com.github.trueddd.trytch.ui.screens.stream.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.CoilImage
import com.github.trueddd.trytch.ui.buildImageRequest
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.twitch.data.User

// TODO: replace with composable fun
class Clips(
    private val user: User,
    private val clipsState: StreamerPageState.ClipsState,
    private val loadCallback: (LoadOptions) -> Unit,
) : PageTab(R.string.streamer_screen_clips_tab) {

    data class LoadOptions(
        val user: User,
    )

    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            loadCallback(LoadOptions(user))
        }
        ClipsContent(user, clipsState, loadCallback)
    }
}

@Preview
@Composable
private fun ClipsContent(
    user: User = User.test(),
    clipsState: StreamerPageState.ClipsState = StreamerPageState.ClipsState.test(),
    loadCallback: (Clips.LoadOptions) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn {
            items(clipsState.clips) { item ->
                Box(modifier = Modifier) {
                    val interactionSource = remember { MutableInteractionSource() }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .background(AppTheme.Primary)
                            .clickable(
                                indication = rememberRipple(),
                                interactionSource = interactionSource,
                                onClick = { loadCallback(Clips.LoadOptions(user)) },
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(2f)
                                .aspectRatio(16 / 9f)
                        ) {
                            CoilImage(
                                model = buildImageRequest(item.thumbnailUrl),
                                contentDescription = item.title,
                                modifier = Modifier
                                    .background(AppTheme.Secondary)
                            )
//                            StreamViewers(viewersCountText = stream.shortenedViewerCount)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(3f)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = item.title,
                                    color = AppTheme.AccentText,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                )
                                Text(
                                    text = item.creatorName,
                                    color = AppTheme.PrimaryText,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                )
                                Text(
                                    text = item.gameId,
                                    color = AppTheme.PrimaryTextDark,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
