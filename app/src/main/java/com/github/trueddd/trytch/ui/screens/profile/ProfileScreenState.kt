package com.github.trueddd.trytch.ui.screens.profile

import com.github.trueddd.twitch.data.User

data class ProfileScreenState(
    val user: User,
    val isLoading: Boolean,
) {

    companion object {

        fun default(user: User) = ProfileScreenState(
            user = user,
            isLoading = false,
        )

        fun test() = ProfileScreenState(
            user = User.test().copy(current = true),
            isLoading = true,
        )
    }
}
