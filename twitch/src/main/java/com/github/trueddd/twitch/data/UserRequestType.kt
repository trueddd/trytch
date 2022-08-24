package com.github.trueddd.twitch.data

sealed class UserRequestType {
    data class Id(val value: String) : UserRequestType()
    data class Token(val value: String) : UserRequestType()
}
