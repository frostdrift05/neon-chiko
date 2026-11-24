package com.Meztido0.Dewgov.domain.repository

import com.Meztido0.Dewgov.domain.model.PlayerProfile
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val profile: StateFlow<PlayerProfile>

    fun update(transform: (PlayerProfile) -> PlayerProfile)

    fun resetCredits()

    fun resetProgress()

    fun today(): String
}
