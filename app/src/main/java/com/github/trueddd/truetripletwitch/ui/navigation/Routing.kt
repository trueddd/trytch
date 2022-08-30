package com.github.trueddd.truetripletwitch.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Routing : Parcelable {

    @Parcelize
    object Main : Routing()

    @Parcelize
    class Stream(val streamId: String) : Routing()
}
