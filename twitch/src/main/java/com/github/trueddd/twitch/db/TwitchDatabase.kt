package com.github.trueddd.twitch.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
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
    version = 6,
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

    companion object {

        fun create(context: Context): TwitchDatabase {
            return Room.databaseBuilder(context, TwitchDatabase::class.java, "twitch_database")
                .build()
        }
    }

    @RenameColumn(tableName = "streams", fromColumnName = "tagIds", toColumnName = "tags")
    class TwitchStreamTagsMigration : AutoMigrationSpec
}
