package com.Meztido0.Dewgov.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.Meztido0.Dewgov.presentation.app.AppViewModel
import com.Meztido0.Dewgov.presentation.bonus.BonusRoadViewModel
import com.Meztido0.Dewgov.presentation.collection.CollectionViewModel
import com.Meztido0.Dewgov.presentation.game.GameViewModel
import com.Meztido0.Dewgov.presentation.gameover.GameOverViewModel
import com.Meztido0.Dewgov.presentation.lobby.SlotLobbyViewModel
import com.Meztido0.Dewgov.presentation.menu.MenuViewModel
import com.Meztido0.Dewgov.presentation.missions.MissionsViewModel
import com.Meztido0.Dewgov.presentation.responsible.ResponsiblePlayViewModel
import com.Meztido0.Dewgov.presentation.rewards.RewardsViewModel
import com.Meztido0.Dewgov.presentation.routemap.RouteMapViewModel
import com.Meztido0.Dewgov.presentation.settings.SettingsViewModel
import com.Meztido0.Dewgov.presentation.splash.SplashViewModel

class AppViewModelFactory(
    container: AppContainer
) : ViewModelProvider.Factory by buildAppFactory(container)

private fun buildAppFactory(container: AppContainer): ViewModelProvider.Factory =
    viewModelFactory {
        initializer { AppViewModel() }
        initializer { SplashViewModel() }
        initializer {
            MenuViewModel(
                container.profileRepository,
                container.missionRepository,
                container.settingsRepository,
                container.runSession,
                container.computeRank
            )
        }
        initializer {
            SlotLobbyViewModel(
                container.profileRepository,
                container.settingsRepository,
                container.runSession
            )
        }
        initializer {
            GameViewModel(
                container.profileRepository,
                container.settingsRepository,
                container.missionRepository,
                container.runSession,
                container.spinReels,
                container.evaluateSpin,
                container.applyRunRewards
            )
        }
        initializer {
            BonusRoadViewModel(
                container.profileRepository,
                container.settingsRepository,
                container.missionRepository,
                container.runSession,
                container.resolveJunction,
                container.applyRunRewards
            )
        }
        initializer { RouteMapViewModel(container.profileRepository) }
        initializer {
            CollectionViewModel(
                container.profileRepository,
                container.runSession,
                container.applyRunRewards
            )
        }
        initializer {
            MissionsViewModel(
                container.missionRepository,
                container.profileRepository,
                container.runSession,
                container.applyRunRewards
            )
        }
        initializer { RewardsViewModel(container.profileRepository, container.computeRank) }
        initializer {
            ResponsiblePlayViewModel(
                container.profileRepository,
                container.settingsRepository
            )
        }
        initializer {
            SettingsViewModel(
                container.settingsRepository,
                container.profileRepository,
                container.missionRepository,
                container.runSession
            )
        }
        initializer {
            GameOverViewModel(
                container.profileRepository,
                container.settingsRepository,
                container.runSession,
                container.applyRunRewards
            )
        }
    }
