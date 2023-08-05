package com.github.trueddd.trytch.di

import com.github.trueddd.trytch.navigation.AppBackPressStrategy
import com.github.trueddd.trytch.navigation.AppBackStack
import com.github.trueddd.trytch.settings.SettingsManager
import com.github.trueddd.trytch.ui.StatelessViewModel
import com.github.trueddd.trytch.ui.screens.main.MainViewModel
import com.github.trueddd.trytch.ui.screens.profile.ProfileViewModel
import com.github.trueddd.trytch.ui.screens.splash.SplashViewModel
import com.github.trueddd.trytch.ui.screens.stream.StreamViewModel
import com.github.trueddd.trytch.ui.screens.stream.chat.EmotesPanelViewModel
import com.github.trueddd.trytch.ui.screens.stream.page.StreamerPageViewModel
import com.github.trueddd.twitch.data.User
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

    factory { (appBackStackHandlerStrategy: AppBackPressStrategy) -> EmotesPanelViewModel(
        emotesProvider = get(),
        appBackPressStrategy = appBackStackHandlerStrategy,
    ) }

    single { SplashViewModel(
        twitchStreamsManager = get(),
        twitchBadgesManager = get(),
        twitchUserManager = get(),
        emotesProvider = get(),
    ) }

    factory { (backStack: AppBackStack) -> ProfileViewModel(
        twitchUserManager = get(),
        backStack = backStack,
    ) }

    factory { (user: User) -> StreamerPageViewModel(
        user = user,
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
