package com.Meztido0.Dewgov.presentation.collection

import com.Meztido0.Dewgov.domain.model.Badge
import com.Meztido0.Dewgov.domain.model.Skin

data class SkinEntry(
    val skin: Skin,
    val unlocked: Boolean,
    val equipped: Boolean
)

data class BadgeEntry(
    val badge: Badge,
    val unlocked: Boolean
)

data class CollectionUiState(
    val skins: List<SkinEntry> = emptyList(),
    val badges: List<BadgeEntry> = emptyList(),
    val shards: Int = 0
) {
    val unlockedBadges: Int
        get() = badges.count { it.unlocked }
}
