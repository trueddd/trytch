package com.github.trueddd.twitch

import com.github.trueddd.twitch.dto.twitch.TwitchClip
import com.github.trueddd.twitch.dto.twitch.TwitchVideo
import kotlinx.collections.immutable.ImmutableList

interface TwitchClipsManager {

    suspend fun loadClips(channelId: String): ImmutableList<TwitchClip>

    suspend fun loadVideos(channelId: String): ImmutableList<TwitchVideo>
}
