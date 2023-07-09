package com.github.trueddd.trytch.di

import com.bumble.appyx.navmodel.backstack.BackStack
import com.github.trueddd.trytch.navigation.Routing
import com.github.trueddd.trytch.settings.SettingsManager
import com.github.trueddd.trytch.ui.StatelessViewModel
import com.github.trueddd.trytch.ui.screens.main.MainViewModel
import com.github.trueddd.trytch.ui.screens.profile.ProfileViewModel
import com.github.trueddd.trytch.ui.screens.splash.SplashViewModel
import com.github.trueddd.trytch.ui.screens.stream.StreamViewModel
import com.google.android.exoplayer2.ExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    factory { MainViewModel(
        twitchUserManager = get(),
        twitchStreamsManager = get(),
        twitchBadgesManager = get(),
        chatManager = get(),
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
