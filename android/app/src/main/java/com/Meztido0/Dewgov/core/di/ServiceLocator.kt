package com.Meztido0.Dewgov.core.di

import android.content.Context
import com.Meztido0.Dewgov.data.local.PreferencesStore
import com.Meztido0.Dewgov.data.repository.MissionRepositoryImpl
import com.Meztido0.Dewgov.data.repository.ProfileRepositoryImpl
import com.Meztido0.Dewgov.data.repository.SettingsRepositoryImpl
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ApplyRunRewardsUseCase
import com.Meztido0.Dewgov.domain.usecase.ComputeRankUseCase
import com.Meztido0.Dewgov.domain.usecase.EvaluateSpinUseCase
import com.Meztido0.Dewgov.domain.usecase.RefreshDailyMissionsUseCase
import com.Meztido0.Dewgov.domain.usecase.ResolveBonusJunctionUseCase
import com.Meztido0.Dewgov.domain.usecase.SpinReelsUseCase

object ServiceLocator {

    private var container: AppContainer? = null

    fun init(context: Context) {
        if (container == null) {
            container = AppContainer(context.applicationContext)
        }
    }

    val viewModelFactory: AppViewModelFactory
        get() = AppViewModelFactory(requireContainer())

    internal fun requireContainer(): AppContainer =
        checkNotNull(container) { CONTAINER_ERROR }

    private const val CONTAINER_ERROR = "ServiceLocator.init must run before the first screen"
}

class AppContainer(context: Context) {

    private val store = PreferencesStore(context)

    val profileRepository: ProfileRepository = ProfileRepositoryImpl(store)
    val settingsRepository: SettingsRepository = SettingsRepositoryImpl(store)
    val missionRepository: MissionRepository =
        MissionRepositoryImpl(store, RefreshDailyMissionsUseCase())

    val runSession = RunSession()

    val spinReels = SpinReelsUseCase()
    val evaluateSpin = EvaluateSpinUseCase()
    val resolveJunction = ResolveBonusJunctionUseCase()
    val applyRunRewards = ApplyRunRewardsUseCase()
    val computeRank = ComputeRankUseCase()

    fun clearLocalData() {
        store.clearAll()
        profileRepository.resetProgress()
        settingsRepository.resetDefaults()
        missionRepository.resetProgress()
        runSession.clear()
    }
}
