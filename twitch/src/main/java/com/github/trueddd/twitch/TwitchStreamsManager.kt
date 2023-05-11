package com.github.trueddd.twitch

import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.StreamInfo
import kotlinx.coroutines.flow.Flow

interface TwitchStreamsManager {

    val followedStreamsFlow: Flow<List<Stream>>

    fun getStreamFlow(channel: String): Flow<Stream?>

    fun updateFollowedStreams(): Flow<Result<Unit>>

    fun getStreamVideoInfo(channel: String): Flow<List<StreamInfo>>

    suspend fun clearStreams()
}
