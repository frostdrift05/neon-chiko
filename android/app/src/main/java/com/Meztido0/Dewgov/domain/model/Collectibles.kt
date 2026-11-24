package com.Meztido0.Dewgov.domain.model

enum class SkinPerk { NONE, EXTRA_JUNCTION, FIRST_FEATHER_WILD, EXTRA_LANE_BOOST }

data class Skin(
    val id: String,
    val nameRes: Int,
    val perkRes: Int,
    val perk: SkinPerk,
    val tint: Long,
    val shardCost: Int
)

data class Badge(
    val id: String,
    val nameRes: Int,
    val conditionRes: Int
)
