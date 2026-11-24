package com.Meztido0.Dewgov.presentation.menu

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig

data class MenuUiState(
    val credits: Int = GameConfig.STARTING_CREDITS,
    val routePoints: Int = 0,
    val rankNameRes: Int = R.string.rank_chick,
    val laneBoost: Int = 0,
    val laneBoostMax: Int = GameConfig.LANE_BOOST_MAX,
    val badgeCount: Int = 0,
    val totalBadges: Int = 12,
    val skinsUnlocked: Int = 1,
    val totalSkins: Int = 6,
    val districtIndex: Int = 1,
    val totalDistricts: Int = 5,
    val missionsActive: Int = 0,
    val totalMissions: Int = 3,
    val machinesUnlocked: Int = 1,
    val machinesTotal: Int = 3,
    val bet: Int = GameConfig.BET,
    val reducedMotion: Boolean = false
)
