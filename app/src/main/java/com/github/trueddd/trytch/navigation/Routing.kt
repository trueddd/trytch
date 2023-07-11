package com.github.trueddd.trytch.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Routing(val name: String) : Parcelable {

    object Keys {
        const val Main = "main"
        const val Stream = "stream"
        const val EmotesPanel = "emotes_panel"
        const val Profile = "profile"
    }

    @Parcelize
    object Main : Routing(Keys.Main)

    @Parcelize
    class Stream(val channel: String) : Routing(Keys.Stream)

    @Parcelize
    object Profile : Routing(Keys.Profile)
}
