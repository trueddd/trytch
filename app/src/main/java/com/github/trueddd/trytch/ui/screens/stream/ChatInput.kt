package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.theme.AppTheme

@Preview
@Composable
private fun ChatInput1() {
    ChatInput(
        initialText = "test message 123",
    )
}

@Preview
@Composable
private fun ChatInput2() {
    ChatInput(
        emotesOpen = true,
    )
}

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    initialText: String = "",
    emotesOpen: Boolean = false,
    onSendMessageClicked: (String) -> Unit = {},
    onEmoteButtonClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .background(AppTheme.Primary)
    ) {
        var text by remember(initialText) { mutableStateOf(initialText) }
        var isFocused by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(
                imeAction = if (text.isEmpty()) ImeAction.Previous else ImeAction.Send,
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onSendMessageClicked(text)
                    text = ""
                    focusManager.clearFocus()
                },
                onPrevious = {
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                color = AppTheme.PrimaryText,
            ),
            cursorBrush = SolidColor(AppTheme.Accent),
            decorationBox = { textField ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        textField()
                        if (text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.chat_send_message),
                                fontSize = 16.sp,
                                color = AppTheme.PrimaryTextDark,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Emotes",
                        tint = if (emotesOpen) AppTheme.Accent else AppTheme.PrimaryTextDark,
                        modifier = Modifier
                            .clickable(onClick = onEmoteButtonClicked)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .padding(8.dp)
                .background(AppTheme.SecondaryText, RoundedCornerShape(8.dp))
        )
    }
}
