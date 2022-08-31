package com.github.trueddd.truetripletwitch.ui.screens.main

import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.User

data class MainScreenState(
    val user: User?,
    val userLoading: Boolean,
    val streams: List<Stream>,
) {

    companion object {

        fun test() = MainScreenState(
            User(
                id = "0",
                login = "truetripled",
                displayName = "truetripled",
                email = "qwe123@gmail.com",
                profileImageUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/c0fb8aca-3fc7-41f9-b336-1c39b5dc3afc-profile_image-300x300.png"
            ),
            false,
            listOf(Stream.test()),
        )

        fun default() = MainScreenState(
            user = null,
            userLoading = false,
            streams = emptyList(),
        )
    }
}
