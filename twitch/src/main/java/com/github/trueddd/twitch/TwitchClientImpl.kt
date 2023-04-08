package com.github.trueddd.twitch

internal class TwitchClientImpl(
    badgesManager: TwitchBadgesManager,
    streamsManager: TwitchStreamsManager,
    userManager: TwitchUserManager,
) : TwitchClient,
    TwitchStreamsManager by streamsManager,
    TwitchBadgesManager by badgesManager,
    TwitchUserManager by userManager
