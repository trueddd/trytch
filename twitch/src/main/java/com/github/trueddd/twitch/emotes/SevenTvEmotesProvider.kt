package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.SevenTvEmote
import io.ktor.client.*
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
) : EmotesProvider, CoroutineScope {

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private suspend fun getEmotesUrl(channel: String?): String {
        return channel?.let { twitchDao.getUser(it) }
            ?.let { "https://api.7tv.app/v2/users/$it/emotes" }
            ?: "https://api.7tv.app/v2/emotes/global"
    }

    override fun update(channel: String?) {
        launch {
            val url = getEmotesUrl(channel)
            val emotes = try {
                httpClient.get(Url(url)).body<List<SevenTvEmote>>()
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }

        }
    }

    override suspend fun getEmote(word: String): Emote? {
        TODO("Not yet implemented")
    }
}