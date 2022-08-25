package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.twitch.TwitchClient
import org.koin.dsl.module

val twitchModule = module {

    single { TwitchClient.create(context = get()) }
}
