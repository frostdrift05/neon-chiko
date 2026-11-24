package com.Meztido0.Dewgov.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val STARTING_CREDITS = 2000
    const val BET = 20
    const val DEFAULT_RUN_SPINS = 3
    const val DAILY_REFILL = 500
    const val LANE_BOOST_MAX = 12
    const val REELS = 3
    const val ROWS = 3
    const val CELLS = 9
    const val PITY_LOSS_STREAK = 5
    const val PITY_WILD_MULTIPLIER = 1.4f
    const val BIG_WIN_MULTIPLIER = 10
    const val BIG_WIN_COOLDOWN_SPINS = 2
    const val MARKERS_FOR_BONUS = 3
    const val BASE_JUNCTIONS = 4
    const val SKIN_SHARD_COST = 3
    const val RP_PER_SPIN = 1
    const val RP_PER_WIN = 5
    const val RP_PER_BONUS_ROUTE = 15
    const val RP_PER_MISSION = 25
    const val MISSION_CLAIM_CREDITS = 250
    const val BANNER_HOLD_MS = 1400L
    const val NEAR_WIN_HOLD_MS = 900L
    const val RESOLVE_DELAY_MS = 250L
    const val REEL_TICK_MS = 60L
    const val REVEAL_HOLD_MS = 1200L

    val PAYLINES: List<List<Int>> = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )
    const val HORIZONTAL_LINES = 3
}
