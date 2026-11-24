package com.Meztido0.Dewgov.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.navigation.Screen
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.presentation.bonus.BonusRoadScreen
import com.Meztido0.Dewgov.presentation.collection.CollectionScreen
import com.Meztido0.Dewgov.presentation.game.GameScreen
import com.Meztido0.Dewgov.presentation.gameover.GameOverScreen
import com.Meztido0.Dewgov.presentation.lobby.SlotLobbyScreen
import com.Meztido0.Dewgov.presentation.menu.MenuScreen
import com.Meztido0.Dewgov.presentation.missions.MissionsScreen
import com.Meztido0.Dewgov.presentation.responsible.ResponsiblePlayScreen
import com.Meztido0.Dewgov.presentation.rewards.RewardsScreen
import com.Meztido0.Dewgov.presentation.routemap.RouteMapScreen
import com.Meztido0.Dewgov.presentation.settings.SettingsScreen
import com.Meztido0.Dewgov.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()

    BackHandler(enabled = backStack.isNotEmpty()) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(animationSpec = tween(320)) togetherWith fadeOut(animationSpec = tween(220))
        },
        modifier = Modifier
            .fillMaxSize()
            .background(AsphaltBlack),
        label = "screenHost"
    ) { current ->
        when (current) {
            is Screen.Splash -> SplashScreen(onFinished = appViewModel::finishSplash)
            is Screen.Menu -> MenuScreen(
                onPlay = { appViewModel.navigate(Screen.Game) },
                onLobby = { appViewModel.navigate(Screen.SlotLobby) },
                onBonusRoad = { appViewModel.navigate(Screen.BonusRoad(practice = true)) },
                onRouteMap = { appViewModel.navigate(Screen.RouteMap) },
                onCollection = { appViewModel.navigate(Screen.Collection) },
                onMissions = { appViewModel.navigate(Screen.Missions) },
                onRewards = { appViewModel.navigate(Screen.Rewards) },
                onResponsiblePlay = { appViewModel.navigate(Screen.ResponsiblePlay) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )
            is Screen.SlotLobby -> SlotLobbyScreen(
                onPlay = { appViewModel.navigateFromRun(Screen.Game) },
                onBack = appViewModel::back
            )
            is Screen.Game -> GameScreen(
                onMenu = appViewModel::goMenu,
                onCollection = { appViewModel.navigate(Screen.Collection) },
                onBonusRoad = { appViewModel.navigate(Screen.BonusRoad(practice = false)) },
                onRunFinished = { summary -> appViewModel.navigateFromRun(Screen.GameOver(summary)) }
            )
            is Screen.BonusRoad -> BonusRoadScreen(
                practice = current.practice,
                onContinue = appViewModel::back,
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.RouteMap -> RouteMapScreen(
                onOpenLobby = { appViewModel.navigate(Screen.SlotLobby) },
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.Collection -> CollectionScreen(
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.Missions -> MissionsScreen(
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.Rewards -> RewardsScreen(
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.ResponsiblePlay -> ResponsiblePlayScreen(
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.Settings -> SettingsScreen(
                onMenu = appViewModel::goMenu,
                onBack = appViewModel::back
            )
            is Screen.GameOver -> GameOverScreen(
                summary = current.summary,
                onPlayAgain = { appViewModel.navigateFromRun(Screen.Game) },
                onMenu = appViewModel::goMenu,
                onCollection = { appViewModel.navigate(Screen.Collection) }
            )
        }
    }
}
