package com.github.trueddd.trytch.ui.screens.stream.chat

import androidx.lifecycle.viewModelScope
import com.github.trueddd.trytch.navigation.AppBackPressStrategy
import com.github.trueddd.trytch.ui.StatefulViewModel
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class EmotesPanelViewModel(
    private val emotesProvider: EmotesProvider,
    private val appBackPressStrategy: AppBackPressStrategy,
) : StatefulViewModel<EmotesPanelState>() {

    override fun initialState() = EmotesPanelState.default()

    private val backStackInterceptor: () -> Boolean = {
        when {
            stateFlow.value.searchEnabled -> {
                updateState { it.withSearchDisabled() }
                false
            }
            stateFlow.value.panelOpen -> {
                updateState { it.withPanelOpened(panelOpened = false) }
                false
            }
            else -> true
        }
    }

    init {
        combine(
            stateFlow.map { it.searchText }.debounce(200L),
            stateFlow.map { it.selectedProvider },
        ) { text, provider -> text to provider }
            .distinctUntilChanged()
            .flatMapLatest { (search, provider) ->
                if (search.isNotEmpty()) {
                    emotesProvider.getEmotesByNameFlow(search)
                } else {
                    emotesProvider.getEmotesFlow(provider)
                }
            }
            .onEach { emotes -> updateState { it.copy(emotes = emotes) } }
            .launchIn(viewModelScope)
        appBackPressStrategy.addInterceptor(backStackInterceptor)
    }

    fun togglePanel() {
        updateState {
            it.withPanelOpened(!it.panelOpen)
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
            it.copy(searchEnabled = !it.searchEnabled, searchText = "")
        }
    }

    override fun onCleared() {
        super.onCleared()
        appBackPressStrategy.removeInterceptor(backStackInterceptor)
    }

    private fun EmotesPanelState.withSearchDisabled() = copy(searchEnabled = false, searchText = "")
    private fun EmotesPanelState.withPanelOpened(panelOpened: Boolean) = withSearchDisabled().copy(panelOpen = panelOpened)
}
