package com.github.trueddd.twitch.db

import androidx.room.TypeConverter
import com.github.trueddd.twitch.data.Emote

class TwitchStreamTagsConverter {

    @TypeConverter
    fun fromString(encoded: String): List<String> {
        return encoded.split(" ")
    }

    @TypeConverter
    fun toString(decoded: List<String>): String {
        return decoded.joinToString(" ")
    }
}

class EmoteProviderConverter {

    @TypeConverter
    fun fromString(encoded: String): Emote.Provider {
        return when (encoded) {
            "twitch" -> Emote.Provider.Twitch
            "7tv" -> Emote.Provider.SevenTv
            "ffz" -> Emote.Provider.FrankerFacez
            "bttv" -> Emote.Provider.BetterTtv
            else -> throw Exception("Unexpected emote provider - $encoded")
        }
    }

    @TypeConverter
    fun toString(decoded: Emote.Provider): String {
        return decoded.value
    }
}
