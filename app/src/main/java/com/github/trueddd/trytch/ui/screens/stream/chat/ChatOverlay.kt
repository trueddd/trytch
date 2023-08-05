package com.github.trueddd.trytch.ui.screens.stream.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.ui.isLandscape
import kotlin.math.roundToInt

@Composable
fun ChatOverlay(
    chatOverlayStatus: ChatOverlayStatus,
    chatStatus: ChatStatus,
    modifier: Modifier = Modifier,
    onChatOverlayDragged: (Offset) -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val deviceScreenSize = remember(configuration, density) {
        val widthDp = configuration.screenWidthDp.toFloat().let { Dp(it) }
        val heightDp = configuration.screenHeightDp.toFloat().let { Dp(it) }
        with(density) {
            val maxWidth = widthDp.toPx() - chatOverlayStatus.size.widthDp.dp.toPx()
            val maxHeight = heightDp.toPx() - chatOverlayStatus.size.heightDp.dp.toPx()
            Size(maxWidth, maxHeight)
        }
    }
    if (chatOverlayStatus.enabled && configuration.isLandscape) {
        var positionX by remember(chatOverlayStatus.shiftX) { mutableStateOf(chatOverlayStatus.shiftX) }
        var positionY by remember(chatOverlayStatus.shiftY) { mutableStateOf(chatOverlayStatus.shiftY) }
        Box(
            modifier = modifier
                .offset { IntOffset(positionX.roundToInt(), positionY.roundToInt()) }
                .size(chatOverlayStatus.size.widthDp.dp, chatOverlayStatus.size.heightDp.dp)
                .background(Color.Black.copy(alpha = chatOverlayStatus.opacity))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            positionX = (positionX + dragAmount.x).coerceIn(0f, deviceScreenSize.width)
                            positionY = (positionY + dragAmount.y).coerceIn(0f, deviceScreenSize.height)
                        },
                        onDragEnd = { onChatOverlayDragged(Offset(positionX, positionY)) },
                    )
                }
        ) {
            ChatMessages(
                messages = chatStatus.messages,
                fontSize = 8.sp,
                scrollEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}
