package com.github.trueddd.twitch.emotes

import android.util.Log

interface EmoteStorage {

    fun update(updateOption: EmoteUpdateOption)

    fun updateEmoteSets(emoteSetIds: List<String>) {
        Log.d("EmoteStorage", "Updating emote sets is not implemented for this storage")
    }
}
