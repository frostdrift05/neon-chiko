package com.Meztido0.Dewgov.presentation.rewards

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.domain.model.BoardRow
import com.Meztido0.Dewgov.domain.model.RankTier

data class RewardsUiState(
    val routePoints: Int = 0,
    val rankIndex: Int = 0,
    val rankNameRes: Int = R.string.rank_chick,
    val nextRankNameRes: Int = R.string.rank_scout,
    val nextRewardRes: Int = R.string.rank_scout_reward,
    val pointsToNext: Int = 0,
    val progress: Float = 0f,
    val ladder: List<RankTier> = emptyList(),
    val board: List<BoardRow> = emptyList()
)
