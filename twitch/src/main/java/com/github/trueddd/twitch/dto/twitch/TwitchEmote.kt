package com.github.trueddd.twitch.dto.twitch

import com.github.trueddd.twitch.data.Emote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class TwitchEmote {

    abstract fun toEmote(templateUrl: String): Emote

    protected fun createEmote(
        id: String,
        name: String,
        global: Boolean,
        scale: List<TwitchEmoteScale>,
        format: List<TwitchEmoteFormat>,
        themeMode: List<TwitchEmoteThemeMode>,
        templateUrl: String,
    ) = Emote(
        id = id,
        name = name,
        provider = Emote.Provider.Twitch,
        global = global,
        versions = scale.map {
            Emote.Version(
                height = it.size,
                width = it.size,
                url = templateUrl
                    .replace("{{id}}", id)
                    .replace("{{format}}", if (format.contains(TwitchEmoteFormat.Animated)) "animated" else "static")
                    .replace("{{theme_mode}}", if (themeMode.contains(TwitchEmoteThemeMode.Dark)) "dark" else "light")
                    .replace("{{scale}}", it.stringValue),
            )
        }
    )

    @Serializable
    data class Global(
        val id: String,
        val name: String,
        val images: TwitchEmoteImages,
        val format: List<TwitchEmoteFormat>,
        val scale: List<TwitchEmoteScale>,
        @SerialName("theme_mode")
        val themeMode: List<TwitchEmoteThemeMode>,
    ) : TwitchEmote() {

        override fun toEmote(templateUrl: String) = createEmote(
            id = id,
            name = name, global = true,
            scale = scale,
            format = format,
            themeMode = themeMode,
            templateUrl = templateUrl,
        )
    }

    @Serializable
    data class Channel(
        val id: String,
        val name: String,
        val images: TwitchEmoteImages,
        val tier: String?,
        @SerialName("emote_type")
        val emoteType: TwitchEmoteType,
        @SerialName("emote_set_id")
        val emoteSetId: String,
        @SerialName("owner_id")
        val ownerId: String?,
        val format: List<TwitchEmoteFormat>,
        val scale: List<TwitchEmoteScale>,
        @SerialName("theme_mode")
        val themeMode: List<TwitchEmoteThemeMode>,
    ) : TwitchEmote() {

        override fun toEmote(templateUrl: String) = createEmote(
            id = id,
            name = name,
            global = ownerId == "0",
            scale = scale,
            format = format,
            themeMode = themeMode,
            templateUrl = templateUrl,
        )
    }
}
