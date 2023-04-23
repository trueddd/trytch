package com.github.trueddd.twitch.data

sealed class UserRequestType {
    data class Id(val value: List<String>) : UserRequestType()
    data class Token(val value: String) : UserRequestType()
}
