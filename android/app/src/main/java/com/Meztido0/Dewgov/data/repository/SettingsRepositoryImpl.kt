package com.Meztido0.Dewgov.data.repository

import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.domain.model.GameSettings
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    private val _settings = MutableStateFlow(store.readSettings())
    override val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    override fun update(transform: (GameSettings) -> GameSettings) {
        val next = transform(_settings.value)
        _settings.value = next
        store.writeSettings(next)
    }

    override fun resetDefaults() {
        val fresh = GameSettings()
        _settings.value = fresh
        store.writeSettings(fresh)
    }
}
