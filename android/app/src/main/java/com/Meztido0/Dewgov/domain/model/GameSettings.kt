package com.Meztido0.Dewgov.domain.model

import com.Meztido0.Dewgov.core.config.GameConfig

enum class ReelSpeed(val firstStopMs: Long, val secondStopMs: Long, val thirdStopMs: Long) {
    RELAXED(900L, 1400L, 1900L),
    STANDARD(700L, 1100L, 1500L),
    TURBO(450L, 700L, 950L);

    val stops: List<Long>
        get() = listOf(firstStopMs, secondStopMs, thirdStopMs)
}

data class GameSettings(
    val reelSpeed: ReelSpeed = ReelSpeed.STANDARD,
    val nearWinHighlight: Boolean = true,
    val reducedMotion: Boolean = false,
    val payoutTableOnStart: Boolean = false,
    val runSpins: Int = GameConfig.DEFAULT_RUN_SPINS,
    val breakReminderEnabled: Boolean = false,
    val breakIntervalMinutes: Int = 30
)
