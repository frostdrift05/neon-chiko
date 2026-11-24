package com.Meztido0.Dewgov.domain.model

enum class MissionGoalType { SPINS, FEATHERS, BONUS_ROUTES }

data class Mission(
    val id: String,
    val nameRes: Int,
    val descriptionRes: Int,
    val type: MissionGoalType,
    val goal: Int
)

data class MissionProgress(
    val mission: Mission,
    val progress: Int,
    val claimed: Boolean
) {
    val complete: Boolean
        get() = progress >= mission.goal

    val capped: Int
        get() = if (progress > mission.goal) mission.goal else progress
}
