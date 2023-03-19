package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.BadgeVersion
import com.google.gson.annotations.SerializedName

data class TwitchBadgeSet(
    @SerializedName("set_id")
    val setId: String,
    @SerializedName("versions")
    val versions: List<TwitchBadgeVersion>,
) {

    fun toBadgeVersions(channel: String? = null) = versions.map {
        BadgeVersion(setId, it.id, channel ?: "", it.imageUrl1x, it.imageUrl2x, it.imageUrl4x)
    }
}
