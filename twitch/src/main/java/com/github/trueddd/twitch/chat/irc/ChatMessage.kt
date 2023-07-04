package com.github.trueddd.twitch.chat.irc

sealed class ChatMessage {

    sealed class Response : ChatMessage() {

        object Authorization : Response()
        object Capabilities : Response()
        object Ping : Response()
    }

    sealed class Request : ChatMessage() {
        
    }
}
