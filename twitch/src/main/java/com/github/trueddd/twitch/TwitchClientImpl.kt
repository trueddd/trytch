package com.github.trueddd.twitch

import com.github.trueddd.twitch.emotes.EmotesProvider

internal class TwitchClientImpl(
    badgesManager: TwitchBadgesManager,
    streamsManager: TwitchStreamsManager,
    userManager: TwitchUserManager,
    emotesProvider: EmotesProvider,
) : TwitchClient,
    TwitchStreamsManager by streamsManager,
    TwitchBadgesManager by badgesManager,
    TwitchUserManager by userManager,
    EmotesProvider by emotesProvider
