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

    private val providers = listOf<EmoteStorage>(
        SevenTvEmotesProvider(httpClient, twitchDao, emoteDao),
        FrankerFaceZEmotesProvider(httpClient, twitchDao, emoteDao),
        BetterTTVEmotesProvider(httpClient, twitchDao, emoteDao),
    )

    override fun update(updateOption: EmoteUpdateOption) {
        providers.forEach {
            it.update(updateOption)
        }
    }

    override suspend fun getEmote(word: String): Emote? {
        return emoteDao.getEmoteByName(word)
            .entries
            .minByOrNull { it.key.sortingOrder }
            ?.let { (info, versions) ->
                Emote(
                    id = info.id,
                    name = info.name,
                    provider = info.provider,
                    global = info.global,
                    versions = versions.map { Emote.Version(it.width, it.height, it.url) },
                )
            }
    }
}
