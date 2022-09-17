package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.twitch.chat.ChatManager
import com.github.trueddd.twitch.TwitchClient
import com.github.trueddd.twitch.db.TwitchDatabase
import org.koin.dsl.module

val twitchModule = module {

    single { TwitchDatabase.create(context = get()) }

    single { TwitchClient.create(database = get()) }

    single { ChatManager.create(database = get()) }
}
