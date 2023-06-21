package com.github.trueddd.trytch.settings

data class UserSettings(
    val streamSettings: StreamSettings,
) {

    companion object {
        fun default() = UserSettings(
            streamSettings = StreamSettings.default(),
        )
    }
}
