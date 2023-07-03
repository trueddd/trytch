package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
    ChatInput()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    initialText: String = "",
    onSendMessageClicked: (String) -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        var text by remember(initialText) { mutableStateOf(initialText) }
        var isFocused by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        TextField(
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
            placeholder = {
                Text(
                    text = "Send a message",
                )
            },
            trailingIcon = {
                if (!isFocused) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Emotes",
                    )
                }
            },
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = AppTheme.Primary,
                textColor = AppTheme.PrimaryText,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )
    }
}
