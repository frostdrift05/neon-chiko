package com.Meztido0.Dewgov.core.navigation

import com.Meztido0.Dewgov.domain.model.RunSummary

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object SlotLobby : Screen
    data object Game : Screen
    data class BonusRoad(val practice: Boolean) : Screen
    data object RouteMap : Screen
    data object Collection : Screen
    data object Missions : Screen
    data object Rewards : Screen
    data object ResponsiblePlay : Screen
    data object Settings : Screen
    data class GameOver(val summary: RunSummary) : Screen
}
