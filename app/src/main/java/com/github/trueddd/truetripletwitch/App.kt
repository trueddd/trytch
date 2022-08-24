package com.github.trueddd.truetripletwitch

import android.app.Application
import com.github.trueddd.truetripletwitch.di.twitchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(twitchModule)
        }
    }
}
