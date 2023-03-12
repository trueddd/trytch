package com.github.trueddd.twitch.data

sealed class ConnectionStatus {
    object Connecting : ConnectionStatus()
    object Connected : ConnectionStatus()
    data class Disconnected(val error: Exception?) : ConnectionStatus()
}
