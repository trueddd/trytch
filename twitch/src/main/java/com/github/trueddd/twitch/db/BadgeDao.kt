package com.github.trueddd.twitch.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.trueddd.twitch.data.BadgeVersion

@Dao
internal interface BadgeDao {

    @Upsert
    suspend fun upsertBadgeVersions(badgeVersions: List<BadgeVersion>)

    @Transaction
    suspend fun updateGlobalBadges(badgeVersions: List<BadgeVersion>) {
        clearGlobalBadges()
        upsertBadgeVersions(badgeVersions)
    }

    @Query("delete from badge_versions where channel = null")
    suspend fun clearGlobalBadges()

    @Query("select * from badge_versions where badgeId = :setId and (channel = :channel or channel = '')")
    suspend fun getBadgeVersionsForSetId(channel: String, setId: String): List<BadgeVersion>
}
