package com.Meztido0.Dewgov.data.repository

import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.domain.model.PlayerProfile
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class ProfileRepositoryImpl(private val store: PreferencesStore) : ProfileRepository {

    private val _profile = MutableStateFlow(store.readProfile())
    override val profile: StateFlow<PlayerProfile> = _profile.asStateFlow()

    override fun update(transform: (PlayerProfile) -> PlayerProfile) {
        val next = transform(_profile.value)
        _profile.value = next
        store.writeProfile(next)
    }

    override fun resetCredits() {
        update { it.copy(credits = PlayerProfile().credits) }
    }

    override fun resetProgress() {
        val fresh = PlayerProfile()
        _profile.value = fresh
        store.writeProfile(fresh)
    }

    override fun today(): String = LocalDate.now().toString()
}
