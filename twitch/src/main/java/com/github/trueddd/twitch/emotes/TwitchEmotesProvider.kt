package com.github.trueddd.twitch.emotes

import android.util.Log
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.db.EmoteDao
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.twitch.TwitchEmote
import com.github.trueddd.twitch.dto.twitch.TwitchEmotesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class TwitchEmotesProvider(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
    private val emoteDao: EmoteDao,
) : EmoteStorage, CoroutineScope {

    companion object {
        private const val TAG = "TwitchEmotesProvider"
        private const val MaxEmoteSetsSize = 25
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private fun getEmotesUrl(updateOption: EmoteUpdateOption): String {
        return when (updateOption) {
            is EmoteUpdateOption.Global -> "https://api.twitch.tv/helix/chat/emotes/global"
            is EmoteUpdateOption.Channel -> "https://api.twitch.tv/helix/chat/emotes"
        }
    }

    override fun update(updateOption: EmoteUpdateOption) {
        launch {
            val url = getEmotesUrl(updateOption)
            val emotes = try {
                val response = httpClient.get(Url(url)) {
                    if (updateOption is EmoteUpdateOption.Channel) {
                        parameter("broadcaster_id", twitchDao.getUserByName(updateOption.name)?.id)
                    }
                }
                when (updateOption) {
                    is EmoteUpdateOption.Channel -> response.body<TwitchEmotesResponse<TwitchEmote.Channel>>()
                    is EmoteUpdateOption.Global -> response.body<TwitchEmotesResponse<TwitchEmote.Global>>()
                }.toEmotes()
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            emoteDao.updateEmotes(Emote.Provider.Twitch, emotes, updateOption)
            Log.d(TAG, "${emotes.size} emotes loaded for $updateOption")
        }
    }

    override fun updateEmoteSets(emoteSetIds: List<String>) {
        launch {
            val emotes = emoteSetIds.chunked(MaxEmoteSetsSize).mapNotNull { emoteSets ->
                try {
                    httpClient.get(Url("https://api.twitch.tv/helix/chat/emotes/set")) {
                        emoteSets.forEach {
                            parameter("emote_set_id", it)
                        }
                    }.body<TwitchEmotesResponse<TwitchEmote.Channel>>().toEmotes()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }.flatten()
            emoteDao.updateEmotes(Emote.Provider.Twitch, emotes, EmoteUpdateOption.Channel(""))
            Log.d(TAG, "${emotes.size} emotes loaded from emote sets ($emoteSetIds)")
        }
    }
}
