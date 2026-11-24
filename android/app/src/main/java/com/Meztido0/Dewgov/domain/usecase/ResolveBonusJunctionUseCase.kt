package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.domain.model.BonusOutcome
import com.Meztido0.Dewgov.domain.model.BonusOutcomeType
import kotlin.random.Random

class ResolveBonusJunctionUseCase {

    operator fun invoke(junction: Int, random: Random): BonusOutcome {
        val bag = mutableListOf<Pair<BonusOutcome, Int>>()
        bag.add(BonusOutcome(BonusOutcomeType.CREDITS, credits = 120) to 16)
        bag.add(BonusOutcome(BonusOutcomeType.CREDITS, credits = 240) to 14)
        bag.add(BonusOutcome(BonusOutcomeType.CREDITS, credits = 400) to 10)
        bag.add(BonusOutcome(BonusOutcomeType.FEATHER_BUNDLE, routePoints = 30) to 25)
        bag.add(BonusOutcome(BonusOutcomeType.ROUTE_BADGE, badge = true, routePoints = 10) to 15)
        bag.add(BonusOutcome(BonusOutcomeType.SKIN_SHARD, shards = 1) to 10)
        if (junction > 1) {
            bag.add(BonusOutcome(BonusOutcomeType.ROADBLOCK) to 10)
        }
        val total = bag.sumOf { it.second }
        var ticket = random.nextInt(total)
        for (entry in bag) {
            ticket -= entry.second
            if (ticket < 0) {
                return entry.first
            }
        }
        return bag.first().first
    }
}
