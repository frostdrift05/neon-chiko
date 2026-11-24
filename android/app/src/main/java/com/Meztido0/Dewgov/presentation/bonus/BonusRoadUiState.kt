package com.Meztido0.Dewgov.presentation.bonus

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.BonusOutcome

data class BonusRoadUiState(
    val junction: Int = 1,
    val totalJunctions: Int = GameConfig.BASE_JUNCTIONS,
    val practice: Boolean = false,
    val outcome: BonusOutcome? = null,
    val pickedLeft: Boolean = true,
    val finished: Boolean = false,
    val roadblock: Boolean = false,
    val creditsEarned: Int = 0,
    val routePointsEarned: Int = 0,
    val badgesEarned: Int = 0,
    val shardsEarned: Int = 0,
    val reducedMotion: Boolean = false,
    val skinTint: Long = 0xFFFF006E
) {
    val revealed: Boolean
        get() = outcome != null
}
