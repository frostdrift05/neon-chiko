package com.Meztido0.Dewgov.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.BoardRow
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.usecase.ComputeRankUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RewardsViewModel(
    private val profileRepository: ProfileRepository,
    private val computeRank: ComputeRankUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                val tiers = SampleData.ranks
                val index = computeRank(profile.routePoints, tiers)
                val nextIndex = (index + 1).coerceAtMost(tiers.lastIndex)
                val board = buildBoard(profile.routePoints)
                _state.update {
                    it.copy(
                        routePoints = profile.routePoints,
                        rankIndex = index,
                        rankNameRes = tiers[index].nameRes,
                        nextRankNameRes = tiers[nextIndex].nameRes,
                        nextRewardRes = tiers[nextIndex].rewardRes,
                        pointsToNext = computeRank.pointsToNext(profile.routePoints, tiers),
                        progress = computeRank.progressToNext(profile.routePoints, tiers),
                        ladder = tiers,
                        board = board
                    )
                }
            }
        }
    }

    private fun buildBoard(routePoints: Int): List<BoardRow> {
        val rows = SampleData.rivals.map { rival ->
            BoardRow(rival.nameRes, rival.routePoints, false)
        } + BoardRow(R.string.board_you, routePoints, true)
        return rows.sortedByDescending { it.routePoints }
    }
}
