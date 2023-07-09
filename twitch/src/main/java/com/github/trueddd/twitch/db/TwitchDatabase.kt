package com.github.trueddd.twitch.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.trueddd.twitch.data.BadgeVersion
import com.github.trueddd.twitch.data.EmoteInfo
import com.github.trueddd.twitch.data.EmoteVersion
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.Tokens
import com.github.trueddd.twitch.data.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Tokens::class,
        Stream::class,
        BadgeVersion::class,
        EmoteInfo::class,
        EmoteVersion::class,
    ],
    version = 1,
    autoMigrations = [
    ],
)
@TypeConverters(
    TwitchStreamTagsConverter::class,
    EmoteProviderConverter::class,
)
abstract class TwitchDatabase : RoomDatabase() {

    internal abstract fun twitchDao(): TwitchDao
    internal abstract fun badgeDao(): BadgeDao
    internal abstract fun emoteDao(): EmoteDao

    companion object {

        @OptIn(DelicateCoroutinesApi::class)
        fun create(context: Context, twitchDataStore: DataStore<Preferences>): TwitchDatabase {
            return Room.databaseBuilder(context, TwitchDatabase::class.java, "twitch_database")
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        GlobalScope.launch {
                            twitchDataStore.edit { it.clear() }
                        }
                    }
                })
                .build()
        }
    }
}
