package com.github.trueddd.twitch.chat.irc

import kotlinx.coroutines.flow.StateFlow

interface ChatClient {

    fun connect(login: String, token: String)

    fun disconnect()

    val connectionStateFlow: StateFlow<ConnectionState>
}
