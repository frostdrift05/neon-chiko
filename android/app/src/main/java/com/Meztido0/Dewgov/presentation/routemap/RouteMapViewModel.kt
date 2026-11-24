package com.Meztido0.Dewgov.presentation.routemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteMapViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RouteMapUiState())
    val state: StateFlow<RouteMapUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                val currentIndex = SampleData.districts
                    .indexOfLast { profile.badgeCount >= it.badgeRequirement }
                    .coerceAtLeast(0)
                val nodes = SampleData.districts.mapIndexed { index, district ->
                    RouteNode(
                        district = district,
                        unlocked = profile.badgeCount >= district.badgeRequirement,
                        current = index == currentIndex
                    )
                }
                _state.update { it.copy(nodes = nodes, badgeCount = profile.badgeCount) }
            }
        }
    }

    fun toggle(districtId: String) {
        val node = _state.value.nodes.firstOrNull { it.district.id == districtId } ?: return
        if (!node.unlocked) {
            return
        }
        _state.update {
            it.copy(expandedId = if (it.expandedId == districtId) "" else districtId)
        }
    }

    fun selectMachine(machineId: String) {
        profileRepository.update { it.copy(selectedMachine = machineId) }
    }
}
