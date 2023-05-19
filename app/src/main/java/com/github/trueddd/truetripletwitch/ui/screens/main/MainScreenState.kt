package com.github.trueddd.truetripletwitch.ui.screens.main

import com.github.trueddd.twitch.data.BroadcasterType
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MainScreenState(
    val user: User?,
    val userLoading: Boolean,
    val streams: ImmutableList<Stream>,
    val streamsLoading: Boolean,
) {

    companion object {

        fun test() = MainScreenState(
            user = User(
                id = "0",
                login = "truetripled",
                displayName = "truetripled",
                profileImageUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/c0fb8aca-3fc7-41f9-b336-1c39b5dc3afc-profile_image-300x300.png",
                broadcasterType = BroadcasterType.Affiliate,
                description = "Just a regular Twitch streamer.",
            ),
            userLoading = false,
            streams = persistentListOf(Stream.test()),
            streamsLoading = true,
        )

        fun default() = MainScreenState(
            user = null,
            userLoading = false,
            streams = persistentListOf(),
            streamsLoading = false,
        )
    }
}
