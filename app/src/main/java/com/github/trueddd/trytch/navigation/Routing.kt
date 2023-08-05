package com.github.trueddd.trytch.navigation

import android.os.Parcelable
import com.github.trueddd.twitch.data.User
import kotlinx.parcelize.Parcelize

sealed class Routing(val name: String) : Parcelable {

    object Keys {
        const val Main = "main"
        const val Stream = "stream"
        const val EmotesPanel = "emotes_panel"
        const val Profile = "profile"
        const val StreamerPage = "streamer_page"
    }

    @Parcelize
    data object Main : Routing(Keys.Main)

    @Parcelize
    data class Stream(val channel: String) : Routing(Keys.Stream)

    @Parcelize
    data class StreamerPage(val user: User) : Routing(Keys.StreamerPage)

    @Parcelize
    data object Profile : Routing(Keys.Profile)
}
