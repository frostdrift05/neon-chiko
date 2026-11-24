package com.Meztido0.Dewgov.domain.model

enum class BonusOutcomeType { CREDITS, FEATHER_BUNDLE, ROUTE_BADGE, SKIN_SHARD, ROADBLOCK }

data class BonusOutcome(
    val type: BonusOutcomeType,
    val credits: Int = 0,
    val routePoints: Int = 0,
    val shards: Int = 0,
    val badge: Boolean = false
)

data class JunctionResult(
    val junction: Int,
    val pickedLeft: Boolean,
    val outcome: BonusOutcome
)
