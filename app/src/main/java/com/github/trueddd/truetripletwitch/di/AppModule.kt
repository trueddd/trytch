package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.truetripletwitch.ui.screens.main.MainViewModel
import com.github.trueddd.truetripletwitch.ui.screens.stream.StreamViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MainViewModel(twitchClient = get()) }

    viewModel { (streamId: String) -> StreamViewModel(streamId, twitchClient = get()) }
}
