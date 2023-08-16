package com.github.trueddd.trytch.di

import android.content.Context
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchClient
import com.github.trueddd.twitch.TwitchClipsManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.TwitchUserManager
import com.github.trueddd.twitch.chat.ChatManager
import com.github.trueddd.twitch.dataStore
import com.github.trueddd.twitch.db.TwitchDatabase
import com.github.trueddd.twitch.emotes.EmotesProvider
import org.koin.dsl.binds
import org.koin.dsl.module

val twitchModule = module {

    single { get<Context>().dataStore }

    single { TwitchDatabase.create(context = get(), twitchDataStore = get()) }

    single { TwitchClient.create(database = get(), context = get()) } binds arrayOf(
        TwitchBadgesManager::class,
        TwitchUserManager::class,
        TwitchStreamsManager::class,
        EmotesProvider::class,
        TwitchClipsManager::class,
    )

    single { ChatManager.create(twitchBadgesManager = get(), database = get(), emotesProvider = get()) }
}
