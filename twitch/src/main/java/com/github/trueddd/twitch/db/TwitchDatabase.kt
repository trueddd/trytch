package com.github.trueddd.twitch.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.Tokens
import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.data.BadgeVersion
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Tokens::class,
        Stream::class,
        BadgeVersion::class,
    ],
    version = 7,
    autoMigrations = [
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6, spec = TwitchDatabase.TwitchStreamTagsMigration::class),
    ],
)
@TypeConverters(
    TwitchStreamTagsConverter::class,
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

    @RenameColumn(tableName = "streams", fromColumnName = "tagIds", toColumnName = "tags")
    class TwitchStreamTagsMigration : AutoMigrationSpec
}
