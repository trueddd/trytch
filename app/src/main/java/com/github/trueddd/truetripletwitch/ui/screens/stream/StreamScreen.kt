package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.truetripletwitch.ui.modifyIf

class StreamScreen(
    private val streamId: String,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        StreamScreen(
            streamId = streamId,
        )
    }
}

@Preview
@Composable
fun StreamScreen(
    streamId: String = "qwerty",
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        LocalConfiguration.current.orientation
        Box(
            modifier = Modifier
                .modifyIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    this.fillMaxWidth()
                        .fillMaxHeight(0.3f)
                }
                .modifyIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    this.fillMaxSize()
                }
                .background(MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = streamId,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}
