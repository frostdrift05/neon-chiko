package com.Meztido0.Dewgov.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.GameSettings
import com.Meztido0.Dewgov.domain.model.MissionProgress
import com.Meztido0.Dewgov.domain.model.PlayerProfile
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ComputeRankUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MenuViewModel(
    private val profileRepository: ProfileRepository,
    private val missionRepository: MissionRepository,
    private val settingsRepository: SettingsRepository,
    private val runSession: RunSession,
    private val computeRank: ComputeRankUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        missionRepository.refreshForToday()
        viewModelScope.launch {
            combine(
                profileRepository.profile,
                missionRepository.missions,
                settingsRepository.settings
            ) { profile, missions, settings ->
                build(profile, missions, settings)
            }.collect { next ->
                _state.value = next
            }
        }
    }

    fun startRun() {
        val profile = profileRepository.profile.value
        val spins = settingsRepository.settings.value.runSpins
        runSession.start(profile.selectedMachine, spins)
    }

    private fun build(
        profile: PlayerProfile,
        missions: List<MissionProgress>,
        settings: GameSettings
    ): MenuUiState {
        val rankIndex = computeRank(profile.routePoints, SampleData.ranks)
        val district = SampleData.districts.indexOfLast { profile.badgeCount >= it.badgeRequirement }
        return MenuUiState(
            credits = profile.credits,
            routePoints = profile.routePoints,
            rankNameRes = SampleData.ranks[rankIndex].nameRes,
            laneBoost = profile.laneBoost,
            laneBoostMax = GameConfig.LANE_BOOST_MAX,
            badgeCount = profile.badgeCount,
            totalBadges = SampleData.badges.size,
            skinsUnlocked = profile.unlockedSkins.size,
            totalSkins = SampleData.skins.size,
            districtIndex = (district + 1).coerceAtLeast(1),
            totalDistricts = SampleData.districts.size,
            missionsActive = missions.count { !it.claimed },
            totalMissions = missions.size,
            machinesUnlocked = SampleData.machines.count { profile.badgeCount >= it.badgeRequirement },
            machinesTotal = SampleData.machines.size,
            bet = GameConfig.BET,
            reducedMotion = settings.reducedMotion
        )
    }
}
