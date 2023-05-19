package com.github.trueddd.truetripletwitch.ui.screens.profile

import androidx.lifecycle.viewModelScope
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.github.trueddd.truetripletwitch.navigation.Routing
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchUserManager
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileViewModel(
    private val twitchUserManager: TwitchUserManager,
    private val backStack: BackStack<Routing>,
) : StatefulViewModel<ProfileScreenState>() {

    override fun initialState() = ProfileScreenState.default(twitchUserManager.userFlow.value!!)

    fun initProfileScreen() {
        twitchUserManager.userFlow
            .filter { it == null }
            .onEach { backStack.pop() }
            .launchIn(viewModelScope)
    }
}
