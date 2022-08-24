package com.github.trueddd.twitch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.trueddd.twitch.data.Tokens
import com.github.trueddd.twitch.data.User

@Database(
    entities = [
        User::class,
        Tokens::class,
    ],
    version = 2,
)
abstract class TwitchDatabase : RoomDatabase() {

    abstract fun twitchDao(): TwitchDao

    companion object {

        fun create(context: Context): TwitchDatabase {
            return Room.databaseBuilder(context, TwitchDatabase::class.java, "twitch_database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
