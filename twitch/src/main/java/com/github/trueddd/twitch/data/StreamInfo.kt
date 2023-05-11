package com.github.trueddd.twitch.data

data class StreamInfo(
    val quality: String,
    val url: String,
) {

    val resolution: Int
        get() = quality.substringBefore('p').toInt()

    val frameRate: Int?
        get() = quality.substringAfter('p').toIntOrNull()
}
