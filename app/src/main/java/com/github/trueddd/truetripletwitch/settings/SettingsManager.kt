package com.github.trueddd.truetripletwitch.settings

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.StateFlow

private val Context.settingsDataStore by preferencesDataStore(name = "settings_store")

interface SettingsManager {

    companion object {
        fun create(
            context: Context,
        ): SettingsManager {
            return SettingsManagerImpl(
                context.settingsDataStore,
            )
        }
    }

    val settingsFlow: StateFlow<UserSettings>

    fun modifySettings(block: (UserSettings) -> UserSettings)
}

fun SettingsManager.modifyStreamSettings(block: (StreamSettings) -> StreamSettings) {
    modifySettings { settings ->
        val streamSettings = block(settings.streamSettings)
        settings.copy(streamSettings = streamSettings)
    }
}
