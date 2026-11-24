package com.Meztido0.Dewgov.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.domain.model.ReelSpeed
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val missionRepository: MissionRepository,
    private val runSession: RunSession
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update {
                    it.copy(
                        reelSpeed = settings.reelSpeed,
                        nearWinHighlight = settings.nearWinHighlight,
                        reducedMotion = settings.reducedMotion,
                        payoutTableOnStart = settings.payoutTableOnStart,
                        runSpins = settings.runSpins
                    )
                }
            }
        }
    }

    fun setReelSpeed(speed: ReelSpeed) {
        settingsRepository.update { it.copy(reelSpeed = speed) }
    }

    fun setNearWin(enabled: Boolean) {
        settingsRepository.update { it.copy(nearWinHighlight = enabled) }
    }

    fun setReducedMotion(enabled: Boolean) {
        settingsRepository.update { it.copy(reducedMotion = enabled) }
    }

    fun setPayoutTable(enabled: Boolean) {
        settingsRepository.update { it.copy(payoutTableOnStart = enabled) }
    }

    fun setRunSpins(spins: Int) {
        settingsRepository.update { it.copy(runSpins = spins) }
    }

    fun askReset() {
        _state.update { it.copy(showResetConfirm = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(showResetConfirm = false) }
    }

    fun confirmReset() {
        profileRepository.resetProgress()
        settingsRepository.resetDefaults()
        missionRepository.resetProgress()
        runSession.clear()
        _state.update { it.copy(showResetConfirm = false) }
    }
}
