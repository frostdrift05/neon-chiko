package com.Meztido0.Dewgov.domain.model

data class PaylineWin(
    val lineIndex: Int,
    val cells: List<Int>,
    val symbol: SlotSymbol,
    val amount: Int
)

data class SpinResult(
    val grid: List<SlotSymbol>,
    val wins: List<PaylineWin>,
    val totalWin: Int,
    val markerCount: Int,
    val featherCount: Int,
    val nearWin: Boolean,
    val nearWinReel: Int,
    val bonusTriggered: Boolean
) {
    val winningCells: Set<Int>
        get() = wins.flatMap { it.cells }.toSet()

    val topWin: PaylineWin?
        get() = wins.maxByOrNull { it.amount }
}
