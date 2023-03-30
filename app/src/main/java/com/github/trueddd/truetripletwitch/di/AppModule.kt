package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.truetripletwitch.ui.StatelessViewModel
import com.github.trueddd.truetripletwitch.ui.screens.main.MainViewModel
import com.github.trueddd.truetripletwitch.ui.screens.splash.SplashViewModel
import com.github.trueddd.truetripletwitch.ui.screens.stream.StreamViewModel
import com.google.android.exoplayer2.ExoPlayer
import org.koin.dsl.module

val appModule = module {

    factory { MainViewModel(
        twitchUserManager = get(),
        twitchStreamsManager = get(),
    ) }

    factory { (channel: String) -> StreamViewModel(
        channel,
        twitchStreamsManager = get(),
        player = get(),
        chatManager = get(),
        twitchBadgesManager = get(),
    ) }

    factory { SplashViewModel(
        twitchStreamsManager = get(),
        twitchBadgesManager = get(),
        twitchUserManager = get(),
    ) }

    single {
        ExoPlayer.Builder(get())
            .build()
            .apply {
                playWhenReady = true
            }
    }

    single<NodeViewModelStore> { mutableMapOf() }
}

typealias NodeViewModelStore = MutableMap<String, StatelessViewModel>
