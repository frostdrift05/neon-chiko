package com.Meztido0.Dewgov.presentation.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ApplyRunRewardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionsViewModel(
    private val missionRepository: MissionRepository,
    private val profileRepository: ProfileRepository,
    private val runSession: RunSession,
    private val applyRunRewards: ApplyRunRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MissionsUiState())
    val state: StateFlow<MissionsUiState> = _state.asStateFlow()

    init {
        missionRepository.refreshForToday()
        viewModelScope.launch {
            combine(
                missionRepository.missions,
                profileRepository.profile
            ) { missions, profile ->
                MissionsUiState(
                    missions = missions,
                    credits = profile.credits,
                    claimedCount = missions.count { it.claimed }
                )
            }.collect { next ->
                _state.update { next }
            }
        }
    }

    fun claim(missionId: String) {
        if (!missionRepository.claim(missionId)) {
            return
        }
        profileRepository.update { profile ->
            val rewarded = profile.copy(
                credits = profile.credits + GameConfig.MISSION_CLAIM_CREDITS,
                routePoints = profile.routePoints + GameConfig.RP_PER_MISSION
            )
            val withRun = applyRunRewards.awardBadge(rewarded, "midnight_run")
            val nextBadge = applyRunRewards.nextUnearnedBadge(
                withRun,
                listOf("first_feather", "triple_van", "lane_master", "road_scout", "chrome_touch")
            )
            if (nextBadge != null) {
                applyRunRewards.awardBadge(withRun, nextBadge)
            } else {
                withRun
            }
        }
        runSession.markUnlock()
    }
}
