package com.Meztido0.Dewgov.domain.repository

import com.Meztido0.Dewgov.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>

    fun update(transform: (GameSettings) -> GameSettings)

    fun resetDefaults()
}
