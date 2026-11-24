package com.Meztido0.Dewgov.domain.repository

import com.Meztido0.Dewgov.domain.model.MissionGoalType
import com.Meztido0.Dewgov.domain.model.MissionProgress
import kotlinx.coroutines.flow.StateFlow

interface MissionRepository {
    val missions: StateFlow<List<MissionProgress>>

    fun refreshForToday()

    fun advance(type: MissionGoalType, amount: Int)

    fun claim(missionId: String): Boolean

    fun resetProgress()
}
