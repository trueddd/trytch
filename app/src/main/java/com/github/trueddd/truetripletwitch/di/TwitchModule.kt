package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchClient
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.TwitchUserManager
import com.github.trueddd.twitch.chat.ChatManager
import com.github.trueddd.twitch.db.TwitchDatabase
import org.koin.dsl.binds
import org.koin.dsl.module

val twitchModule = module {

    single { TwitchDatabase.create(context = get()) }

    single { TwitchClient.create(database = get(), context = get()) } binds arrayOf(
        TwitchBadgesManager::class,
        TwitchUserManager::class,
        TwitchStreamsManager::class,
    )

    single { ChatManager.create(twitchBadgesManager = get(), database = get()) }
}
