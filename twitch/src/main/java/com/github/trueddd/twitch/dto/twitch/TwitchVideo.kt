package com.github.trueddd.twitch.dto.twitch

import com.github.trueddd.twitch.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.util.Calendar
import java.util.Date

/**
 * @property language The ISO 639-1 two-letter language code that the broadcaster broadcasts in.
 * For example, en for English. The value is other if the broadcaster uses a language that Twitch doesn’t support.
 * @property duration The video’s length in ISO 8601 duration format.
 * For example, 3m21s represents 3 minutes, 21 seconds.
 * @property thumbnailUrl A URL to a thumbnail image of the video.
 * Before using the URL, you must replace the `%{width}` and `%{height}` placeholders with the width and height of the thumbnail you want returned.
 * Specify the width and height in pixels. Because the CDN preserves the thumbnail’s ratio, the thumbnail may not be the exact size you requested.
 */
@Serializable
data class TwitchVideo(
    val id: String,
    @SerialName("stream_id")
    val streamId: String?,
    val title: String,
    val description: String,
    val url: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("user_login")
    val userLogin: String,
    @SerialName("view_count")
    val viewCount: Int,
    val language: String,
    @Serializable(with = DateSerializer::class)
    @SerialName("created_at")
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    @SerialName("published_at")
    val publishedAt: Date,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    val duration: String,
    val type: Type,
    @SerialName("muted_segments")
    val mutedSegments: List<MutedSegment>?,
) {

    fun getThumbnailUrl(width: Int, height: Int): HttpUrl {
        return thumbnailUrl
            .replace("%{width}", width.toString())
            .replace("%{height}", height.toString())
            .toHttpUrl()
    }

    @Serializable
    enum class Type {
        @SerialName("archive")
        Archive,
        @SerialName("highlight")
        Highlight,
        @SerialName("upload")
        Upload;
    }

    /**
     * @property offset The offset, in seconds, from the beginning of the video to where the muted segment begins.
     * @property duration The duration of the muted segment, in seconds.
     */
    @Serializable
    data class MutedSegment(
        val offset: Int,
        val duration: Int,
    )

    companion object {
        fun test() = TwitchVideo(
            id = "335921245",
            streamId = null,
            userId = "1234",
            userName = "JJ",
            userLogin = "jj",
            title = "random1",
            description = "Welcome to Twitch development! Here is a quick overview of our products and information to help you get started.",
            createdAt = Calendar.getInstance().apply {
                set(
                    /* year = */ 2018,
                    /* month = */ 11,
                    /* date = */ 14,
                    /* hourOfDay = */ 21,
                    /* minute = */ 30,
                    /* second = */ 18
                )
            }.time,
            publishedAt = Calendar.getInstance().apply {
                set(
                    /* year = */ 2018,
                    /* month = */ 11,
                    /* date = */ 14,
                    /* hourOfDay = */ 22,
                    /* minute = */ 4,
                    /* second = */ 30
                )
            }.time,
            url = "https://www.twitch.tv/videos/335921245",
            thumbnailUrl = "https://static-cdn.jtvnw.net/cf_vods/d2nvs31859zcd8/twitchdev/335921245/ce0f3a7f-57a3-4152-bc06-0c6610189fb3/thumb/index-0000000000-%{width}x%{height}.jpg",
            viewCount = 1863062,
            language = "en",
            type = Type.Upload,
            duration = "3m21s",
            mutedSegments = listOf(
                MutedSegment(duration = 30, offset = 120),
            ),
        )
    }
}
