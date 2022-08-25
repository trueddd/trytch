package com.github.trueddd.truetripletwitch.di

import com.github.trueddd.truetripletwitch.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MainViewModel(twitchClient = get()) }
}
