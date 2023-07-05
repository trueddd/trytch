package com.github.trueddd.twitch.emotes

import android.util.Log
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.db.EmoteDao
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.ffz.FfzChannelEmotesResponse
import com.github.trueddd.twitch.dto.ffz.FfzGlobalEmotesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class FrankerFaceZEmotesProvider(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
    private val emoteDao: EmoteDao,
) : EmoteStorage, CoroutineScope {

    companion object {
        private const val TAG = "FFZEmotesProvider"
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private fun String.channelEmotesUrl() = "https://api.frankerfacez.com/v1/room/id/$this"

    private suspend fun getEmotesUrl(updateOption: EmoteUpdateOption): String? {
        return when (updateOption) {
            is EmoteUpdateOption.Global -> "https://api.frankerfacez.com/v1/set/global"
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
                        response.body<FfzGlobalEmotesResponse>()
                            .sets.flatMap { it.value.emoticons }
                    }
                    is EmoteUpdateOption.Channel -> {
                        response.body<FfzChannelEmotesResponse>()
                            .let { it.sets[it.room.setId.toString()]?.emoticons }
                            ?: emptyList()
                    }
                }
                emotes.map { it.toEmote(updateOption) }
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            emoteDao.updateEmotes(Emote.Provider.FrankerFacez, emotes, updateOption)
            Log.d(TAG, "${emotes.size} emotes loaded for $updateOption")
        }
    }
}
