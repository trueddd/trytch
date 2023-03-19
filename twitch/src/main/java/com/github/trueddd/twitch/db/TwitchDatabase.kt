package com.github.trueddd.twitch.db

import android.content.Context
import androidx.room.*
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.Tokens
import com.github.trueddd.twitch.data.User
import com.github.trueddd.twitch.data.BadgeVersion

@Database(
    entities = [
        User::class,
        Tokens::class,
        Stream::class,
        BadgeVersion::class,
    ],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
    ],
)
@TypeConverters(
    TwitchStreamTagsConverter::class,
)
abstract class TwitchDatabase : RoomDatabase() {

    abstract fun twitchDao(): TwitchDao
    abstract fun badgeDao(): BadgeDao

    companion object {

        fun create(context: Context): TwitchDatabase {
            return Room.databaseBuilder(context, TwitchDatabase::class.java, "twitch_database")
                .build()
        }
    }
}
