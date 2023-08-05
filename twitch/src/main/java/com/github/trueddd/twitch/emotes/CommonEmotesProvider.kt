package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.data.EmoteInfo
import com.github.trueddd.twitch.data.EmoteVersion
import com.github.trueddd.twitch.db.EmoteDao
import com.github.trueddd.twitch.db.TwitchDao
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

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

    private fun Map.Entry<EmoteInfo, List<EmoteVersion>>.toEmote() = Emote(
        id = key.id,
        name = key.name,
        provider = key.provider,
        global = key.global,
        versions = value.map { Emote.Version(it.width, it.height, it.url) },
    )

    override suspend fun getEmote(word: String, providers: List<Emote.Provider>): Emote? {
        return emoteDao.getEmoteByName(word, providers)
            .entries
            .minByOrNull { it.key.sortingOrder }
            ?.toEmote()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEmotesFlow(provider: Emote.Provider): Flow<ImmutableList<Emote>> {
        return emoteDao.getEmotesByProvider(provider)
            .flowOn(Dispatchers.IO)
            .mapLatest { emotes -> emotes.map { it.toEmote() }.toImmutableList() }
            .flowOn(Dispatchers.Default)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEmotesByNameFlow(query: String): Flow<ImmutableList<Emote>> {
        return emoteDao.getEmotesByQuery(query)
            .flowOn(Dispatchers.IO)
            .mapLatest { emotes -> emotes.map { it.toEmote() }.toImmutableList() }
            .flowOn(Dispatchers.Default)
    }

    override fun updateEmoteSets(emoteSetIds: List<String>) {
        providers[Emote.Provider.Twitch]?.updateEmoteSets(emoteSetIds)
    }
}
