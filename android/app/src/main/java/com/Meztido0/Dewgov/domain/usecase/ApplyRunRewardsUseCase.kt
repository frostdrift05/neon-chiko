package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.PlayerProfile
import com.Meztido0.Dewgov.domain.model.RunOutcome
import com.Meztido0.Dewgov.domain.session.RunState

class ApplyRunRewardsUseCase {

    fun outcome(run: RunState, credits: Int, bet: Int): RunOutcome {
        return when {
            credits < bet -> RunOutcome.OUT_OF_CREDITS
            run.netWin >= 0 -> RunOutcome.PROFIT
            else -> RunOutcome.LOSS
        }
    }

    fun refill(profile: PlayerProfile, today: String): PlayerProfile {
        return profile.copy(
            credits = profile.credits + GameConfig.DAILY_REFILL,
            lastRefillDay = today
        )
    }

    fun awardBadge(profile: PlayerProfile, badgeId: String): PlayerProfile {
        if (profile.badges.contains(badgeId)) {
            return profile
        }
        return profile.copy(badges = profile.badges + badgeId)
    }

    fun nextUnearnedBadge(profile: PlayerProfile, allBadges: List<String>): String? {
        return allBadges.firstOrNull { !profile.badges.contains(it) }
    }
}
