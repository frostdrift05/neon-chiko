package com.Meztido0.Dewgov.presentation.common

import androidx.compose.ui.graphics.Color
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.domain.model.BonusOutcomeType
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import com.Meztido0.Dewgov.domain.model.SpriteKind
import com.Meztido0.Dewgov.domain.model.Volatility
import java.util.Locale

fun symbolDrawable(symbol: SlotSymbol): Int = when (symbol) {
    SlotSymbol.CHIKO -> R.drawable.sprite_1
    SlotSymbol.ROAD_SIGN -> R.drawable.sprite_2
    SlotSymbol.VAN -> R.drawable.sprite_3
    SlotSymbol.FEATHER -> R.drawable.sprite_4
    SlotSymbol.MARKER -> R.drawable.sprite_5
}

fun spriteDrawable(kind: SpriteKind): Int = when (kind) {
    SpriteKind.CHIKO -> R.drawable.sprite_1
    SpriteKind.ROAD_SIGN -> R.drawable.sprite_2
    SpriteKind.VAN -> R.drawable.sprite_3
    SpriteKind.FEATHER -> R.drawable.sprite_4
    SpriteKind.MARKER -> R.drawable.sprite_5
}

fun symbolNameRes(symbol: SlotSymbol): Int = when (symbol) {
    SlotSymbol.CHIKO -> R.string.symbol_chiko
    SlotSymbol.ROAD_SIGN -> R.string.symbol_road_sign
    SlotSymbol.VAN -> R.string.symbol_van
    SlotSymbol.FEATHER -> R.string.symbol_feather
    SlotSymbol.MARKER -> R.string.symbol_marker
}

fun symbolRoleRes(symbol: SlotSymbol): Int = when (symbol) {
    SlotSymbol.CHIKO -> R.string.symbol_role_wild
    SlotSymbol.MARKER -> R.string.symbol_role_scatter
    SlotSymbol.VAN -> R.string.symbol_role_mid
    else -> R.string.symbol_role_low
}

fun symbolAccent(symbol: SlotSymbol): Color = when (symbol) {
    SlotSymbol.CHIKO -> NeonMagenta
    SlotSymbol.MARKER -> StreetAmber
    SlotSymbol.VAN -> ChromeCyan
    SlotSymbol.ROAD_SIGN -> RoadYellow
    SlotSymbol.FEATHER -> TextSecondary
}

fun volatilityRes(volatility: Volatility): Int = when (volatility) {
    Volatility.LOW -> R.string.volatility_low
    Volatility.MEDIUM -> R.string.volatility_medium
    Volatility.HIGH -> R.string.volatility_high
}

fun outcomeTitleRes(type: BonusOutcomeType): Int = when (type) {
    BonusOutcomeType.CREDITS -> R.string.bonus_outcome_credits
    BonusOutcomeType.FEATHER_BUNDLE -> R.string.bonus_outcome_feathers
    BonusOutcomeType.ROUTE_BADGE -> R.string.bonus_outcome_badge
    BonusOutcomeType.SKIN_SHARD -> R.string.bonus_outcome_shard
    BonusOutcomeType.ROADBLOCK -> R.string.bonus_outcome_roadblock
}

fun outcomeAccent(type: BonusOutcomeType): Color = when (type) {
    BonusOutcomeType.CREDITS -> RoadYellow
    BonusOutcomeType.FEATHER_BUNDLE -> ChromeCyan
    BonusOutcomeType.ROUTE_BADGE -> NeonMagenta
    BonusOutcomeType.SKIN_SHARD -> StreetAmber
    BonusOutcomeType.ROADBLOCK -> TextSecondary
}

fun grouped(value: Int): String = String.format(Locale.US, "%,d", value)

fun signed(value: Int): String =
    if (value >= 0) String.format(Locale.US, "+%,d", value) else String.format(Locale.US, "%,d", value)

fun clock(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
}
