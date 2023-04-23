package com.github.trueddd.twitch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.data.EmoteInfo
import com.github.trueddd.twitch.data.EmoteVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
internal interface EmoteDao {

    @Query("select * from emote_info join emote_versions on (emote_info.id = emote_versions.id and emote_info.provider = emote_versions.provider) where name = :name")
    suspend fun getEmoteByName(name: String): Map<EmoteInfo, List<EmoteVersion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoteInfo(emotesInfo: List<EmoteInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoteVersions(emoteVersions: List<EmoteVersion>)

    @Transaction
    suspend fun insertEmotes(emotes: List<Emote>) {
        withContext(Dispatchers.Default) {
            val info = emotes.map { EmoteInfo(it.id, it.name, it.provider) }
            val versions = emotes.flatMap { emote ->
                emote.versions.map {
                    EmoteVersion(emote.id, emote.provider, it.width, it.height, it.url)
                }
            }
            withContext(Dispatchers.IO) {
                insertEmoteInfo(info)
                insertEmoteVersions(versions)
            }
        }
    }
}
