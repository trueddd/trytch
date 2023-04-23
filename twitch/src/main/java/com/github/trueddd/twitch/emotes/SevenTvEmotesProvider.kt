package com.github.trueddd.twitch.emotes

import android.util.Log
import com.github.trueddd.twitch.db.EmoteDao
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.SevenTvEmote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class SevenTvEmotesProvider(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
    private val emoteDao: EmoteDao,
) : EmoteStorage, CoroutineScope {

    companion object {
        private const val TAG = "SevenTvEmotesProvider"
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private fun String.sevenTvEmotesUrl() = "https://api.7tv.app/v2/users/$this/emotes"

    private suspend fun getEmotesUrl(updateOption: EmoteUpdateOption): String? {
        return when (updateOption) {
            is EmoteUpdateOption.Global -> "https://api.7tv.app/v2/emotes/global"
            is EmoteUpdateOption.Channel -> twitchDao.getUserByName(updateOption.name)?.id?.sevenTvEmotesUrl()
        }
    }

    override fun update(updateOption: EmoteUpdateOption) {
        launch {
            val url = getEmotesUrl(updateOption) ?: run {
                Log.e(TAG, "Emotes url not found for $updateOption")
                return@launch
            }
            val emotes = try {
                httpClient.get(Url(url))
                    .body<List<SevenTvEmote>>()
                    .map { it.toEmote() }
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            emoteDao.insertEmotes(emotes)
            Log.d(TAG, "${emotes.size} emotes loaded for $updateOption")
        }
    }
}
