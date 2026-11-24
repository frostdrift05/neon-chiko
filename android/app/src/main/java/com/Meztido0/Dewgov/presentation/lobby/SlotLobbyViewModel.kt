package com.Meztido0.Dewgov.presentation.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SlotLobbyViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val runSession: RunSession
) : ViewModel() {

    private val _state = MutableStateFlow(SlotLobbyUiState())
    val state: StateFlow<SlotLobbyUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                val machines = SampleData.machines.map { machine ->
                    LobbyMachine(
                        machine = machine,
                        unlocked = profile.badgeCount >= machine.badgeRequirement,
                        selected = profile.selectedMachine == machine.id
                    )
                }
                _state.update {
                    it.copy(
                        machines = machines,
                        credits = profile.credits,
                        badgeCount = profile.badgeCount
                    )
                }
            }
        }
    }

    fun select(machineId: String) {
        val entry = _state.value.machines.firstOrNull { it.machine.id == machineId } ?: return
        if (!entry.unlocked) {
            return
        }
        profileRepository.update { it.copy(selectedMachine = machineId) }
    }

    fun startRun() {
        val profile = profileRepository.profile.value
        runSession.start(profile.selectedMachine, settingsRepository.settings.value.runSpins)
    }
}
