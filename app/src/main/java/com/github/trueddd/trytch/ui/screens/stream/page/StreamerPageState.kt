package com.github.trueddd.trytch.ui.screens.stream.page

import com.github.trueddd.twitch.data.User

data class StreamerPageState(
    val streamerUser: User,
) {

    companion object {

        fun test() = StreamerPageState(
            streamerUser = User.test(),
        )

        fun default(user: User) = StreamerPageState(
            streamerUser = user,
        )
    }
}
