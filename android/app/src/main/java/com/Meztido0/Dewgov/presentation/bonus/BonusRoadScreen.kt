package com.Meztido0.Dewgov.presentation.bonus

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.ChromeLight
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.LaneCard
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.presentation.common.grouped
import com.Meztido0.Dewgov.presentation.common.outcomeAccent
import com.Meztido0.Dewgov.presentation.common.outcomeTitleRes

@Composable
fun BonusRoadScreen(
    practice: Boolean,
    onContinue: () -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    val bonusViewModel: BonusRoadViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by bonusViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(practice) {
        bonusViewModel.begin(practice)
    }

    BonusRoadContent(
        state = state,
        onPick = bonusViewModel::pick,
        onContinue = if (practice) onMenu else onContinue,
        onMenu = onMenu,
        onBack = onBack
    )
}

@Composable
private fun BonusRoadContent(
    state: BonusRoadUiState,
    onPick: (Boolean) -> Unit,
    onContinue: () -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.bonus_title,
        onBack = onBack,
        backgroundAlpha = 0.7f,
        overlayColors = listOf(
            StreetAmber.copy(alpha = 0.18f),
            AsphaltBlack.copy(alpha = 0.78f),
            AsphaltBlack.copy(alpha = 0.95f)
        )
    ) {
        Text(
            text = stringResource(
                R.string.bonus_junction,
                state.junction,
                state.totalJunctions
            ),
            style = MaterialTheme.typography.labelSmall,
            color = ChromeCyan,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RoadScene(state = state)
        }
        if (state.finished) {
            EndPanel(state = state, onContinue = onContinue, onMenu = onMenu)
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LaneCard(
                    labelRes = R.string.lane_left,
                    teaserIndex = state.junction,
                    revealedTitleRes = if (state.revealed && state.pickedLeft) {
                        state.outcome?.let { outcomeTitleRes(it.type) }
                    } else {
                        null
                    },
                    revealedDetail = if (state.revealed && state.pickedLeft) {
                        outcomeDetail(state)
                    } else {
                        ""
                    },
                    revealedAccent = state.outcome?.let { outcomeAccent(it.type) } ?: ChromeCyan,
                    enabled = !state.revealed,
                    onPick = { onPick(true) },
                    modifier = Modifier.weight(1f)
                )
                LaneCard(
                    labelRes = R.string.lane_right,
                    teaserIndex = state.junction + 1,
                    revealedTitleRes = if (state.revealed && !state.pickedLeft) {
                        state.outcome?.let { outcomeTitleRes(it.type) }
                    } else {
                        null
                    },
                    revealedDetail = if (state.revealed && !state.pickedLeft) {
                        outcomeDetail(state)
                    } else {
                        ""
                    },
                    revealedAccent = state.outcome?.let { outcomeAccent(it.type) } ?: StreetAmber,
                    enabled = !state.revealed,
                    onPick = { onPick(false) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun outcomeDetail(state: BonusRoadUiState): String {
    val outcome = state.outcome ?: return ""
    return when {
        outcome.credits > 0 -> stringResource(R.string.credits_plus, grouped(outcome.credits))
        outcome.shards > 0 -> stringResource(R.string.shards_plus, outcome.shards)
        outcome.badge -> stringResource(R.string.badge_plus)
        outcome.routePoints > 0 -> stringResource(R.string.rp_plus, outcome.routePoints)
        else -> stringResource(R.string.bonus_route_ends)
    }
}

@Composable
private fun RoadScene(state: BonusRoadUiState) {
    val transition = rememberInfiniteTransition(label = "roadScroll")
    val scroll by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dashScroll"
    )
    val offset = if (state.reducedMotion) 0f else scroll
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val vanishY = size.height * 0.18f
            val centerX = size.width / 2f
            drawLine(
                color = ChromeLight.copy(alpha = 0.35f),
                start = Offset(centerX - size.width * 0.46f, size.height),
                end = Offset(centerX - size.width * 0.06f, vanishY),
                strokeWidth = 4f
            )
            drawLine(
                color = ChromeLight.copy(alpha = 0.35f),
                start = Offset(centerX + size.width * 0.46f, size.height),
                end = Offset(centerX + size.width * 0.06f, vanishY),
                strokeWidth = 4f
            )
            val dashes = 7
            for (index in 0 until dashes) {
                val progress = ((index + offset) / dashes).coerceIn(0f, 1f)
                val y = size.height - progress * (size.height - vanishY)
                val halfLength = 16f * (1f - progress) + 3f
                drawLine(
                    color = ChromeLight.copy(alpha = 0.55f * (1f - progress) + 0.1f),
                    start = Offset(centerX, y - halfLength),
                    end = Offset(centerX, y + halfLength),
                    strokeWidth = 6f * (1f - progress) + 1.5f
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.sprite_1),
            contentDescription = stringResource(R.string.cd_equipped_skin),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                Color(state.skinTint).copy(alpha = 0.3f),
                androidx.compose.ui.graphics.BlendMode.Overlay
            ),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(96.dp)
        )
    }
}

@Composable
private fun EndPanel(
    state: BonusRoadUiState,
    onContinue: () -> Unit,
    onMenu: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (state.roadblock) {
                    stringResource(R.string.bonus_roadblock_title)
                } else {
                    stringResource(R.string.bonus_complete_title)
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (state.roadblock) StreetAmber else NeonMagenta
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.bonus_summary,
                    grouped(if (state.practice) 0 else state.creditsEarned),
                    state.routePointsEarned,
                    state.badgesEarned,
                    state.shardsEarned
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            if (state.practice) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.bonus_practice_note),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
        PrimaryButton(
            textRes = R.string.action_continue,
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth()
        )
        SecondaryButton(
            textRes = R.string.action_menu,
            onClick = onMenu,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
