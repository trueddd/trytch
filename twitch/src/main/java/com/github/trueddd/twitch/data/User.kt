package com.github.trueddd.twitch.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "users",
    primaryKeys = ["id"],
)
@Parcelize
data class User(
    val id: String,
    val login: String,
    val displayName: String,
    val profileImageUrl: String,
    @ColumnInfo(defaultValue = "")
    val offlineImageUrl: String,
    val broadcasterType: BroadcasterType,
    val description: String,
    val current: Boolean = false,
) : Parcelable {

    companion object {

        fun test() = User(
            id = "1235342124",
            login = "truetripled",
            displayName = "truetripled",
            profileImageUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/c0fb8aca-3fc7-41f9-b336-1c39b5dc3afc-profile_image-300x300.png",
            offlineImageUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/3f13ab61-ec78-4fe6-8481-8682cb3b0ac2-channel_offline_image-1920x1080.png",
            broadcasterType = BroadcasterType.Affiliate,
            description = "Just a regular Twitch streamer.",
        )
    }
}
