package com.github.trueddd.trytch.ui.screens.main

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
            user = User.test(),
            userLoading = false,
            streams = persistentListOf(
                Stream.test(),
                Stream.test(),
                Stream.test(),
            ),
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
