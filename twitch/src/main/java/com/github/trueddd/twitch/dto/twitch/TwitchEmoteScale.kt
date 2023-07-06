package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = TwitchEmoteScale.Serializer::class)
sealed class TwitchEmoteScale(val value: Int, val stringValue: String) {

    companion object {
        private const val BaseSize = 28
    }

    object Serializer : KSerializer<TwitchEmoteScale> {

        private val serializer = String.serializer()

        override val descriptor = serializer.descriptor

        override fun deserialize(decoder: Decoder): TwitchEmoteScale {
            return when (val scaleValue = serializer.deserialize(decoder)) {
                "3.0" -> Large
                "2.0" -> Medium
                "1.0" -> Small
                else -> throw SerializationException("Unexpected scale for twitch emote: $scaleValue")
            }
        }

        override fun serialize(encoder: Encoder, value: TwitchEmoteScale) {
            serializer.serialize(encoder, value.stringValue)
        }
    }

    val size: Int
        get() = value * BaseSize

    object Small : TwitchEmoteScale(1, "1.0")
    object Medium : TwitchEmoteScale(2, "2.0")
    object Large : TwitchEmoteScale(4, "3.0")
}
