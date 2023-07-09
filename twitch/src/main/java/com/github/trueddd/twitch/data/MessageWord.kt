package com.github.trueddd.twitch.data

import com.github.trueddd.twitch.dto.twitch.TwitchEmoteFormat
import com.github.trueddd.twitch.dto.twitch.TwitchEmoteScale
import com.github.trueddd.twitch.dto.twitch.TwitchEmoteThemeMode

sealed class MessageWord(open val content: String) {
    data class Default(override val content: String) : MessageWord(content)
    data class Mention(override val content: String) : MessageWord(content)
    data class Link(override val content: String) : MessageWord(content)
    data class Emote(
        val emote: com.github.trueddd.twitch.data.Emote,
        override val content: String,
    ) : MessageWord(content)

    data class UnknownTwitchEmote(
        val twitchId: String,
        override val content: String,
    ) : MessageWord(content) {

        private val templateUrl = "https://static-cdn.jtvnw.net/emoticons/v2/$twitchId/{{format}}/{{theme_mode}}/{{scale}}"

        fun url(
            format: TwitchEmoteFormat = TwitchEmoteFormat.Static,
            themeMode: TwitchEmoteThemeMode = TwitchEmoteThemeMode.Dark,
            scale: TwitchEmoteScale = TwitchEmoteScale.Large,
        ): String {
            return templateUrl
                .replace("{{format}}", format.formatValue)
                .replace("{{theme_mode}}", themeMode.modeValue)
                .replace("{{scale}}", scale.stringValue)
        }
    }
}
