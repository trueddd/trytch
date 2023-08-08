package com.github.trueddd.twitch

import com.github.trueddd.twitch.dto.twitch.TwitchClip
import kotlinx.collections.immutable.ImmutableList

interface TwitchClipsManager {

    suspend fun loadClips(channelId: String): ImmutableList<TwitchClip>
}
