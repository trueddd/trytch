package com.github.trueddd.twitch

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.github.trueddd.twitch.db.TwitchDatabase
import com.github.trueddd.twitch.emotes.CommonEmotesProvider
import com.github.trueddd.twitch.emotes.EmotesProvider

val Context.dataStore by preferencesDataStore(name = "twitch_store")

interface TwitchClient : TwitchUserManager, TwitchStreamsManager, TwitchBadgesManager, EmotesProvider {

    companion object {
        fun create(context: Context, database: TwitchDatabase): TwitchClient {
            val httpClient = createHttpClient(database.twitchDao())
            return TwitchClientImpl(
                badgesManager = TwitchBadgesManagerImpl(
                    httpClient,
                    database.twitchDao(),
                    database.badgeDao(),
                    context.dataStore,
                ),
                streamsManager = TwitchStreamsManagerImpl(
                    httpClient,
                    database.twitchDao(),
                ),
                userManager = TwitchUserManagerImpl(
                    database.twitchDao(),
                    httpClient,
                ),
                emotesProvider = CommonEmotesProvider(
                    httpClient,
                    database.twitchDao(),
                    database.emoteDao(),
                ),
            )
        }
    }
}
