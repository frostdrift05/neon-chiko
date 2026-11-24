package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.Machine
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import kotlin.random.Random

class SpinReelsUseCase {

    operator fun invoke(
        machine: Machine,
        lossStreak: Int,
        bigWinCooldown: Int,
        firstFeatherWild: Boolean,
        random: Random
    ): List<SlotSymbol> {
        val weights = buildWeights(machine, lossStreak, bigWinCooldown)
        val total = weights.values.sum()
        val cells = MutableList(GameConfig.CELLS) { draw(weights, total, random) }
        if (firstFeatherWild) {
            val index = cells.indexOf(SlotSymbol.FEATHER)
            if (index >= 0) {
                cells[index] = SlotSymbol.CHIKO
            }
        }
        return cells
    }

    fun randomSymbol(random: Random): SlotSymbol {
        val all = SlotSymbol.entries
        return all[random.nextInt(all.size)]
    }

    private fun buildWeights(
        machine: Machine,
        lossStreak: Int,
        bigWinCooldown: Int
    ): Map<SlotSymbol, Float> {
        val pityActive = lossStreak >= GameConfig.PITY_LOSS_STREAK && bigWinCooldown == 0
        return SlotSymbol.entries.associateWith { symbol ->
            var weight = symbol.weight.toFloat()
            when (symbol) {
                SlotSymbol.MARKER -> weight *= machine.markerBias
                SlotSymbol.FEATHER -> weight *= machine.lowBias
                SlotSymbol.ROAD_SIGN -> weight *= machine.lowBias
                SlotSymbol.CHIKO -> if (pityActive) {
                    weight *= GameConfig.PITY_WILD_MULTIPLIER
                }
                SlotSymbol.VAN -> weight *= 1f
            }
            weight
        }
    }

    private fun draw(weights: Map<SlotSymbol, Float>, total: Float, random: Random): SlotSymbol {
        var ticket = random.nextFloat() * total
        for (entry in weights) {
            ticket -= entry.value
            if (ticket <= 0f) {
                return entry.key
            }
        }
        return SlotSymbol.FEATHER
    }
}
