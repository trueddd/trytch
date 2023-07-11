package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.screens.stream.chat.ChatOverlaySizes
import com.github.trueddd.trytch.ui.screens.stream.chat.ChatOverlayStatus
import com.github.trueddd.trytch.ui.screens.stream.chat.chatOverlaySizeFrom
import com.github.trueddd.trytch.ui.theme.AppTheme
import kotlin.math.roundToInt

@Preview
@Composable
private fun SettingsPanelPreview() {
    SettingsPanel(
        playerStatus = PlayerStatus.test(),
        chatOverlayStatus = ChatOverlayStatus.test(),
        onQualityClicked = {},
        chatOverlayChecked = {},
        chatOverlayOpacityChanged = {},
        chatOverlaySizeChanged = {},
    )
}

@Composable
fun SettingsPanel(
    playerStatus: PlayerStatus,
    chatOverlayStatus: ChatOverlayStatus,
    onQualityClicked: (String) -> Unit,
    chatOverlayChecked: (Boolean) -> Unit,
    chatOverlayOpacityChanged: (Float) -> Unit,
    chatOverlaySizeChanged: (ChatOverlayStatus.Size) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(AppTheme.Primary)
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        AppTheme.SecondaryText,
                        RoundedCornerShape(8.dp)
                    ),
            ) {
                Text(
                    text = stringResource(R.string.stream_quality_title),
                    color = AppTheme.AccentText,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                ) {
                    playerStatus.streamLinks.forEach { (quality, _) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxWidth()
                                .clickable { onQualityClicked(quality) }
                        ) {
                            RadioButton(
                                selected = quality == playerStatus.selectedStream,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AppTheme.Accent,
                                    unselectedColor = AppTheme.PrimaryText,
                                ),
                                modifier = Modifier
                            )
                            Text(
                                text = quality,
                                color = if (quality == playerStatus.selectedStream) AppTheme.Accent else AppTheme.PrimaryText,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .background(AppTheme.SecondaryText, RoundedCornerShape(8.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.stream_chat_overlay_title),
                        color = AppTheme.AccentText,
                        modifier = Modifier
                    )
                    Switch(
                        checked = chatOverlayStatus.enabled,
                        onCheckedChange = chatOverlayChecked,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = AppTheme.Accent,
                            checkedThumbColor = Color.White,
                            uncheckedBorderColor = AppTheme.PrimaryTextDark,
                            uncheckedThumbColor = AppTheme.PrimaryTextDark,
                            uncheckedTrackColor = AppTheme.Primary,
                        ),
                        modifier = Modifier
                            .scale(0.8f)
                            .padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.stream_chat_overlay_opacity),
                        color = AppTheme.PrimaryText,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                    var sliderValue by remember(chatOverlayStatus.opacity) {
                        mutableStateOf(chatOverlayStatus.opacity)
                    }
                    Slider(
                        value = sliderValue,
                        enabled = chatOverlayStatus.enabled,
                        colors = SliderDefaults.colors(
                            activeTrackColor = AppTheme.Accent,
                            thumbColor = AppTheme.Accent,
                        ),
                        onValueChange = { sliderValue = it },
                        onValueChangeFinished = { chatOverlayOpacityChanged(sliderValue) },
                        modifier = Modifier
                            .width(120.dp)
                            .padding(end = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.stream_chat_overlay_size),
                        color = AppTheme.PrimaryText,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                    var sliderValue by remember(chatOverlayStatus.size) {
                        chatOverlayStatus.size.order
                            .toFloat()
                            .let { mutableStateOf(it) }
                    }
                    val steps = ChatOverlaySizes.count() - 2
                    Slider(
                        value = sliderValue,
                        valueRange = 0f .. ChatOverlaySizes.size.minus(1).toFloat(),
                        steps = steps,
                        colors = SliderDefaults.colors(
                            activeTrackColor = AppTheme.Accent,
                            thumbColor = AppTheme.Accent,
                        ),
                        enabled = chatOverlayStatus.enabled,
                        onValueChange = { sliderValue = it },
                        onValueChangeFinished = {
                            chatOverlaySizeFrom(sliderValue.roundToInt())
                                ?.let(chatOverlaySizeChanged)
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .padding(end = 8.dp)
                    )
                }
            }
        }
    }
}
