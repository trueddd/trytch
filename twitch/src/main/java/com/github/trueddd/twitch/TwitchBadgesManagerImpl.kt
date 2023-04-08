package com.github.trueddd.twitch

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.github.trueddd.twitch.db.BadgeDao
import com.github.trueddd.twitch.db.TwitchDao
import com.github.trueddd.twitch.dto.TwitchBadgeSet
import com.github.trueddd.twitch.dto.TwitchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.*
import kotlin.time.Duration.Companion.days

internal class TwitchBadgesManagerImpl(
    private val httpClient: HttpClient,
    private val twitchDao: TwitchDao,
    private val badgeDao: BadgeDao,
    private val twitchDataStore: DataStore<Preferences>,
) : TwitchBadgesManager, CoroutineScope {

    private companion object {
        const val TAG = "TwitchBadgesManager"
        val GlobalBadgesUpdateInterval = 3.days.inWholeMilliseconds
        val ChannelBadgesUpdateInterval = 1.days.inWholeMilliseconds
        val GlobalBadgesLastUpdatedKey = longPreferencesKey("global_badges_last_updated")
    }

    private val String.channelBadgesLastUpdatedKey: Preferences.Key<Long>
        get() = longPreferencesKey("${this}_badges_last_updated")

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private suspend fun badgesNeedUpdate(channel: String?): Boolean {
        val key = channel?.channelBadgesLastUpdatedKey ?: GlobalBadgesLastUpdatedKey
        val lastUpdated = twitchDataStore.data.first()[key]
        Log.d(TAG, "Badges of $channel was updated at ${lastUpdated?.let { Date(it) } ?: "never"}")
        val updateInterval = if (channel == null) GlobalBadgesUpdateInterval else ChannelBadgesUpdateInterval
        val currentDate = System.currentTimeMillis()
        return when {
            lastUpdated == null -> true
            lastUpdated + updateInterval > currentDate -> false
            else -> true
        }
    }

    private suspend fun saveBadgesUpdateDate(channel: String?) {
        val key = channel?.channelBadgesLastUpdatedKey ?: GlobalBadgesLastUpdatedKey
        twitchDataStore.edit { preferences ->
            val updatedAt = System.currentTimeMillis()
            preferences[key] = updatedAt
        }
    }

    override fun updateBadges() {
        launch {
            if (!badgesNeedUpdate(channel = null)) {
                return@launch
            }
            val badgesData = try {
                httpClient.get(Url("https://api.twitch.tv/helix/chat/badges/global"))
                    .body<TwitchResponse<List<TwitchBadgeSet>>>().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            val badges = badgesData.flatMap { it.toBadgeVersions() }
            badgeDao.updateGlobalBadges(badges)
            saveBadgesUpdateDate(channel = null)
        }
    }

    override fun updateChannelBadges(channel: String) {
        launch {
            Log.d(TAG, "Updating badges for $channel")
            if (!badgesNeedUpdate(channel)) {
                Log.d(TAG, "Updating badges for $channel is skipped")
                return@launch
            }
            val broadcasterId = twitchDao.getStreamByUserName(channel)?.userId ?: return@launch
            val badgesData = try {
                httpClient.get(Url("https://api.twitch.tv/helix/chat/badges")) {
                    parameter("broadcaster_id", broadcasterId)
                }.body<TwitchResponse<List<TwitchBadgeSet>>>().data
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            Log.d(TAG, "Loaded badges for $channel: $badgesData")
            val badges = badgesData.flatMap { it.toBadgeVersions(channel) }
            badgeDao.upsertBadgeVersions(badges)
            saveBadgesUpdateDate(channel)
        }
    }

    override suspend fun getBadgeUrl(channel: String, badgeName: String, badgeTier: String?): String? {
        return withContext(coroutineContext) {
            val badgeVersion = badgeTier ?: "0"
            badgeDao.getBadgeVersionsForSetId(channel, badgeName)
                .filter { it.id == badgeVersion }
                .maxByOrNull { it.channel.length }
                ?.imageUrl2x
        }
    }
}
