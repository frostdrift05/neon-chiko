package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.domain.model.RankTier

class ComputeRankUseCase {

    operator fun invoke(routePoints: Int, tiers: List<RankTier>): Int {
        var index = 0
        tiers.forEachIndexed { position, tier ->
            if (routePoints >= tier.threshold) {
                index = position
            }
        }
        return index
    }

    fun progressToNext(routePoints: Int, tiers: List<RankTier>): Float {
        val index = invoke(routePoints, tiers)
        if (index >= tiers.lastIndex) {
            return 1f
        }
        val from = tiers[index].threshold
        val to = tiers[index + 1].threshold
        val span = (to - from).coerceAtLeast(1)
        return ((routePoints - from).toFloat() / span).coerceIn(0f, 1f)
    }

    fun pointsToNext(routePoints: Int, tiers: List<RankTier>): Int {
        val index = invoke(routePoints, tiers)
        if (index >= tiers.lastIndex) {
            return 0
        }
        return (tiers[index + 1].threshold - routePoints).coerceAtLeast(0)
    }
}
