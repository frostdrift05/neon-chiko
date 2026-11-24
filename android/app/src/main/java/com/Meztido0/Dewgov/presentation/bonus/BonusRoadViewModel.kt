package com.Meztido0.Dewgov.presentation.bonus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.BonusOutcomeType
import com.Meztido0.Dewgov.domain.model.MissionGoalType
import com.Meztido0.Dewgov.domain.model.SkinPerk
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ApplyRunRewardsUseCase
import com.Meztido0.Dewgov.domain.usecase.ResolveBonusJunctionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class BonusRoadViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val missionRepository: MissionRepository,
    private val runSession: RunSession,
    private val resolveJunction: ResolveBonusJunctionUseCase,
    private val applyRunRewards: ApplyRunRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BonusRoadUiState())
    val state: StateFlow<BonusRoadUiState> = _state.asStateFlow()

    private var revealJob: Job? = null
    private var random = Random(System.currentTimeMillis())

    fun begin(practice: Boolean) {
        revealJob?.cancel()
        val profile = profileRepository.profile.value
        val skin = SampleData.skinOf(profile.equippedSkin)
        val extra = if (skin.perk == SkinPerk.EXTRA_JUNCTION) 1 else 0
        random = Random(System.currentTimeMillis())
        _state.value = BonusRoadUiState(
            junction = 1,
            totalJunctions = GameConfig.BASE_JUNCTIONS + extra,
            practice = practice,
            reducedMotion = settingsRepository.settings.value.reducedMotion,
            skinTint = skin.tint
        )
    }

    fun pick(left: Boolean) {
        val current = _state.value
        if (current.revealed || current.finished) {
            return
        }
        val outcome = resolveJunction(current.junction, random)
        val roadblock = outcome.type == BonusOutcomeType.ROADBLOCK
        _state.update {
            it.copy(
                outcome = outcome,
                pickedLeft = left,
                roadblock = roadblock,
                creditsEarned = it.creditsEarned + outcome.credits,
                routePointsEarned = it.routePointsEarned + outcome.routePoints,
                badgesEarned = it.badgesEarned + if (outcome.badge) 1 else 0,
                shardsEarned = it.shardsEarned + outcome.shards
            )
        }
        revealJob = viewModelScope.launch {
            delay(GameConfig.REVEAL_HOLD_MS)
            val after = _state.value
            if (after.roadblock || after.junction >= after.totalJunctions) {
                complete()
            } else {
                _state.update { it.copy(junction = it.junction + 1, outcome = null) }
            }
        }
    }

    private fun complete() {
        val current = _state.value
        val credits = if (current.practice) 0 else current.creditsEarned
        profileRepository.update { profile ->
            var next = profile.copy(
                credits = profile.credits + credits,
                routePoints = profile.routePoints +
                    current.routePointsEarned + GameConfig.RP_PER_BONUS_ROUTE,
                skinShards = profile.skinShards + current.shardsEarned
            )
            next = applyRunRewards.awardBadge(next, "road_scout")
            if (current.badgesEarned > 0) {
                next = applyRunRewards.awardBadge(next, "chrome_touch")
            }
            next
        }
        missionRepository.advance(MissionGoalType.BONUS_ROUTES, 1)
        runSession.recordBonusRoute(
            credits = credits,
            routePoints = current.routePointsEarned + GameConfig.RP_PER_BONUS_ROUTE,
            badges = current.badgesEarned,
            shards = current.shardsEarned
        )
        _state.update { it.copy(finished = true) }
    }

    override fun onCleared() {
        revealJob?.cancel()
        super.onCleared()
    }
}
