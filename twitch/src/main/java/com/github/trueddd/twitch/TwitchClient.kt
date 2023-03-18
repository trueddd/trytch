package com.github.trueddd.twitch

import com.github.trueddd.twitch.db.TwitchDatabase

interface TwitchClient : TwitchUserManager, TwitchStreamsManager, TwitchBadgesManager {

    companion object {
        fun create(database: TwitchDatabase): TwitchClient {
            val httpClient = createHttpClient(database.twitchDao())
            return TwitchClientImpl(
                twitchDao = database.twitchDao(),
                httpClient = httpClient,
                badgesManager = TwitchBadgesManagerImpl(httpClient, database.badgeDao()),
            )
        }
    }
}
