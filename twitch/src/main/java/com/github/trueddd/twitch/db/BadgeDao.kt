package com.github.trueddd.twitch.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.trueddd.twitch.data.BadgeVersion

@Dao
interface BadgeDao {

    @Upsert
    suspend fun upsertBadgeVersions(badgeVersions: List<BadgeVersion>)

    @Transaction
    suspend fun updateGlobalBadges(badgeVersions: List<BadgeVersion>) {
        clearBadges()
        upsertBadgeVersions(badgeVersions)
    }

    @Query("delete from badge_versions")
    suspend fun clearBadges()

    @Query("select * from badge_versions where badgeId = :setId")
    suspend fun getBadgeVersionsForSetId(setId: String): List<BadgeVersion>
}
