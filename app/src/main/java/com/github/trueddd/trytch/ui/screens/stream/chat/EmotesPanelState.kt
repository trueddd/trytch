package com.github.trueddd.trytch.ui.screens.stream.chat

import com.github.trueddd.twitch.data.Emote
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class EmotesPanelState(
    val panelOpen: Boolean,
    val selectedProvider: Emote.Provider,
    val searchEnabled: Boolean,
    val searchText: String,
    val emotes: ImmutableList<Emote>,
) {

    companion object {

        fun default() = EmotesPanelState(
            panelOpen = false,
            selectedProvider = Emote.Provider.Twitch,
            searchEnabled = false,
            searchText = "",
            emotes = persistentListOf(),
        )

        fun test() = EmotesPanelState(
            panelOpen = false,
            selectedProvider = Emote.Provider.Twitch,
            searchEnabled = false,
            searchText = "",
            emotes = List(100) { Emote.test() }.toImmutableList(),
        )
    }
}
