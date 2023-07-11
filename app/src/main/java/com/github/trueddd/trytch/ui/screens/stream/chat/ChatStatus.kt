package com.github.trueddd.trytch.ui.screens.stream.chat

import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.data.MessageWord
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ChatStatus(
    val messages: ImmutableList<ChatMessage>,
    val connectionStatus: ConnectionStatus,
) {

    companion object {

        fun test() = ChatStatus(
            messages = persistentListOf(
                ChatMessage(
                    channel = "twitch",
                    author = "truetripled",
                    userColor = "#1E90FF",
                    badges = listOf("https://static-cdn.jtvnw.net/badges/v1/b817aba4-fad8-49e2-b88a-7cc744dfa6ec/2"),
                    words = listOf(MessageWord.Default("hello"))
                ),
                ChatMessage(
                    channel = "twitch",
                    author = "eltripledo",
                    words = listOf(MessageWord.Default("hey,"), MessageWord.Default("everyone!!"))
                ),
                ChatMessage(
                    channel = "twitch",
                    author = "eltripledo",
                    words = listOf(MessageWord.Default("hey,"), MessageWord.Mention("@dan"))
                ),
                ChatMessage(
                    channel = "twitch",
                    author = "eltripledo",
                    words = listOf(MessageWord.Default("mega supa dupa long message which for sure will not fit in this damn screen i bet it would not"))
                ),
                ChatMessage(
                    channel = "twitch",
                    author = "truetripled",
                    userColor = "#1E90FF",
                    badges = listOf("https://static-cdn.jtvnw.net/badges/v1/b817aba4-fad8-49e2-b88a-7cc744dfa6ec/2"),
                    words = listOf(MessageWord.Default(":)"))
                )
            ),
            connectionStatus = ConnectionStatus.Connected,
        )
    }
}
