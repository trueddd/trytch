package com.github.trueddd.twitch

import com.github.trueddd.twitch.data.Stream
import kotlinx.coroutines.flow.Flow

interface TwitchStreamsManager {

    val followedStreamsFlow: Flow<List<Stream>>

    fun getStreamFlow(channel: String): Flow<Stream?>

    fun updateFollowedStreams(): Flow<Result<Unit>>

    fun getStreamVideoInfo(channel: String): Flow<Map<String, String>>

    suspend fun clearStreams()
}
