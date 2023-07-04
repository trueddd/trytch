package com.github.trueddd.twitch.chat.irc

import android.util.Log
import com.github.trueddd.twitch.chat.ChatManagerImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChatClientImpl : ChatClient, CoroutineScope {

    companion object {
        const val TAG = "ChatClient"
    }

    private val client by lazy {
        HttpClient {
            install(WebSockets)
        }
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private val connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    override val connectionStateFlow: StateFlow<ConnectionState>
        get() = connectionState

    private val disconnectSignal = MutableSharedFlow<Unit>()

    override fun connect(login: String, token: String) {
        launch {
            Log.d(TAG, "Connecting chat...")
            client.webSocket("wss://irc-ws.chat.twitch.tv:443") {
                disconnectSignal
                    .onEach { close() }
                    .launchIn(this)
                incoming.receiveAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .onEach { frame ->
                        Log.d(TAG, "IRC message | ${frame.readText()}")
//                        when (val text = frame.readText()) {
//                        }
                    }
                    .launchIn(this)
                connectionState.emit(ConnectionState.Connecting)
                send("PASS oauth:$token")
                send("NICK $login")
//                send("CAP REQ :twitch.tv/membership twitch.tv/tags twitch.tv/commands")
                val loginResult = (incoming.receive() as? Frame.Text)?.readText()
                if (loginResult?.contains("welcome, glhf!", ignoreCase = true) == true) {
                    incoming.receiveAsFlow()
                        .onStart {
                            connectionState.emit(ConnectionState.Connected)
                        }
                        .filterIsInstance<Frame.Text>()
                        .collect { frame ->
                            when (val text = frame.readText()) {
                            }
                        }
                } else {
                    connectionState.emit(ConnectionState.Disconnected)
                }
            }
        }
    }

    override fun disconnect() {
        launch {
            Log.d(TAG, "Disconnecting chat")
            disconnectSignal.emit(Unit)
        }
    }
}