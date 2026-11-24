package com.Meztido0.Dewgov.presentation.responsible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ResponsiblePlayViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResponsiblePlayUiState())
    val state: StateFlow<ResponsiblePlayUiState> = _state.asStateFlow()

    private var tickJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                profileRepository.profile,
                settingsRepository.settings
            ) { profile, settings ->
                Triple(profile.spinsToday, profile.credits, settings)
            }.collect { triple ->
                _state.update {
                    it.copy(
                        spinsToday = triple.first,
                        credits = triple.second,
                        reminderEnabled = triple.third.breakReminderEnabled,
                        intervalMinutes = triple.third.breakIntervalMinutes
                    )
                }
            }
        }
    }

    fun resumeSession() {
        if (tickJob != null) {
            return
        }
        tickJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _state.update { current ->
                    val seconds = current.sessionSeconds + 1
                    val limit = (current.intervalMinutes * 60L).coerceAtLeast(60L)
                    val due = current.reminderEnabled && seconds % limit == 0L
                    current.copy(
                        sessionSeconds = seconds,
                        showReminder = current.showReminder || due
                    )
                }
            }
        }
    }

    fun pauseSession() {
        tickJob?.cancel()
        tickJob = null
    }

    fun setReminderEnabled(enabled: Boolean) {
        settingsRepository.update { it.copy(breakReminderEnabled = enabled) }
    }

    fun setInterval(minutes: Int) {
        settingsRepository.update { it.copy(breakIntervalMinutes = minutes) }
    }

    fun dismissReminder() {
        _state.update { it.copy(showReminder = false) }
    }

    fun askResetCredits() {
        _state.update { it.copy(showResetConfirm = true) }
    }

    fun cancelResetCredits() {
        _state.update { it.copy(showResetConfirm = false) }
    }

    fun confirmResetCredits() {
        profileRepository.resetCredits()
        _state.update { it.copy(showResetConfirm = false) }
    }

    override fun onCleared() {
        pauseSession()
        super.onCleared()
    }
}
