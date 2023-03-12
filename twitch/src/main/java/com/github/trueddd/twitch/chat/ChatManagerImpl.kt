package com.github.trueddd.twitch.chat

import android.util.Log
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.db.TwitchDao
import com.ktmi.tmi.commands.join
import com.ktmi.tmi.dsl.builder.scopes.MainScope
import com.ktmi.tmi.dsl.builder.scopes.tmi
import com.ktmi.tmi.dsl.plugins.Reconnect
import com.ktmi.tmi.events.onConnected
import com.ktmi.tmi.events.onMessage
import com.ktmi.tmi.events.onTwitchMessage
import com.ktmi.tmi.messages.TwitchMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import java.util.*

internal class ChatManagerImpl(
    private val twitchDao: TwitchDao,
) : ChatManager {

    companion object {
        const val TAG = "ChatManager"
    }

    override fun connectChat(channel: String): Flow<ChatStatus> {
        return channelFlow<ChatStatus> {
            val messages = LinkedList<ChatMessage>()
            send(ChatStatus(messages, ConnectionStatus.Connecting))
            val userToken = twitchDao.getUserToken() ?: run {
                send(ChatStatus(messages, ConnectionStatus.Disconnected(IllegalStateException("Token is null"))))
                return@channelFlow
            }
            var chatClient: MainScope? = null
            tmi(
                token = userToken,
                secure = true,
                context = coroutineContext,
            ) {
                + Reconnect(5)

                onConnected {
                    chatClient = this
                    join(channel)
                }

                onMessage {
                    val newMessage = ChatMessage(
                        author = message.displayName ?: message.username,
                        message.message,
                        userColor = message.color?.ifEmpty { null },
                    )
                    Log.d(TAG, "New message: $newMessage")
                    messages.add(0, newMessage)
                    if (messages.size > 100) {
                        messages.removeLast()
                    }
                    trySend(ChatStatus(messages.toList(), ConnectionStatus.Connected))
                }

                onTwitchMessage<TwitchMessage> { message ->
                    Log.d(TAG, "Event: ${message.rawMessage}")
                }
            }
            awaitClose {
                Log.d(TAG, "Disconnecting")
                chatClient?.disconnect()
                chatClient = null
            }
        }.flowOn(Dispatchers.IO)
    }
}