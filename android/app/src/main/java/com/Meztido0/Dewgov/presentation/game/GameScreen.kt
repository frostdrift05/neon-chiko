package com.Meztido0.Dewgov.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.ChromeLight
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceElevated
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.NeonProgressBar
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.ReelCell
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.StatCardData
import com.Meztido0.Dewgov.core.ui.StatCardRow
import com.Meztido0.Dewgov.domain.model.RunSummary
import com.Meztido0.Dewgov.presentation.common.grouped
import com.Meztido0.Dewgov.presentation.dialog.PauseDialog
import com.Meztido0.Dewgov.presentation.dialog.PayoutTableDialog
import kotlin.math.floor

@Composable
fun GameScreen(
    onMenu: () -> Unit,
    onCollection: () -> Unit,
    onBonusRoad: () -> Unit,
    onRunFinished: (RunSummary) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.OpenBonusRoad -> onBonusRoad()
                is GameEvent.FinishRun -> onRunFinished(event.summary)
            }
        }
    }

    DisposableEffect(Unit) {
        gameViewModel.resume()
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onBack = onMenu,
        onSpin = gameViewModel::spin,
        onEndRun = gameViewModel::endRunNow,
        onCollection = onCollection,
        onShowPayout = { gameViewModel.togglePayoutTable(true) },
        onHidePayout = { gameViewModel.togglePayoutTable(false) },
        onShowPause = { gameViewModel.togglePause(true) },
        onHidePause = { gameViewModel.togglePause(false) },
        onMenu = onMenu
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onBack: () -> Unit,
    onSpin: () -> Unit,
    onEndRun: () -> Unit,
    onCollection: () -> Unit,
    onShowPayout: () -> Unit,
    onHidePayout: () -> Unit,
    onShowPause: () -> Unit,
    onHidePause: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenScaffold(
        titleRes = state.machineNameRes,
        onBack = onBack,
        backgroundAlpha = 0.45f,
        trailing = {
            IconButton(onClick = onShowPause, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(
                    R.string.game_spin_counter,
                    (state.spinIndex + 1).coerceAtMost(state.totalSpins),
                    state.totalSpins
                ),
                style = MaterialTheme.typography.labelSmall,
                color = ChromeCyan
            )
            Text(
                text = stringResource(state.perkRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                maxLines = 1
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    WinBanner(state = state)
                }
                Spacer(modifier = Modifier.height(8.dp))
                ReelCabinet(
                    state = state,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.height(14.dp))
                LaneBoostMeter(state = state)
            }
        }
        StatCardRow(
            items = statItems(state),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        ControlsBlock(
            state = state,
            onSpin = onSpin,
            onEndRun = onEndRun,
            onCollection = onCollection,
            onShowPayout = onShowPayout
        )
    }
    if (state.showPayoutTable) {
        PayoutTableDialog(bet = state.bet, onDismiss = onHidePayout)
    }
    if (state.showPause) {
        PauseDialog(
            onResume = onHidePause,
            onEndRun = onEndRun,
            onMenu = {
                onHidePause()
                onMenu()
            }
        )
    }
}

@Composable
private fun statItems(state: GameUiState): List<StatCardData> {
    val items = mutableListOf<StatCardData>()
    items.add(
        StatCardData(
            labelRes = R.string.stat_credits,
            value = grouped(state.credits),
            accent = RoadYellow
        )
    )
    items.add(
        StatCardData(
            labelRes = R.string.stat_bet,
            value = grouped(state.bet),
            accent = ChromeCyan
        )
    )
    if (state.runWin > 0) {
        items.add(
            StatCardData(
                labelRes = R.string.stat_run_win,
                value = grouped(state.runWin),
                accent = NeonMagenta
            )
        )
    }
    return items
}

@Composable
private fun ReelCabinet(state: GameUiState, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(22.dp)
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val frame = 13.dp
        val widthCap = if (maxWidth < 380.dp) maxWidth else 380.dp
        val heightCap = if (maxHeight < 392.dp) maxHeight else 392.dp
        val cellByWidth = (widthCap - frame * 2) / GameConfig.REELS
        val cellByHeight = (heightCap - frame * 2) / GameConfig.ROWS
        val rawCell = if (cellByWidth < cellByHeight) cellByWidth else cellByHeight
        val cell = floor(rawCell.value).dp.coerceAtLeast(48.dp)
        val boardWidth = cell * GameConfig.REELS + frame * 2
        Box(
            modifier = Modifier
                .width(boardWidth)
                .align(Alignment.Center)
                .shadow(24.dp, shape, ambientColor = NeonMagenta, spotColor = ChromeCyan)
                .background(SurfaceNight, shape)
                .border(
                    3.dp,
                    Brush.verticalGradient(listOf(ChromeLight, TextMuted)),
                    shape
                )
                .padding(10.dp)
        ) {
            Column {
                for (row in 0 until GameConfig.ROWS) {
                    Row {
                        for (col in 0 until GameConfig.REELS) {
                            val index = row * GameConfig.REELS + col
                            ReelCell(
                                symbol = state.grid[index],
                                cellSize = cell,
                                winning = state.winningCells.contains(index),
                                nearMiss = state.nearWin && state.nearWinReel == col,
                                spinning = col >= state.stoppedReels
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WinBanner(state: GameUiState) {
    AnimatedVisibility(
        visible = state.bannerWin > 0 || state.nearWin,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = fadeOut()
    ) {
        val amber = state.bannerWin == 0
        Box(
            modifier = Modifier
                .background(
                    if (amber) {
                        StreetAmber.copy(alpha = 0.18f)
                    } else {
                        NeonMagenta.copy(alpha = 0.2f)
                    },
                    RoundedCornerShape(14.dp)
                )
                .border(
                    1.dp,
                    if (amber) StreetAmber else NeonMagenta,
                    RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = if (amber) {
                    stringResource(R.string.game_near_win)
                } else {
                    stringResource(
                        R.string.game_win_banner,
                        grouped(state.bannerWin),
                        stringResource(state.bannerSymbolRes),
                        state.bannerMultiplier
                    )
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (amber) StreetAmber else TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun LaneBoostMeter(state: GameUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = if (state.bonusReady) {
                stringResource(R.string.game_bonus_ready)
            } else {
                stringResource(R.string.game_lane_boost, state.laneBoost, state.laneBoostMax)
            },
            style = MaterialTheme.typography.labelSmall,
            color = if (state.bonusReady) NeonMagenta else ChromeCyan
        )
        Spacer(modifier = Modifier.height(6.dp))
        NeonProgressBar(
            progress = state.laneBoost.toFloat() / state.laneBoostMax,
            accent = if (state.bonusReady) NeonMagenta else ChromeCyan,
            animated = !state.reducedMotion
        )
    }
}

@Composable
private fun ControlsBlock(
    state: GameUiState,
    onSpin: () -> Unit,
    onEndRun: () -> Unit,
    onCollection: () -> Unit,
    onShowPayout: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceNight.copy(alpha = 0.94f), shape)
            .border(1.dp, ChromeCyan.copy(alpha = 0.3f), shape)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        PrimaryButton(
            textRes = R.string.action_spin,
            onClick = onSpin,
            icon = Icons.Filled.PlayArrow,
            enabled = state.spinEnabled,
            height = 64.dp,
            stateDescriptionRes = if (state.phase == GamePhase.IDLE) {
                null
            } else {
                R.string.state_spinning
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                textRes = R.string.action_end_run,
                onClick = onEndRun,
                icon = Icons.Filled.Star,
                accent = NeonMagenta,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                textRes = R.string.action_collection,
                onClick = onCollection,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.game_bet_line, state.bet),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            IconButton(onClick = onShowPayout, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.cd_payout_table),
                    tint = ChromeCyan,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
