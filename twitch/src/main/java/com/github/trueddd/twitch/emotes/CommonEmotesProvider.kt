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

    private val providers = mapOf<Emote.Provider, EmoteStorage>(
        Emote.Provider.Twitch to TwitchEmotesProvider(httpClient, twitchDao, emoteDao),
        Emote.Provider.SevenTv to SevenTvEmotesProvider(httpClient, twitchDao, emoteDao),
        Emote.Provider.FrankerFacez to FrankerFaceZEmotesProvider(httpClient, twitchDao, emoteDao),
        Emote.Provider.BetterTtv to BetterTTVEmotesProvider(httpClient, twitchDao, emoteDao),
    )

    override fun update(updateOption: EmoteUpdateOption) {
        providers.forEach { (_, storage) ->
            storage.update(updateOption)
        }
    }

    override suspend fun getEmote(word: String, providers: List<Emote.Provider>): Emote? {
        return emoteDao.getEmoteByName(word, providers)
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

    override fun updateEmoteSets(emoteSetIds: List<String>) {
        providers[Emote.Provider.Twitch]?.updateEmoteSets(emoteSetIds)
    }
}
