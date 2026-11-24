package com.Meztido0.Dewgov.domain.model

enum class SlotSymbol(val weight: Int, val payout: Int) {
    FEATHER(30, 2),
    ROAD_SIGN(26, 4),
    VAN(22, 8),
    MARKER(12, 0),
    CHIKO(10, 25);

    val isWild: Boolean
        get() = this == CHIKO

    val isScatter: Boolean
        get() = this == MARKER

    val isHighValue: Boolean
        get() = this == VAN || this == CHIKO
}
