package com.github.trueddd.twitch.emotes

import android.util.Log
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.db.EmoteDao
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.bttv.BttvChannelEmotesResponse
import com.github.trueddd.twitch.dto.bttv.BttvEmote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// TODO: implement real-time emotes updates - https://betterttv.com/developers/websocket
internal class BetterTTVEmotesProvider(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
    private val emoteDao: EmoteDao,
) : EmoteStorage, CoroutineScope {

    companion object {
        private const val TAG = "BTTVEmotesProvider"
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private fun String.channelEmotesUrl() = "https://api.betterttv.net/3/cached/users/twitch/$this"

    private suspend fun getEmotesUrl(updateOption: EmoteUpdateOption): String? {
        return when (updateOption) {
            is EmoteUpdateOption.Global -> "https://api.betterttv.net/3/cached/emotes/global"
            is EmoteUpdateOption.Channel -> twitchDao.getUserByName(updateOption.name)?.id?.channelEmotesUrl()
        }
    }

    override fun update(updateOption: EmoteUpdateOption) {
        launch {
            val url = getEmotesUrl(updateOption) ?: run {
                Log.e(TAG, "Emotes url not found for $updateOption")
                return@launch
            }
            val emotes = try {
                val response = httpClient.get(Url(url))
                val emotes = when (updateOption) {
                    is EmoteUpdateOption.Global -> {
                        response.body<List<BttvEmote>>()
                    }
                    is EmoteUpdateOption.Channel -> {
                        response.body<BttvChannelEmotesResponse>()
                            .let { it.channelEmotes + it.sharedEmotes }
                    }
                }
                emotes.map { it.toEmote(updateOption) }
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            emoteDao.updateEmotes(Emote.Provider.BetterTtv, emotes, updateOption)
            Log.d(TAG, "${emotes.size} emotes loaded for $updateOption")
        }
    }
}
