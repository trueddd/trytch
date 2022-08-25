package com.github.trueddd.twitch.db

import androidx.room.TypeConverter

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
