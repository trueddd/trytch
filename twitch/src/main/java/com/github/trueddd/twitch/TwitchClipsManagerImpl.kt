package com.github.trueddd.twitch

import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.twitch.PaginatedTwitchResponse
import com.github.trueddd.twitch.dto.twitch.TwitchClip
import com.github.trueddd.twitch.dto.twitch.TwitchVideo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Url
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class TwitchClipsManagerImpl(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
) : TwitchClipsManager {

    sealed class LoadOption {
        data class Id(val value: String) : LoadOption()
        data class Game(val gameId: String) : LoadOption()
        data class Broadcaster(val broadcasterId: String) : LoadOption()
    }

    // TODO: pagination support
    private suspend fun fetchClips(loadOption: LoadOption): List<TwitchClip>? {
        return try {
            httpClient.get(Url("https://api.twitch.tv/helix/clips")) {
                when (loadOption) {
                    is LoadOption.Id -> parameter("id", loadOption.value)
                    is LoadOption.Broadcaster -> parameter("broadcaster_id", loadOption.broadcasterId)
                    is LoadOption.Game -> parameter("game_id", loadOption.gameId)
                }
            }.body<PaginatedTwitchResponse<List<TwitchClip>>>().data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // TODO: pagination support
    private suspend fun fetchVideos(loadOption: LoadOption): List<TwitchVideo>? {
        return try {
            httpClient.get(Url("https://api.twitch.tv/helix/videos")) {
                when (loadOption) {
                    is LoadOption.Id -> parameter("id", loadOption.value)
                    is LoadOption.Broadcaster -> parameter("user_id", loadOption.broadcasterId)
                    is LoadOption.Game -> parameter("game_id", loadOption.gameId)
                }
            }.body<PaginatedTwitchResponse<List<TwitchVideo>>>().data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun loadClips(channelId: String): ImmutableList<TwitchClip> {
        return withContext(Dispatchers.IO) {
            fetchClips(LoadOption.Broadcaster(channelId))?.toImmutableList() ?: persistentListOf()
        }
    }

    override suspend fun loadVideos(channelId: String): ImmutableList<TwitchVideo> {
        return withContext(Dispatchers.IO) {
            fetchVideos(LoadOption.Broadcaster(channelId))?.toImmutableList() ?: persistentListOf()
        }
    }
}
