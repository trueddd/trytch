package com.github.trueddd.twitch.chat

import android.util.Log
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.emotes.EmotesProvider
import com.ktmi.tmi.commands.join
import com.ktmi.tmi.commands.leave
import com.ktmi.tmi.commands.sendMessage
import com.ktmi.tmi.dsl.builder.scopes.ChannelScope
import com.ktmi.tmi.dsl.builder.scopes.MainScope
import com.ktmi.tmi.dsl.builder.scopes.channel
import com.ktmi.tmi.dsl.builder.scopes.tmi
import com.ktmi.tmi.dsl.plugins.Reconnect
import com.ktmi.tmi.events.onConnected
import com.ktmi.tmi.events.onMessage
import com.ktmi.tmi.events.onTwitchMessage
import com.ktmi.tmi.messages.TwitchMessage
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import java.util.LinkedList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: Implement chat IRC client
internal class ChatManagerImpl(
    private val badgesManager: TwitchBadgesManager,
    private val twitchDao: TwitchDao,
    emotesProvider: EmotesProvider,
) : ChatManager {

    companion object {
        const val TAG = "ChatManager"
    }

    private val chatMessageWordsParser by lazy {
        ChatMessageWordsParser(emotesProvider)
    }

    private var mainScope: MainScope? = null
    private val chatClientsMap = mutableMapOf<String, ChannelScope>()

    override fun sendMessage(channel: String, text: String) {
        chatClientsMap[channel]?.sendMessage(channel, text)
    }

    private suspend fun resolveMainScope(): MainScope? {
        return mainScope ?: run {
            val userToken = twitchDao.getUserToken() ?: return null
            suspendCoroutine {
                tmi(
                    token = userToken,
                    secure = true,
                ) {
                    + Reconnect(5)
                    onConnected {
                        Log.d(TAG, "Chat connected")
                        mainScope = this
                        it.resume(this)
                    }
                }
            }
        }
    }

    override fun connectChat(channel: String): Flow<ChatStatus> {
        return channelFlow {
            val messages = LinkedList<ChatMessage>()
            send(ChatStatus(messages.toImmutableList(), ConnectionStatus.Connecting))
            resolveMainScope()?.channel(channel) {
                chatClientsMap[channel] = this
                join(channel)
                send(ChatStatus(messages.toImmutableList(), ConnectionStatus.Connected))
                onTwitchMessage<TwitchMessage> {
                    Log.d(TAG, "TwitchMessage: ${it.rawMessage.raw}")
                }
                onMessage {
                    Log.d(TAG, "${this.message.displayName} ${message.message}")
                    val badges = message.badges
                        ?.mapNotNull { (name, tier) -> badgesManager.getBadgeUrl(channel, name, tier) }
                        ?: emptyList()
                    val newMessage = ChatMessage(
                        author = message.displayName ?: message.username,
                        userColor = message.color?.ifEmpty { null },
                        badges = badges,
                        words = chatMessageWordsParser.split(message.message),
                    )
                    Log.d(TAG, "New message: $newMessage")
                    messages.add(0, newMessage)
                    if (messages.size > 100) {
                        messages.removeLast()
                    }
                    trySend(ChatStatus(messages.toImmutableList(), ConnectionStatus.Connected))
                }
            }
            awaitClose {
                Log.d(TAG, "Disconnecting")
                chatClientsMap[channel]?.leave(channel)
                chatClientsMap.remove(channel)
            }
        }.flowOn(Dispatchers.IO)
    }
}
