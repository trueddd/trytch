package com.github.trueddd.twitch.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Entity(
    tableName = "streams",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["followedByUserId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("followedByUserId")]
)
data class Stream(
    val id: String,
    val userId: String,
    val userLogin: String,
    val userName: String,
    val gameId: String,
    val gameName: String,
    val type: String,
    val title: String,
    val viewerCount: Int,
    val startedAt: String,
    val language: String,
    val thumbnailUrl: String,
    val tags: List<String>,
    val followedByUserId: String,
) {

    val shortenedViewerCount: String
        get() = when (viewerCount) {
            in 1_000_000 .. Int.MAX_VALUE -> String.format("%.1fM", viewerCount / 1_000_000f)
            in 1_000 .. 999_999 -> String.format("%.1fK", viewerCount / 1_000f)
            else -> "$viewerCount"
        }

    fun getThumbnailUrl(width: Int, height: Int): HttpUrl {
        return thumbnailUrl
            .replace("{width}", width.toString())
            .replace("{height}", height.toString())
            .toHttpUrl()
    }

    companion object {
        fun test() = Stream(
            id = "123",
            userId = "123",
            userLogin = "truetripled",
            userName = "truetripled",
            gameId = "444",
            gameName = "Dota 2",
            type = "live",
            title = "Regular streams are incoming",
            viewerCount = 12,
            startedAt = "2021-03-31T20:57:26Z",
            language = "ru",
            thumbnailUrl = "https://static-cdn.jtvnw.net/previews-ttv/live_user_aws-{width}x{height}.jpg",
            tags = listOf("English", "Cozy", "StreamForEveryone", "Cartoons", "Discuss", "NoPolitics"),
            followedByUserId = "123",
        )
    }
}
