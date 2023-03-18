package com.github.trueddd.twitch

import com.github.trueddd.twitch.db.BadgeDao
import com.github.trueddd.twitch.dto.TwitchBadgeSet
import com.github.trueddd.twitch.dto.TwitchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*

class TwitchBadgesManagerImpl(
    private val httpClient: HttpClient,
    private val badgeDao: BadgeDao,
) : TwitchBadgesManager, CoroutineScope {

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.Default
    }

    override fun updateBadges() {
        launch(Dispatchers.IO) {
            val badgesData = try {
                httpClient.get(Url("https://api.twitch.tv/helix/chat/badges/global"))
                    .body<TwitchResponse<List<TwitchBadgeSet>>>().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            val badges = badgesData.flatMap { it.toBadgeVersions() }
            badgeDao.updateGlobalBadges(badges)
        }
    }

    override suspend fun getBadgeUrl(badgeName: String, badgeTier: String?): String? {
        return withContext(Dispatchers.IO) {
            val badgeVersion = badgeTier ?: "0"
            badgeDao.getBadgeVersionsForSetId(badgeName)
                .firstOrNull { it.id == badgeVersion }
                ?.imageUrl2x
        }
    }
}
