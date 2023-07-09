package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.trueddd.trytch.LocalImageLoader
import com.github.trueddd.trytch.ui.buildImageRequest
import com.github.trueddd.trytch.ui.parseHexColor
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.trytch.ui.toDp
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.data.MessageWord
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.collections.immutable.ImmutableList

@Preview
@Composable
private fun ChatPreview() {
    Chat(chatStatus = ChatStatus.test())
}

@Composable
fun Chat(
    chatStatus: ChatStatus,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(AppTheme.Primary)
    ) {
        ChatMessages(
            messages = chatStatus.messages,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxSize()
        )
        if (chatStatus.connectionStatus is ConnectionStatus.Connecting) {
            CircularProgressIndicator(
                color = AppTheme.Accent,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
        (chatStatus.connectionStatus as? ConnectionStatus.Disconnected)?.error?.let { error ->
            Text(
                text = "Chat connection error\n${error.message}",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun MessageWord(
    word: MessageWord,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    when (word) {
        is MessageWord.Default -> Text(
            text = word.content,
            fontSize = fontSize,
            color = AppTheme.AccentText,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier,
        )
        is MessageWord.Mention -> Text(
            text = word.content,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            color = AppTheme.TextMention,
            modifier = modifier,
        )
        is MessageWord.Emote -> {
            val emoteVersion = word.emote.versions.last()
            AsyncImage(
                model = emoteVersion.url,
                imageLoader = LocalImageLoader.current,
                contentDescription = word.content,
                modifier = modifier
                    .height(16.sp.toDp())
                    .width((16.sp.value * (emoteVersion.width.toFloat() / emoteVersion.height)).sp.toDp()),
            )
        }
        is MessageWord.UnknownTwitchEmote -> {
            AsyncImage(
                model = word.url(),
                imageLoader = LocalImageLoader.current,
                contentDescription = word.content,
                modifier = modifier
                    .size(16.sp.toDp()),
            )
        }
        is MessageWord.Link -> {
            val uriHandler = LocalUriHandler.current
            Text(
                text = word.content,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.Accent,
                textDecoration = TextDecoration.Underline,
                modifier = modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        // todo: show dialog before opening link
                        onClick = { uriHandler.openUri(word.content) },
                    )
            )
        }
    }
}

class ChatMessagePreviewParameterProvider : PreviewParameterProvider<ChatMessage> {
    override val values = sequenceOf(ChatMessage.test())
}

@Composable
private fun Message(
    @PreviewParameter(provider = ChatMessagePreviewParameterProvider::class)
    message: ChatMessage,
    fontSize: TextUnit = 16.sp,
) {
    FlowRow(
        mainAxisSpacing = 4.dp,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
    ) {
        message.badges.forEach { badgeUrl ->
            AsyncImage(
                model = buildImageRequest(badgeUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(fontSize.toDp()),
            )
        }
        Text(
            text = message.author,
            color = message.userColor?.parseHexColor() ?: AppTheme.PrimaryText,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
        )
        message.words.forEach {
            MessageWord(
                word = it,
                fontSize = fontSize,
            )
        }
    }
}

@Composable
fun ChatMessages(
    messages: ImmutableList<ChatMessage>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    scrollEnabled: Boolean = true,
) {
    LazyColumn(
        reverseLayout = true,
        contentPadding = PaddingValues(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Bottom),
        userScrollEnabled = scrollEnabled,
        modifier = modifier,
    ) {
        items(messages) {
            Message(message = it, fontSize = fontSize)
        }
    }
}
