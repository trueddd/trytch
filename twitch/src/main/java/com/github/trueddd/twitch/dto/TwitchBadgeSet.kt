package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.BadgeVersion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchBadgeSet(
    @SerialName("set_id")
    val setId: String,
    @SerialName("versions")
    val versions: List<TwitchBadgeVersion>,
) {

    fun toBadgeVersions(channel: String? = null) = versions.map {
        BadgeVersion(setId, it.id, channel ?: "", it.imageUrl1x, it.imageUrl2x, it.imageUrl4x)
    }
}
