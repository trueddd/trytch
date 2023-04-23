package com.github.trueddd.truetripletwitch.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.trueddd.truetripletwitch.LocalImageLoader
import com.github.trueddd.truetripletwitch.ui.parseHexColor
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.data.MessageWord
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

class ChatStatusParameterProvider : PreviewParameterProvider<ChatStatus> {
    override val values = sequenceOf(
        ChatStatus(listOf(
            ChatMessage("truetripled", "#1E90FF", listOf("https://static-cdn.jtvnw.net/badges/v1/b817aba4-fad8-49e2-b88a-7cc744dfa6ec/2"), listOf(MessageWord.Default("hello"))),
            ChatMessage("eltripledo", words = listOf(MessageWord.Default("hey,"), MessageWord.Default("everyone!!"))),
            ChatMessage("eltripledo", words = listOf(MessageWord.Default("mega supa dupa long message which for sure will not fit in this damn screen i bet it would not"))),
            ChatMessage("truetripled", "#1E90FF", listOf("https://static-cdn.jtvnw.net/badges/v1/b817aba4-fad8-49e2-b88a-7cc744dfa6ec/2"), listOf(MessageWord.Default(":)"))),
        ), ConnectionStatus.Connected),
    )
}

@Preview
@Composable
fun Chat(
    @PreviewParameter(provider = ChatStatusParameterProvider::class)
    chatStatus: ChatStatus,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ChatMessages(messages = chatStatus.messages)
        if (chatStatus.connectionStatus is ConnectionStatus.Connecting) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
        if (chatStatus.connectionStatus is ConnectionStatus.Disconnected) {
            Text(
                text = "Chat connection error",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun MessageWord(word: MessageWord) {
    when (word) {
        is MessageWord.Default -> Text(
            text = word.content,
            color = MaterialTheme.colorScheme.onBackground,
        )
        is MessageWord.Mention -> Text(
            text = word.content,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        is MessageWord.Emote -> {
            val emoteVersion = word.emote.versions.last()
            AsyncImage(
                model = emoteVersion.url,
                imageLoader = LocalImageLoader.current,
                contentDescription = word.content,
                modifier = Modifier
                    .height(16.sp.value.dp)
                    .width((16.sp.value * (emoteVersion.width.toFloat() / emoteVersion.height)).dp),
            )
        }
        is MessageWord.Link -> {
            val uriHandler = LocalUriHandler.current
            Text(
                text = word.content,
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
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

@Preview(widthDp = 360)
@Composable
fun Message(
    message: ChatMessage = ChatMessage("elptripledo", words = listOf(MessageWord.Default("Hello, my name is very long!")))
) {
    FlowRow(
        mainAxisSpacing = 4.dp,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
    ) {
        message.badges.forEach { badgeUrl ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(badgeUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(Dp(16.sp.value)),
            )
        }
        Text(
            text = message.author,
            color = message.userColor?.parseHexColor() ?: MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        message.words.forEach { MessageWord(it) }
    }
}

@Composable
fun ChatMessages(messages: List<ChatMessage>) {
    LazyColumn(
        reverseLayout = true,
        contentPadding = PaddingValues(horizontal = 4.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(messages) {
            Message(message = it)
        }
    }
}
