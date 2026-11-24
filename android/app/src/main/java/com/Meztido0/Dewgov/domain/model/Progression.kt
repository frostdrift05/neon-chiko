package com.Meztido0.Dewgov.domain.model

data class RankTier(
    val id: String,
    val nameRes: Int,
    val threshold: Int,
    val rewardRes: Int
)

data class RivalEntry(
    val nameRes: Int,
    val routePoints: Int
)

data class BoardRow(
    val nameRes: Int,
    val routePoints: Int,
    val isPlayer: Boolean
)
