package com.Meztido0.Dewgov.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ApplyRunRewardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val profileRepository: ProfileRepository,
    private val runSession: RunSession,
    private val applyRunRewards: ApplyRunRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                _state.update {
                    it.copy(
                        skins = SampleData.skins.map { skin ->
                            SkinEntry(
                                skin = skin,
                                unlocked = profile.unlockedSkins.contains(skin.id),
                                equipped = profile.equippedSkin == skin.id
                            )
                        },
                        badges = SampleData.badges.map { badge ->
                            BadgeEntry(badge, profile.badges.contains(badge.id))
                        },
                        shards = profile.skinShards
                    )
                }
            }
        }
    }

    fun equip(skinId: String) {
        val entry = _state.value.skins.firstOrNull { it.skin.id == skinId } ?: return
        if (!entry.unlocked) {
            return
        }
        profileRepository.update { profile ->
            applyRunRewards.awardBadge(profile.copy(equippedSkin = skinId), "chrome_touch")
        }
    }

    fun unlock(skinId: String) {
        val entry = _state.value.skins.firstOrNull { it.skin.id == skinId } ?: return
        if (entry.unlocked) {
            return
        }
        val profile = profileRepository.profile.value
        if (profile.skinShards < GameConfig.SKIN_SHARD_COST) {
            return
        }
        profileRepository.update {
            it.copy(
                skinShards = it.skinShards - GameConfig.SKIN_SHARD_COST,
                unlockedSkins = it.unlockedSkins + skinId,
                equippedSkin = skinId
            )
        }
        runSession.markUnlock()
    }
}
