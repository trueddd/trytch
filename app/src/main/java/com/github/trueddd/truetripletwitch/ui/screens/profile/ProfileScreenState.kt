package com.github.trueddd.truetripletwitch.ui.screens.profile

import com.github.trueddd.twitch.data.User

data class ProfileScreenState(
    val user: User,
) {

    companion object {

        fun default(user: User) = ProfileScreenState(
            user = user,
        )

        fun test() = ProfileScreenState(
            user = User.test().copy(current = true)
        )
    }
}
