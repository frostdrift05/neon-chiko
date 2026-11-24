package com.Meztido0.Dewgov.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.MissionGoalType
import com.Meztido0.Dewgov.domain.model.PlayerProfile
import com.Meztido0.Dewgov.domain.model.RunSummary
import com.Meztido0.Dewgov.domain.model.SkinPerk
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import com.Meztido0.Dewgov.domain.model.SpinResult
import com.Meztido0.Dewgov.domain.repository.MissionRepository
import com.Meztido0.Dewgov.domain.repository.ProfileRepository
import com.Meztido0.Dewgov.domain.repository.SettingsRepository
import com.Meztido0.Dewgov.domain.session.RunSession
import com.Meztido0.Dewgov.domain.usecase.ApplyRunRewardsUseCase
import com.Meztido0.Dewgov.domain.usecase.EvaluateSpinUseCase
import com.Meztido0.Dewgov.domain.usecase.SpinReelsUseCase
import com.Meztido0.Dewgov.presentation.common.symbolNameRes
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val missionRepository: MissionRepository,
    private val runSession: RunSession,
    private val spinReels: SpinReelsUseCase,
    private val evaluateSpin: EvaluateSpinUseCase,
    private val applyRunRewards: ApplyRunRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var spinJob: Job? = null
    private var random = Random(System.currentTimeMillis())

    init {
        viewModelScope.launch {
            combine(
                profileRepository.profile,
                settingsRepository.settings,
                runSession.state
            ) { profile, settings, run ->
                Triple(profile, settings, run)
            }.collect { triple ->
                val machine = SampleData.machineOf(triple.third.machineId)
                val skin = SampleData.skinOf(triple.first.equippedSkin)
                _state.update {
                    it.copy(
                        credits = triple.first.credits,
                        laneBoost = triple.first.laneBoost,
                        runWin = triple.third.grossWin,
                        spinIndex = triple.third.spinsPlayed,
                        totalSpins = triple.third.spinsPlanned,
                        machineNameRes = machine.nameRes,
                        perkRes = skin.perkRes,
                        reducedMotion = triple.second.reducedMotion,
                        bonusReady = triple.first.laneBoost >= GameConfig.LANE_BOOST_MAX
                    )
                }
            }
        }
        if (settingsRepository.settings.value.payoutTableOnStart) {
            _state.update { it.copy(showPayoutTable = true) }
        }
    }

    fun spin() {
        if (_state.value.phase != GamePhase.IDLE) {
            return
        }
        val profile = profileRepository.profile.value
        if (profile.credits < GameConfig.BET) {
            finishRun()
            return
        }
        val settings = settingsRepository.settings.value
        val run = runSession.state.value
        val machine = SampleData.machineOf(run.machineId)
        val skin = SampleData.skinOf(profile.equippedSkin)
        val firstFeatherWild = skin.perk == SkinPerk.FIRST_FEATHER_WILD && run.spinsPlayed == 0
        random = Random(run.machineId.hashCode() * 7919L xor ((run.spinsPlayed + 1) * 104729L))
        val target = spinReels(
            machine = machine,
            lossStreak = profile.lossStreak,
            bigWinCooldown = profile.bigWinCooldown,
            firstFeatherWild = firstFeatherWild,
            random = random
        )
        val result = evaluateSpin(target, GameConfig.BET)
        profileRepository.update {
            it.copy(credits = it.credits - GameConfig.BET, spinsToday = it.spinsToday + 1)
        }
        _state.update {
            it.copy(
                phase = GamePhase.SPINNING,
                stoppedReels = 0,
                winningCells = emptySet(),
                bannerWin = 0,
                bannerMultiplier = 0,
                nearWin = false,
                nearWinReel = -1
            )
        }
        spinJob = viewModelScope.launch {
            val stops = settings.reelSpeed.stops
            val started = System.currentTimeMillis()
            while (true) {
                val elapsed = System.currentTimeMillis() - started
                val stopped = stops.count { elapsed >= it }
                val shown = List(GameConfig.CELLS) { index ->
                    if (index % GameConfig.REELS < stopped) {
                        target[index]
                    } else {
                        spinReels.randomSymbol(random)
                    }
                }
                _state.update { it.copy(grid = shown, stoppedReels = stopped) }
                if (stopped >= GameConfig.REELS) {
                    break
                }
                delay(GameConfig.REEL_TICK_MS)
            }
            delay(GameConfig.RESOLVE_DELAY_MS)
            resolve(result)
        }
    }

    fun togglePayoutTable(show: Boolean) {
        _state.update { it.copy(showPayoutTable = show) }
    }

    fun togglePause(show: Boolean) {
        _state.update { it.copy(showPause = show) }
    }

    fun endRunNow() {
        spinJob?.cancel()
        _state.update { it.copy(phase = GamePhase.IDLE, showPause = false) }
        finishRun()
    }

    fun resume() {
        val run = runSession.state.value
        if (!run.active) {
            return
        }
        if (run.finished || profileRepository.profile.value.credits < GameConfig.BET) {
            finishRun()
        }
    }

    fun pause() {
        spinJob?.cancel()
        spinJob = null
        if (_state.value.phase != GamePhase.IDLE) {
            _state.update { it.copy(phase = GamePhase.IDLE) }
        }
    }

    override fun onCleared() {
        spinJob?.cancel()
        super.onCleared()
    }

    private suspend fun resolve(result: SpinResult) {
        val settings = settingsRepository.settings.value
        val skin = SampleData.skinOf(profileRepository.profile.value.equippedSkin)
        val win = result.totalWin
        val bigWin = win >= GameConfig.BET * GameConfig.BIG_WIN_MULTIPLIER
        val showNearWin = result.nearWin && settings.nearWinHighlight
        var boostGain = 0
        if (win > 0) {
            boostGain += 2
        }
        if (result.nearWin) {
            boostGain += 1
        }
        if (result.markerCount == 2) {
            boostGain += 3
        }
        if (skin.perk == SkinPerk.EXTRA_LANE_BOOST) {
            boostGain += 1
        }
        val routePoints = GameConfig.RP_PER_SPIN + if (win > 0) GameConfig.RP_PER_WIN else 0
        val badgesBefore = profileRepository.profile.value.badges.size
        profileRepository.update { current ->
            val advanced = current.copy(
                credits = current.credits + win,
                routePoints = current.routePoints + routePoints,
                laneBoost = (current.laneBoost + boostGain).coerceAtMost(GameConfig.LANE_BOOST_MAX),
                lossStreak = if (win > 0) 0 else current.lossStreak + 1,
                bigWinCooldown = when {
                    bigWin -> GameConfig.BIG_WIN_COOLDOWN_SPINS
                    current.bigWinCooldown > 0 -> current.bigWinCooldown - 1
                    else -> 0
                }
            )
            awardSpinBadges(advanced, result)
        }
        runSession.recordBadges(profileRepository.profile.value.badges.size - badgesBefore)
        missionRepository.advance(MissionGoalType.SPINS, 1)
        missionRepository.advance(MissionGoalType.FEATHERS, result.featherCount)
        runSession.recordSpin(GameConfig.BET, win, result.featherCount, routePoints)

        val top = result.topWin
        _state.update { current ->
            current.copy(
                phase = GamePhase.RESOLVING,
                grid = result.grid,
                stoppedReels = GameConfig.REELS,
                winningCells = result.winningCells,
                bannerWin = win,
                bannerSymbolRes = if (top != null) symbolNameRes(top.symbol) else current.bannerSymbolRes,
                bannerMultiplier = top?.symbol?.payout ?: 0,
                nearWin = showNearWin,
                nearWinReel = if (showNearWin) result.nearWinReel else -1
            )
        }
        delay(if (win > 0) GameConfig.BANNER_HOLD_MS else GameConfig.NEAR_WIN_HOLD_MS)

        val bonus = result.bonusTriggered ||
            profileRepository.profile.value.laneBoost >= GameConfig.LANE_BOOST_MAX
        _state.update {
            it.copy(
                phase = GamePhase.IDLE,
                bannerWin = 0,
                nearWin = false,
                nearWinReel = -1
            )
        }
        if (bonus) {
            profileRepository.update { it.copy(laneBoost = 0) }
            runSession.markBonusPending()
            eventChannel.send(GameEvent.OpenBonusRoad)
            return
        }
        if (runSession.state.value.finished ||
            profileRepository.profile.value.credits < GameConfig.BET
        ) {
            finishRun()
        }
    }

    private fun awardSpinBadges(profile: PlayerProfile, result: SpinResult): PlayerProfile {
        var next = profile
        if (result.totalWin > 0) {
            next = applyRunRewards.awardBadge(next, "first_feather")
        }
        if (result.wins.any { it.symbol == SlotSymbol.VAN }) {
            next = applyRunRewards.awardBadge(next, "triple_van")
        }
        if (result.wins.any { it.symbol == SlotSymbol.CHIKO }) {
            next = applyRunRewards.awardBadge(next, "wild_streak")
        }
        if (result.markerCount >= GameConfig.MARKERS_FOR_BONUS) {
            next = applyRunRewards.awardBadge(next, "marker_hunter")
        }
        if (next.laneBoost >= GameConfig.LANE_BOOST_MAX) {
            next = applyRunRewards.awardBadge(next, "lane_master")
        }
        if (next.spinsToday >= 20) {
            next = applyRunRewards.awardBadge(next, "feather_storm")
        }
        if (next.badges.size >= 4) {
            next = applyRunRewards.awardBadge(next, "overpass_key")
        }
        if (next.badges.size >= 12) {
            next = applyRunRewards.awardBadge(next, "terminal_pass")
        }
        return next
    }

    private fun finishRun() {
        val run = runSession.state.value
        if (run.spinsPlayed > 0) {
            profileRepository.update { applyRunRewards.awardBadge(it, "night_courier") }
        }
        val profile = profileRepository.profile.value
        val outcome = applyRunRewards.outcome(run, profile.credits, GameConfig.BET)
        val missions = missionRepository.missions.value
        val mission = missions.firstOrNull { !it.claimed } ?: missions.first()
        val delta = when (mission.mission.type) {
            MissionGoalType.SPINS -> run.spinsPlayed
            MissionGoalType.FEATHERS -> run.feathersLanded
            MissionGoalType.BONUS_ROUTES -> run.bonusRoutes
        }
        val summary = RunSummary(
            machineId = run.machineId,
            outcome = outcome,
            spins = run.spinsPlayed,
            netWin = run.netWin,
            routePoints = run.routePoints,
            badgesEarned = run.badgesEarned,
            bonusRoutes = run.bonusRoutes,
            laneBoost = profile.laneBoost,
            creditsLeft = profile.credits,
            missionNameRes = mission.mission.nameRes,
            missionProgress = mission.capped,
            missionGoal = mission.mission.goal,
            missionDelta = delta,
            unlockedSomething = run.unlockedSomething
        )
        runSession.close()
        viewModelScope.launch {
            eventChannel.send(GameEvent.FinishRun(summary))
        }
    }
}
