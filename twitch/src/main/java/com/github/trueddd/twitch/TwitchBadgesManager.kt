package com.github.trueddd.twitch

interface TwitchBadgesManager {

    fun updateBadges()

    suspend fun getBadgeUrl(badgeName: String, badgeTier: String?): String?
}
