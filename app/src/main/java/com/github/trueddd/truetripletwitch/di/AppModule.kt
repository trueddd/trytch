package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.truetripletwitch.ui.screens.main.MainViewModel
import com.github.trueddd.truetripletwitch.ui.screens.stream.StreamViewModel
import com.google.android.exoplayer2.ExoPlayer
import org.koin.dsl.module

val appModule = module {

    factory { MainViewModel(twitchClient = get()) }

    factory { (streamId: String) -> StreamViewModel(streamId, twitchClient = get(), player = get()) }

    single {
        ExoPlayer.Builder(get())
            .build()
            .apply {
                playWhenReady = true
            }
    }

    single<NodeViewModelStore> { mutableMapOf() }
}

typealias NodeViewModelStore = MutableMap<String, StatefulViewModel<*>>
