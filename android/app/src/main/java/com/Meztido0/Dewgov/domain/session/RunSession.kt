package com.Meztido0.Dewgov.domain.session

import com.Meztido0.Dewgov.core.config.GameConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RunState(
    val active: Boolean = false,
    val machineId: String = "diner",
    val spinsPlanned: Int = GameConfig.DEFAULT_RUN_SPINS,
    val spinsPlayed: Int = 0,
    val staked: Int = 0,
    val grossWin: Int = 0,
    val routePoints: Int = 0,
    val badgesEarned: Int = 0,
    val bonusRoutes: Int = 0,
    val feathersLanded: Int = 0,
    val shardsEarned: Int = 0,
    val unlockedSomething: Boolean = false,
    val pendingBonus: Boolean = false
) {
    val netWin: Int
        get() = grossWin - staked

    val finished: Boolean
        get() = spinsPlayed >= spinsPlanned
}

class RunSession {

    private val _state = MutableStateFlow(RunState())
    val state: StateFlow<RunState> = _state.asStateFlow()

    fun start(machineId: String, spins: Int) {
        _state.value = RunState(active = true, machineId = machineId, spinsPlanned = spins)
    }

    fun recordSpin(bet: Int, win: Int, feathers: Int, routePoints: Int) {
        _state.update {
            it.copy(
                spinsPlayed = it.spinsPlayed + 1,
                staked = it.staked + bet,
                grossWin = it.grossWin + win,
                feathersLanded = it.feathersLanded + feathers,
                routePoints = it.routePoints + routePoints
            )
        }
    }

    fun recordBonusRoute(credits: Int, routePoints: Int, badges: Int, shards: Int) {
        _state.update {
            it.copy(
                grossWin = it.grossWin + credits,
                routePoints = it.routePoints + routePoints,
                badgesEarned = it.badgesEarned + badges,
                bonusRoutes = it.bonusRoutes + 1,
                shardsEarned = it.shardsEarned + shards,
                unlockedSomething = it.unlockedSomething || badges > 0 || shards > 0,
                pendingBonus = false
            )
        }
    }

    fun markBonusPending() {
        _state.update { it.copy(pendingBonus = true) }
    }

    fun recordBadges(count: Int) {
        if (count <= 0) {
            return
        }
        _state.update {
            it.copy(badgesEarned = it.badgesEarned + count, unlockedSomething = true)
        }
    }

    fun markUnlock() {
        _state.update { it.copy(unlockedSomething = true) }
    }

    fun close() {
        _state.update { it.copy(active = false) }
    }

    fun clear() {
        _state.value = RunState()
    }
}
