package com.github.trueddd.twitch.chat

import android.util.Log
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.data.ChatMessage
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
import com.ktmi.tmi.events.filterMessage
import com.ktmi.tmi.events.onConnected
import com.ktmi.tmi.events.onMessage
import com.ktmi.tmi.events.onTwitchMessage
import com.ktmi.tmi.messages.GlobalUserStateMessage
import com.ktmi.tmi.messages.TwitchMessage
import com.ktmi.tmi.messages.UserStateMessage
import com.ktmi.tmi.messages.UserStateRelated
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class ChatManagerImpl(
    private val badgesManager: TwitchBadgesManager,
    private val twitchDao: TwitchDao,
    private val emotesProvider: EmotesProvider,
) : ChatManager {

    companion object {
        const val TAG = "ChatManager"
    }

    private val chatMessageWordsParser by lazy {
        ChatMessageWordsParser(emotesProvider)
    }

    private var mainScope: MainScope? = null
        set(value) {
            if (field != value) {
                field?.disconnect()
            }
            field = value
            if (value == null) {
                return
            }
            value.getTwitchFlow()
                .filterMessage<GlobalUserStateMessage>()
                .onEach { message ->
                    val emoteSets = message.rawMessage.tags["emote-sets"]?.split(",") ?: return@onEach
                    if (emoteSets.isEmpty()) return@onEach
                    emotesProvider.updateEmoteSets(emoteSets)
                }
                .launchIn(value)
        }
    private val chatClientsMap = mutableMapOf<String, ChannelScope>()

    private val chatMessagesFlow = MutableSharedFlow<ChatMessage>()

    override fun getMessagesFlow(channel: String): Flow<ChatMessage> {
        return chatMessagesFlow.filter { it.channel.contentEquals(channel, ignoreCase = true) }
    }

    private suspend fun UserStateRelated.toChatMessage(
        content: String,
        twitchEmotesInfo: TwitchEmotesInfo,
    ): ChatMessage {
        return coroutineScope {
            val messageWords = async(Dispatchers.Default) {
                chatMessageWordsParser.split(content, twitchEmotesInfo)
            }
            val badges = async(Dispatchers.IO) {
                badges?.mapNotNull { (name, tier) ->
                    badgesManager.getBadgeUrl(channel, name, tier)
                } ?: emptyList()
            }
            ChatMessage(
                author = displayName ?: username!!,
                channel = channel.removePrefix("#"),
                userColor = color?.ifEmpty { null },
                badges = badges.await(),
                words = messageWords.await(),
            )
        }
    }

    override suspend fun sendMessage(channel: String, text: String) {
        chatClientsMap[channel]?.let { client ->
            coroutineScope {
                val userName = async(Dispatchers.IO) { twitchDao.getUser()?.displayName }
                val userStateMessage = client.getTwitchFlow()
                    .onStart { client.sendMessage(channel, text) }
                    .filterMessage<UserStateMessage>()
                    .first { it.displayName == userName.await() }
                val message = userStateMessage.toChatMessage(text, TwitchEmotesInfo.NotIncluded)
                chatMessagesFlow.emit(message)
            }
        }
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun initialize() {
        if (mainScope != null) {
            return
        }
        GlobalScope.launch {
            resolveMainScope()
        }
    }

    override fun connectChat(channel: String): Flow<ConnectionStatus> {
        return channelFlow {
            send(ConnectionStatus.Connecting)
            resolveMainScope()?.channel(channel) {
                chatClientsMap[channel] = this
                join(channel)
                send(ConnectionStatus.Connected)
                onTwitchMessage<TwitchMessage> {
                    Log.d(TAG, "TwitchMessage: ${it.rawMessage.raw}")
                }
                onMessage {
                    val chatMessage = message.toChatMessage(
                        content = message.message,
                        twitchEmotesInfo = TwitchEmotesInfo.Included.from(emotes = message.emotes ?: emptyList())
                    )
                    Log.d(TAG, "Formed message: $chatMessage")
                    chatMessagesFlow.emit(chatMessage)
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
