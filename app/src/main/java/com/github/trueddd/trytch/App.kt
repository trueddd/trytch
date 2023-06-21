package com.github.trueddd.trytch

import android.app.Application
import com.github.trueddd.trytch.di.appModule
import com.github.trueddd.trytch.di.twitchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(twitchModule, appModule)
        }
    }
}
