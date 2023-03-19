package com.github.trueddd.twitch

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.github.trueddd.twitch.db.TwitchDatabase

internal val Context.dataStore by preferencesDataStore(name = "twitch_store")

interface TwitchClient : TwitchUserManager, TwitchStreamsManager, TwitchBadgesManager {

    companion object {
        fun create(context: Context, database: TwitchDatabase): TwitchClient {
            val httpClient = createHttpClient(database.twitchDao())
            return TwitchClientImpl(
                twitchDao = database.twitchDao(),
                httpClient = httpClient,
                badgesManager = TwitchBadgesManagerImpl(
                    httpClient,
                    database.twitchDao(),
                    database.badgeDao(),
                    context.dataStore,
                ),
            )
        }
    }
}
