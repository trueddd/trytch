package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.twitch.TwitchClient
import com.github.trueddd.twitch.TwitchClientImpl
import org.koin.dsl.module

val twitchModule = module {

    single<TwitchClient> { TwitchClientImpl() }
}
