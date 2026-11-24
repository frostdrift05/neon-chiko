package com.Meztido0.Dewgov.presentation.lobby

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.Machine

data class LobbyMachine(
    val machine: Machine,
    val unlocked: Boolean,
    val selected: Boolean
)

data class SlotLobbyUiState(
    val machines: List<LobbyMachine> = emptyList(),
    val credits: Int = GameConfig.STARTING_CREDITS,
    val badgeCount: Int = 0
)
