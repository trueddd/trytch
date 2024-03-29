package com.github.trueddd.trytch.settings

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsManagerImpl(
    private val settingsDataStore: DataStore<Preferences>,
) : SettingsManager, CoroutineScope {

    private companion object {
        const val TAG = "SettingsManager"
        val PreferredQualityKey = stringPreferencesKey("preferred_stream_quality")
        val ChatOverlayEnabledKey = booleanPreferencesKey("chat_overlay_enabled")
        val ChatOverlayOpacityKey = floatPreferencesKey("chat_overlay_opacity")
        val ChatOverlayShiftXKey = floatPreferencesKey("chat_overlay_shift_x")
        val ChatOverlayShiftYKey = floatPreferencesKey("chat_overlay_shift_y")
        val ChatOverlayWidthKey = intPreferencesKey("chat_overlay_width")
        val ChatOverlayHeightKey = intPreferencesKey("chat_overlay_height")
    }

    override val coroutineContext by lazy {
        SupervisorJob() + Dispatchers.IO
    }

    private fun Preferences.toUserSettings(): UserSettings {
        val streamSettings = StreamSettings(
            preferredQuality = this[PreferredQualityKey],
            chatOverlayEnabled = this[ChatOverlayEnabledKey],
            chatOverlayOpacity = this[ChatOverlayOpacityKey],
            chatOverlayShiftX = this[ChatOverlayShiftXKey],
            chatOverlayShiftY = this[ChatOverlayShiftYKey],
            chatOverlayWidthDp = this[ChatOverlayWidthKey],
            chatOverlayHeightDp = this[ChatOverlayHeightKey],
        )
        return UserSettings(
            streamSettings = streamSettings,
        )
    }

    private fun MutablePreferences.writeSettings(userSettings: UserSettings) {
        Log.d(TAG, "Saving settings: $userSettings")
        userSettings.streamSettings.preferredQuality?.let { this[PreferredQualityKey] = it }
        this[ChatOverlayEnabledKey] = userSettings.streamSettings.chatOverlayEnabled
        this[ChatOverlayOpacityKey] = userSettings.streamSettings.chatOverlayOpacity
        this[ChatOverlayShiftXKey] = userSettings.streamSettings.chatOverlayShiftX
        this[ChatOverlayShiftYKey] = userSettings.streamSettings.chatOverlayShiftY
        this[ChatOverlayWidthKey] = userSettings.streamSettings.chatOverlayWidthDp
        this[ChatOverlayHeightKey] = userSettings.streamSettings.chatOverlayHeightDp
    }

    override val settingsFlow = settingsDataStore.data
        .map { it.toUserSettings() }
        .onEach { Log.d(TAG, "Settings: $it") }
        .stateIn(this, SharingStarted.Eagerly, UserSettings.default())

    override fun modifySettings(block: (UserSettings) -> UserSettings) {
        launch {
            settingsDataStore.edit {
                it.writeSettings(block(it.toUserSettings()))
            }
        }
    }
}
