package com.github.trueddd.twitch

import android.util.Log
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.StreamInfo
import com.github.trueddd.twitch.data.UserRequestType
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.PaginatedTwitchResponse
import com.github.trueddd.twitch.dto.TwitchStream
import com.github.trueddd.twitch.dto.TwitchStreamVideoInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class TwitchStreamsManagerImpl(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
) : TwitchStreamsManager {

    companion object {
        private const val TAG = "TwitchStreamsManager"
    }

    private val twitchApiWrapper by lazy {
        TwitchApiWrapper(httpClient)
    }

    override val followedStreamsFlow: Flow<List<Stream>>
        get() = twitchDao.getStreamsFlow().flowOn(Dispatchers.IO)

    override fun getStreamFlow(channel: String): Flow<Stream?> {
        return twitchDao.getStreamFlowByChannel(channel).flowOn(Dispatchers.IO)
    }

    private suspend fun loadFollowedStreams(userId: String): List<TwitchStream>? {
        return try {
            httpClient.get(Url("https://api.twitch.tv/helix/streams/followed")) {
                parameter("user_id", userId)
            }.body<PaginatedTwitchResponse<List<TwitchStream>>>().data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun updateFollowedStreams(): Flow<Result<Unit>> {
        Log.d(TAG, "Started streams loading")
        return flow {
            val userId = twitchDao.getUser()?.id ?: run {
                emit(Result.failure(IllegalStateException("User is null")))
                return@flow
            }
            val streams = loadFollowedStreams(userId)?.map { it.toStream(userId) }
            if (streams == null) {
                emit(Result.failure(IllegalStateException("Followed streams loading error")))
            } else {
                twitchDao.upsertStreams(streams)
                emit(Result.success(Unit))
                val userIds = streams.map { it.userId }
                val users = twitchApiWrapper.getTwitchUsers(UserRequestType.Id(userIds))
                    ?.map { it.toUser() }
                    ?: return@flow
                Log.d(TAG, "Loaded ${users.size} users from followed streams")
                twitchDao.insertUsers(users)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getStreamVideoInfo(channel: String) = flow {
        val stream = twitchDao.getStreamByUserName(channel) ?: run {
            Log.d(TAG, "No stream found for $channel")
            emit(emptyList())
            return@flow
        }
        try {
            httpClient.get(Url("https://pwn.sh/tools/streamapi.py")) {
                parameter("url", "twitch.tv/${stream.userName}")
            }.body<TwitchStreamVideoInfo>().urls
                .filterKeys { it != "audio_only" }
                .map { StreamInfo(it.key, it.value) }
                .sortedByDescending { it.resolution }
                .let { emit(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun clearStreams() {
        withContext(Dispatchers.IO) {
            twitchDao.deleteStreams()
        }
    }
}
