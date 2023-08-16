package com.github.trueddd.twitch

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object DateSerializer : KSerializer<Date> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)

    private val dateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    }

    override fun deserialize(decoder: Decoder): Date {
        return decoder.decodeString().let { dateFormat.parse(it) }!!
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(dateFormat.format(value))
    }
}
