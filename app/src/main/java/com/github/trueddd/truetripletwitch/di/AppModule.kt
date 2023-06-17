package com.github.trueddd.truetripletwitch.di

import com.bumble.appyx.navmodel.backstack.BackStack
import com.github.trueddd.truetripletwitch.navigation.Routing
import com.github.trueddd.truetripletwitch.settings.SettingsManager
import com.github.trueddd.truetripletwitch.ui.StatelessViewModel
import com.github.trueddd.truetripletwitch.ui.screens.main.MainViewModel
import com.github.trueddd.truetripletwitch.ui.screens.profile.ProfileViewModel
import com.github.trueddd.truetripletwitch.ui.screens.splash.SplashViewModel
import com.github.trueddd.truetripletwitch.ui.screens.stream.StreamViewModel
import com.google.android.exoplayer2.ExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    factory { MainViewModel(
        twitchUserManager = get(),
        twitchStreamsManager = get(),
        twitchBadgesManager = get(),
    ) }

    factory { (channel: String) -> StreamViewModel(
        channel,
        twitchStreamsManager = get(),
        player = get(),
        chatManager = get(),
        twitchBadgesManager = get(),
        emotesProvider = get(),
        settingsManager = get(),
    ) }

    single { SplashViewModel(
        twitchStreamsManager = get(),
        twitchBadgesManager = get(),
        twitchUserManager = get(),
        emotesProvider = get(),
    ) }

    factory { (backStack: BackStack<Routing>) -> ProfileViewModel(
        twitchUserManager = get(),
        backStack = backStack,
    ) }

    single {
        ExoPlayer.Builder(get())
            .build()
            .apply {
                playWhenReady = true
            }
    }

    single<NodeViewModelStore> { mutableMapOf() }

    single(createdAtStart = true) { SettingsManager.create(context = androidContext()) }
}

typealias NodeViewModelStore = MutableMap<String, StatelessViewModel>
