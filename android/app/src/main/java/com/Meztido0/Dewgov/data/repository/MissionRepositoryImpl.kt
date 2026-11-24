package com.Meztido0.Dewgov.data.repository

import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.MissionGoalType
import com.Meztido0.Dewgov.domain.model.MissionProgress
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.usecase.RefreshDailyMissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class MissionRepositoryImpl(
    private val store: PreferencesStore,
    private val refreshDaily: RefreshDailyMissionsUseCase
) : MissionRepository {

    private val _missions = MutableStateFlow(load())
    override val missions: StateFlow<List<MissionProgress>> = _missions.asStateFlow()

    override fun refreshForToday() {
        val today = LocalDate.now().toString()
        if (refreshDaily(store.readMissionDay(), today)) {
            store.clearMissions(SampleData.missions.map { it.id })
            store.writeMissionDay(today)
            _missions.value = load()
        }
    }

    override fun advance(type: MissionGoalType, amount: Int) {
        if (amount <= 0) {
            return
        }
        _missions.value = _missions.value.map { entry ->
            if (entry.mission.type != type || entry.claimed) {
                entry
            } else {
                val next = entry.progress + amount
                store.writeMission(entry.mission.id, next, entry.claimed)
                entry.copy(progress = next)
            }
        }
    }

    override fun claim(missionId: String): Boolean {
        val target = _missions.value.firstOrNull { it.mission.id == missionId } ?: return false
        if (!target.complete || target.claimed) {
            return false
        }
        store.writeMission(missionId, target.progress, true)
        _missions.value = _missions.value.map {
            if (it.mission.id == missionId) it.copy(claimed = true) else it
        }
        return true
    }

    override fun resetProgress() {
        store.clearMissions(SampleData.missions.map { it.id })
        store.writeMissionDay(LocalDate.now().toString())
        _missions.value = load()
    }

    private fun load(): List<MissionProgress> = SampleData.missions.map { mission ->
        MissionProgress(
            mission = mission,
            progress = store.readMissionProgress(mission.id),
            claimed = store.readMissionClaimed(mission.id)
        )
    }
}
