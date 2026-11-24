package com.Meztido0.Dewgov.presentation.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.ChromeRing
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.NeonProgressBar
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.StatCardData
import com.Meztido0.Dewgov.core.ui.StatCardRow
import com.Meztido0.Dewgov.domain.model.RunOutcome
import com.Meztido0.Dewgov.domain.model.RunSummary
import com.Meztido0.Dewgov.presentation.common.grouped
import com.Meztido0.Dewgov.presentation.common.signed

@Composable
fun GameOverScreen(
    summary: RunSummary,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onCollection: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    GameOverContent(
        summary = summary,
        state = state,
        onPlayAgain = {
            if (state.needsRefill) {
                gameOverViewModel.refillAndStartAgain()
            } else {
                gameOverViewModel.startAgain()
            }
            onPlayAgain()
        },
        onMenu = onMenu,
        onCollection = onCollection
    )
}

@Composable
private fun GameOverContent(
    summary: RunSummary,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onCollection: () -> Unit
) {
    val headlineRes = when (summary.outcome) {
        RunOutcome.PROFIT -> R.string.result_route_complete
        RunOutcome.LOSS -> R.string.result_rough_shift
        RunOutcome.OUT_OF_CREDITS -> R.string.result_out_of_credits
    }
    val accent = when (summary.outcome) {
        RunOutcome.PROFIT -> NeonMagenta
        RunOutcome.LOSS -> ChromeCyan
        RunOutcome.OUT_OF_CREDITS -> StreetAmber
    }
    ScreenScaffold(
        titleRes = R.string.result_title,
        backgroundAlpha = 0.4f,
        leading = {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ChromeRing(size = 148.dp, glow = accent, glowElevation = 26.dp) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_equipped_skin),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        Color(state.skinTint).copy(alpha = 0.35f),
                        androidx.compose.ui.graphics.BlendMode.Overlay
                    ),
                    modifier = Modifier.size(104.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(headlineRes),
                style = MaterialTheme.typography.headlineMedium,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            ResultStats(summary = summary, state = state)
            Spacer(modifier = Modifier.height(14.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(
                        R.string.result_mission_line,
                        stringResource(summary.missionNameRes),
                        summary.missionProgress,
                        summary.missionGoal,
                        summary.missionDelta
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonProgressBar(
                    progress = summary.missionProgress.toFloat() / summary.missionGoal,
                    accent = ChromeCyan,
                    barHeight = 6.dp,
                    animated = !state.reducedMotion
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(
                        R.string.result_lane_carry,
                        summary.laneBoost
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryButton(
                textRes = if (state.needsRefill) {
                    R.string.action_refill_and_again
                } else {
                    R.string.action_spin_again
                },
                onClick = onPlayAgain,
                icon = Icons.Filled.Refresh,
                height = 64.dp,
                modifier = Modifier.fillMaxWidth()
            )
            if (summary.unlockedSomething) {
                SecondaryButton(
                    textRes = R.string.action_collection,
                    onClick = onCollection,
                    accent = RoadYellow,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            SecondaryButton(
                textRes = R.string.action_menu,
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResultStats(summary: RunSummary, state: GameOverUiState) {
    val items = mutableListOf<StatCardData>()
    if (summary.netWin != 0) {
        items.add(
            StatCardData(R.string.stat_run_win, signed(summary.netWin), NeonMagenta)
        )
    }
    items.add(StatCardData(R.string.stat_spins, summary.spins.toString(), ChromeCyan))
    if (summary.routePoints > 0) {
        items.add(
            StatCardData(R.string.stat_rp_earned, signed(summary.routePoints), StreetAmber)
        )
    }
    items.add(StatCardData(R.string.stat_credits, grouped(state.credits), RoadYellow))
    Box(modifier = Modifier.fillMaxWidth()) {
        if (items.size == 4) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCardRow(items = items.take(2))
                StatCardRow(items = items.drop(2))
            }
        } else {
            StatCardRow(items = items)
        }
    }
}
