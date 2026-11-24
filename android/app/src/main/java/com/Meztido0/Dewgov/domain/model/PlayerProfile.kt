package com.Meztido0.Dewgov.domain.model

import com.Meztido0.Dewgov.core.config.GameConfig

data class PlayerProfile(
    val credits: Int = GameConfig.STARTING_CREDITS,
    val routePoints: Int = 0,
    val badges: Set<String> = emptySet(),
    val unlockedSkins: Set<String> = setOf("neon"),
    val equippedSkin: String = "neon",
    val skinShards: Int = 0,
    val selectedMachine: String = "diner",
    val laneBoost: Int = 0,
    val lossStreak: Int = 0,
    val bigWinCooldown: Int = 0,
    val spinsToday: Int = 0,
    val lastRefillDay: String = ""
) {
    val badgeCount: Int
        get() = badges.size
}
