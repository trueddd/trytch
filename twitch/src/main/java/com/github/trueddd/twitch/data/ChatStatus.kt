package com.github.trueddd.twitch.data

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
                    author = "truetripled",
                    userColor = "#1E90FF",
                    badges = listOf("https://static-cdn.jtvnw.net/badges/v1/b817aba4-fad8-49e2-b88a-7cc744dfa6ec/2"),
                    words = listOf(MessageWord.Default("hello"))
                ),
                ChatMessage(
                    author = "eltripledo",
                    words = listOf(MessageWord.Default("hey,"), MessageWord.Default("everyone!!"))
                ),
                ChatMessage(
                    author = "eltripledo",
                    words = listOf(MessageWord.Default("mega supa dupa long message which for sure will not fit in this damn screen i bet it would not"))
                ),
                ChatMessage(
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
