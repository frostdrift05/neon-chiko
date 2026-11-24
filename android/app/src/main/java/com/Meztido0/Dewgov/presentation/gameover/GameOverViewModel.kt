package com.Meztido0.Dewgov.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ApplyRunRewardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val credits: Int = GameConfig.STARTING_CREDITS,
    val bet: Int = GameConfig.BET,
    val skinTint: Long = 0xFFFF006E,
    val reducedMotion: Boolean = false
) {
    val needsRefill: Boolean
        get() = credits < bet
}

class GameOverViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val runSession: RunSession,
    private val applyRunRewards: ApplyRunRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                profileRepository.profile,
                settingsRepository.settings
            ) { profile, settings ->
                GameOverUiState(
                    credits = profile.credits,
                    bet = GameConfig.BET,
                    skinTint = SampleData.skinOf(profile.equippedSkin).tint,
                    reducedMotion = settings.reducedMotion
                )
            }.collect { next ->
                _state.update { next }
            }
        }
    }

    fun startAgain() {
        val profile = profileRepository.profile.value
        runSession.start(profile.selectedMachine, settingsRepository.settings.value.runSpins)
    }

    fun refillAndStartAgain() {
        profileRepository.update { applyRunRewards.refill(it, profileRepository.today()) }
        startAgain()
    }
}
