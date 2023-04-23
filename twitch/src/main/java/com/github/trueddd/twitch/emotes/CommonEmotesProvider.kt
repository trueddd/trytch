package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.db.EmoteDao
import com.github.trueddd.twitch.db.TwitchDao
import io.ktor.client.HttpClient

internal class CommonEmotesProvider(
    httpClient: HttpClient,
    twitchDao: TwitchDao,
    private val emoteDao: EmoteDao,
) : EmotesProvider {

    private val providers = listOf(
        SevenTvEmotesProvider(httpClient, twitchDao, emoteDao),
    )

    override fun update(updateOption: EmoteUpdateOption) {
        providers.forEach {
            it.update(updateOption)
        }
    }

    override suspend fun getEmote(word: String): Emote? {
        return emoteDao.getEmoteByName(word)
            .entries
            .firstOrNull()
            ?.let { (info, versions) ->
                Emote(
                    info.id,
                    info.name,
                    info.provider,
                    versions.map { Emote.Version(it.width, it.height, it.url) },
                )
            }
    }
}
