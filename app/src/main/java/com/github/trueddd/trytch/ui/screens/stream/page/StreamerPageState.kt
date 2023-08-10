package com.github.trueddd.trytch.ui.screens.stream.page

import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.dto.twitch.TwitchClip
import com.github.trueddd.twitch.dto.twitch.TwitchVideo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StreamerPageState(
    val streamerUser: User,
    val clipsState: ClipsState,
    val videosState: VideosState,
) {

    data class ClipsState(
        val isLoading: Boolean,
        val clips: ImmutableList<TwitchClip>,
    ) {
        companion object {
            fun test() = ClipsState(
                isLoading = false,
                clips = persistentListOf(
                    TwitchClip.test(),
                    TwitchClip.test(),
                    TwitchClip.test()
                )
            )
            fun default() = ClipsState(isLoading = false, clips = persistentListOf())
        }
    }

    data class VideosState(
        val isLoading: Boolean,
        val videos: ImmutableList<TwitchVideo>,
    ) {
        companion object {
            fun test() = VideosState(
                isLoading = false,
                videos = persistentListOf(
                    TwitchVideo.test(),
                    TwitchVideo.test(),
                    TwitchVideo.test()
                )
            )
            fun default() = VideosState(isLoading = false, videos = persistentListOf())
        }
    }

    companion object {

        fun test() = StreamerPageState(
            streamerUser = User.test(),
            clipsState = ClipsState.test(),
            videosState = VideosState(isLoading = false, videos = persistentListOf()),
        )

        fun default(user: User) = StreamerPageState(
            streamerUser = user,
            clipsState = ClipsState.default(),
            videosState = VideosState(isLoading = false, videos = persistentListOf()),
        )
    }
}
