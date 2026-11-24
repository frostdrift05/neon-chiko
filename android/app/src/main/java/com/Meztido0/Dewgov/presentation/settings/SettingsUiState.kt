package com.Meztido0.Dewgov.presentation.settings

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.ReelSpeed

data class SettingsUiState(
    val reelSpeed: ReelSpeed = ReelSpeed.STANDARD,
    val nearWinHighlight: Boolean = true,
    val reducedMotion: Boolean = false,
    val payoutTableOnStart: Boolean = false,
    val runSpins: Int = GameConfig.DEFAULT_RUN_SPINS,
    val showResetConfirm: Boolean = false
)
