package com.github.trueddd.twitch.dto.bttv

import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.emotes.EmoteUpdateOption
import kotlinx.serialization.Serializable

@Serializable
data class BttvEmote(
    val id: String,
    val code: String,
    val imageType: String,
    val animated: Boolean,
    val userId: String?,
    val modifier: Boolean?,
    val width: Int?,
    val height: Int?,
) {

    companion object {
        private const val EmoteUrlPlaceholder = "https://cdn.betterttv.net/emote"
        private const val BaseSize = 28
    }

    fun toEmote(updateOption: EmoteUpdateOption) = Emote(
        id = id,
        name = code,
        provider = Emote.Provider.BetterTtv,
        global = updateOption is EmoteUpdateOption.Global,
        versions = listOf(1, 2, 4).mapIndexed { index, multiplier ->
            Emote.Version(
                width = multiplier * (width ?: BaseSize),
                height = multiplier * (height ?: BaseSize),
                url = "$EmoteUrlPlaceholder/$id/${index}x.$imageType",
            )
        },
    )
}
