package com.github.trueddd.twitch

interface TwitchBadgesManager {

    fun updateBadges()

    fun updateChannelBadges(channel: String)

    suspend fun getBadgeUrl(channel: String, badgeName: String, badgeTier: String?): String?
}
