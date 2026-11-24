package com.Meztido0.Dewgov.domain.model

enum class RunOutcome { PROFIT, LOSS, OUT_OF_CREDITS }

data class RunSummary(
    val machineId: String,
    val outcome: RunOutcome,
    val spins: Int,
    val netWin: Int,
    val routePoints: Int,
    val badgesEarned: Int,
    val bonusRoutes: Int,
    val laneBoost: Int,
    val creditsLeft: Int,
    val missionNameRes: Int,
    val missionProgress: Int,
    val missionGoal: Int,
    val missionDelta: Int,
    val unlockedSomething: Boolean
)
