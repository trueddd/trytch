package com.github.trueddd.trytch.ui.screens.stream.page

import androidx.lifecycle.viewModelScope
import com.github.trueddd.trytch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchClipsManager
import com.github.trueddd.twitch.data.User
import kotlinx.coroutines.launch

class StreamerPageViewModel(
    private val user: User,
    private val twitchClipsManager: TwitchClipsManager,
) : StatefulViewModel<StreamerPageState>() {

    override fun initialState() = StreamerPageState.default(user)

    fun updateClips(options: Clips.LoadOptions) {
        if (stateFlow.value.clipsState.isLoading) {
            return
        }
        viewModelScope.launch {
            updateState { state -> state.copy(clipsState = state.clipsState.copy(isLoading = true)) }
            val clips = twitchClipsManager.loadClips(options.user.id)
            updateState { state -> state.copy(clipsState = state.clipsState.copy(isLoading = true, clips = clips)) }
        }
    }
}
