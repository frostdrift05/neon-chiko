package com.Meztido0.Dewgov.domain.model

enum class SpriteKind { CHIKO, ROAD_SIGN, VAN, FEATHER, MARKER }

enum class Volatility { LOW, MEDIUM, HIGH }

data class Machine(
    val id: String,
    val nameRes: Int,
    val weightingRes: Int,
    val topLine: Int,
    val volatility: Volatility,
    val badgeRequirement: Int,
    val art: SpriteKind,
    val markerBias: Float,
    val lowBias: Float
)

data class District(
    val id: String,
    val nameRes: Int,
    val badgeRequirement: Int,
    val grantRes: Int,
    val machineId: String
)
