package com.github.trueddd.twitch.dto.ffz

import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.emotes.EmoteUpdateOption
import kotlinx.serialization.Serializable

@Serializable
data class FfzEmoticon(
    val id: Int,
    val name: String,
    val height: Int,
    val width: Int,
    val urls: Map<String, String>,
    val public: Boolean,
    val hidden: Boolean,
    val modifier: Boolean,
) {

    fun toEmote(updateOption: EmoteUpdateOption) = Emote(
        id = id.toString(),
        name = name,
        provider = Emote.Provider.FrankerFacez,
        global = updateOption is EmoteUpdateOption.Global,
        versions = urls
            .mapKeys { (key, _) -> key.toInt() }
            .map { (sizeMultiplier, url) ->
                Emote.Version(width * sizeMultiplier, height * sizeMultiplier, url)
            },
    )
}
