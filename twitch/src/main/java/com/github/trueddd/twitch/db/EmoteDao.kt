package com.github.trueddd.twitch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.data.EmoteInfo
import com.github.trueddd.twitch.data.EmoteVersion
import com.github.trueddd.twitch.emotes.EmoteUpdateOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Dao
internal interface EmoteDao {

    @Query("select * from emote_info join emote_versions on (emote_info.id = emote_versions.id and emote_info.provider = emote_versions.provider) where name = :name and emote_info.provider in (:providers)")
    suspend fun getEmoteByName(name: String, providers: List<Emote.Provider>): Map<EmoteInfo, List<EmoteVersion>>

    @Query("select * from emote_info join emote_versions on (emote_info.id = emote_versions.id and emote_info.provider = emote_versions.provider) where emote_info.provider = :provider")
    fun getEmotesByProvider(provider: Emote.Provider): Flow<Map<EmoteInfo, List<EmoteVersion>>>

    @Query("select * from emote_info join emote_versions on (emote_info.id = emote_versions.id and emote_info.provider = emote_versions.provider) where emote_info.name like ('%' || :query || '%')")
    fun getEmotesByQuery(query: String): Flow<Map<EmoteInfo, List<EmoteVersion>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoteInfo(emotesInfo: List<EmoteInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoteVersions(emoteVersions: List<EmoteVersion>)

    @Query("delete from emote_info where provider = :platform")
    suspend fun clearPlatformEmotes(platform: Emote.Provider)

    @Query("delete from emote_info where provider = :platform and global = 0")
    suspend fun clearChannelEmotes(platform: Emote.Provider)

    @Query("delete from emote_info where provider = 'twitch' and global = 0 and id in (:emoteIds)")
    suspend fun clearTwitchChannelEmotes(emoteIds: List<String>)

    @Transaction
    suspend fun updateEmotes(
        provider: Emote.Provider,
        emotes: List<Emote>,
        updateOption: EmoteUpdateOption,
    ) {
        withContext(Dispatchers.Default) {
            val info = emotes.map {
                EmoteInfo(
                    id = it.id,
                    name = it.name,
                    provider = it.provider,
                    global = when {
                        (updateOption as? EmoteUpdateOption.Channel)?.name == "" -> it.global
                        else -> updateOption is EmoteUpdateOption.Global
                    },
                )
            }
            val versions = emotes.flatMap { emote ->
                emote.versions.map {
                    EmoteVersion(emote.id, emote.provider, it.width, it.height, it.url)
                }
            }
            withContext(Dispatchers.IO) {
                when (updateOption) {
                    is EmoteUpdateOption.Global -> clearPlatformEmotes(provider)
                    is EmoteUpdateOption.Channel -> {
                        if (provider == Emote.Provider.Twitch) {
                            clearTwitchChannelEmotes(info.map { it.id })
                        } else {
                            clearChannelEmotes(provider)
                        }
                    }
                }
                insertEmoteInfo(info)
                insertEmoteVersions(versions)
            }
        }
    }
}
