package com.Meztido0.Dewgov.presentation.missions

import com.Meztido0.Dewgov.domain.model.MissionProgress

data class MissionsUiState(
    val missions: List<MissionProgress> = emptyList(),
    val credits: Int = 0,
    val claimedCount: Int = 0
)
