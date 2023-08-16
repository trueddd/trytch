package com.github.trueddd.twitch.dto.twitch

import com.github.trueddd.twitch.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Calendar
import java.util.Date

/**
 * @param videoId An ID that identifies the video that the clip came from.
 * This field contains an empty string if the video is not available.
 * @param vodOffset The zero-based offset, in seconds, to where the clip starts in the video (VOD).
 * Is null if the video is not available or hasn’t been created yet from the live stream (see video_id).
 * @param language The ISO 639-1 two-letter language code that the broadcaster broadcasts in.
 * For example, en for English. The value is other if the broadcaster uses a language that Twitch doesn’t support.
 * @param duration The length of the clip, in seconds. Precision is 0.1.
 */
@Serializable
data class TwitchClip(
    @SerialName("broadcaster_id")
    val broadcasterId: String,
    @SerialName("broadcaster_name")
    val broadcasterName: String,
    @SerialName("game_id")
    val gameId: String,
    val id: String,
    val url: String,
    @SerialName("creator_id")
    val creatorId: String,
    @SerialName("creator_name")
    val creatorName: String,
    @SerialName("video_id")
    val videoId: String,
    val title: String,
    @SerialName("view_count")
    val viewCount: Int,
    val language: String,
    @Serializable(with = DateSerializer::class)
    @SerialName("created_at")
    val createdAt: Date,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    val duration: Float,
    @SerialName("vod_offset")
    val vodOffset: Int?,
) {

    companion object {
        fun test() = TwitchClip(
            id = "RandomClip1",
            url = "https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
            broadcasterId = "1234",
            broadcasterName = "JJ",
            creatorId = "123456",
            creatorName = "MrMarshall",
            videoId = "",
            gameId = "33103",
            language = "en",
            title = "random1",
            viewCount = 10,
            createdAt = Calendar.getInstance().apply {
                set(
                    /* year = */ 2017,
                    /* month = */ 11,
                    /* date = */ 30,
                    /* hourOfDay = */ 22,
                    /* minute = */ 34,
                    /* second = */ 18
                )
            }.time,
            thumbnailUrl = "https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg",
            duration = 12.9f,
            vodOffset = 1957,
        )
    }
}
