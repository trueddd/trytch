package com.github.trueddd.trytch.ui.screens.stream.chat

import androidx.lifecycle.viewModelScope
import com.github.trueddd.trytch.ui.StatefulViewModel
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
class EmotesPanelViewModel(
    private val emotesProvider: EmotesProvider,
) : StatefulViewModel<EmotesPanelState>() {

    override fun initialState() = EmotesPanelState.default()

    init {
        stateFlow.map { it.selectedProvider }
            .distinctUntilChanged()
            .flatMapLatest { emotesProvider.getEmotesFlow(it) }
            .onEach { emotes -> updateState { it.copy(emotes = emotes) } }
            .launchIn(viewModelScope)
    }

    fun togglePanel() {
        updateState {
            it.copy(panelOpen = !it.panelOpen)
        }
    }

    fun changeTab(provider: Emote.Provider) {
        updateState {
            it.copy(selectedProvider = provider)
        }
    }

    fun updateSearch(searchText: String) {
        updateState {
            it.copy(searchText = searchText)
        }
    }

    fun toggleSearch() {
        updateState {
            it.copy(searchEnabled = !it.searchEnabled)
        }
    }
}
