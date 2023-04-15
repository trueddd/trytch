package com.github.trueddd.twitch.data

private const val NORMAL = "normal"
private const val AFFILIATE = "affiliate"
private const val PARTNER = "partner"

enum class BroadcasterType(val value: String) {
    Normal(NORMAL),
    Affiliate(AFFILIATE),
    Partner(PARTNER);

    companion object {

        fun from(value: String): BroadcasterType {
            return when (value) {
                PARTNER -> Partner
                AFFILIATE -> Affiliate
                else -> Normal
            }
        }
    }
}
