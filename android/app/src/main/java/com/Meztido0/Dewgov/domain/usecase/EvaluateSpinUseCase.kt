package com.Meztido0.Dewgov.domain.usecase

import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.domain.model.PaylineWin
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import com.Meztido0.Dewgov.domain.model.SpinResult

class EvaluateSpinUseCase {

    operator fun invoke(grid: List<SlotSymbol>, bet: Int): SpinResult {
        val wins = mutableListOf<PaylineWin>()
        GameConfig.PAYLINES.forEachIndexed { index, cells ->
            val symbol = lineSymbol(cells.map { grid[it] })
            if (symbol != null && (index < GameConfig.HORIZONTAL_LINES || symbol.isHighValue)) {
                wins.add(PaylineWin(index, cells, symbol, bet * symbol.payout))
            }
        }
        val markers = grid.count { it.isScatter }
        val feathers = grid.count { it == SlotSymbol.FEATHER }
        val total = wins.sumOf { it.amount }
        val nearReel = if (total == 0) nearWinReel(grid, markers) else -1
        return SpinResult(
            grid = grid,
            wins = wins,
            totalWin = total,
            markerCount = markers,
            featherCount = feathers,
            nearWin = total == 0 && nearReel >= 0,
            nearWinReel = nearReel,
            bonusTriggered = markers >= GameConfig.MARKERS_FOR_BONUS
        )
    }

    private fun lineSymbol(symbols: List<SlotSymbol>): SlotSymbol? {
        if (symbols.any { it.isScatter }) {
            return null
        }
        if (symbols.all { it.isWild }) {
            return SlotSymbol.CHIKO
        }
        val base = symbols.firstOrNull { !it.isWild } ?: return null
        return if (symbols.all { it == base || it.isWild }) base else null
    }

    private fun nearWinReel(grid: List<SlotSymbol>, markers: Int): Int {
        if (markers == 2) {
            val cell = grid.indexOfLast { it.isScatter }
            return if (cell >= 0) cell % GameConfig.REELS else 0
        }
        GameConfig.PAYLINES.forEach { cells ->
            val symbols = cells.map { grid[it] }
            val high = symbols.count { it.isHighValue }
            if (high == 2) {
                val oddIndex = symbols.indexOfFirst { !it.isHighValue }
                if (oddIndex >= 0) {
                    return cells[oddIndex] % GameConfig.REELS
                }
            }
        }
        return -1
    }
}
