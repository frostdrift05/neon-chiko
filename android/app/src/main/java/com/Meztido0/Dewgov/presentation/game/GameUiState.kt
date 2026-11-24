package com.Meztido0.Dewgov.presentation.game

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.RunSummary
import com.Meztido0.Dewgov.domain.model.SlotSymbol

enum class GamePhase { IDLE, SPINNING, RESOLVING }

data class GameUiState(
    val grid: List<SlotSymbol> = List(GameConfig.CELLS) { SlotSymbol.FEATHER },
    val stoppedReels: Int = GameConfig.REELS,
    val phase: GamePhase = GamePhase.IDLE,
    val credits: Int = GameConfig.STARTING_CREDITS,
    val bet: Int = GameConfig.BET,
    val runWin: Int = 0,
    val spinIndex: Int = 0,
    val totalSpins: Int = GameConfig.DEFAULT_RUN_SPINS,
    val laneBoost: Int = 0,
    val laneBoostMax: Int = GameConfig.LANE_BOOST_MAX,
    val machineNameRes: Int = R.string.machine_diner,
    val perkRes: Int = R.string.skin_neon_perk,
    val winningCells: Set<Int> = emptySet(),
    val bannerWin: Int = 0,
    val bannerSymbolRes: Int = R.string.symbol_chiko,
    val bannerMultiplier: Int = 0,
    val nearWin: Boolean = false,
    val nearWinReel: Int = -1,
    val bonusReady: Boolean = false,
    val reducedMotion: Boolean = false,
    val showPayoutTable: Boolean = false,
    val showPause: Boolean = false
) {
    val spinEnabled: Boolean
        get() = phase == GamePhase.IDLE && credits >= bet
}

sealed interface GameEvent {
    data object OpenBonusRoad : GameEvent
    data class FinishRun(val summary: RunSummary) : GameEvent
}
