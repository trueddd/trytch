package com.github.trueddd.trytch.ui.screens.stream.page

import com.github.trueddd.trytch.ui.StatefulViewModel
import com.github.trueddd.twitch.data.User

class StreamerPageViewModel(
    private val user: User,
) : StatefulViewModel<StreamerPageState>() {

    override fun initialState() = StreamerPageState.default(user)
}
