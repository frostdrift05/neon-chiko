package com.Meztido0.Dewgov.presentation.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.NeonProgressBar
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionHeader
import com.Meztido0.Dewgov.domain.model.BoardRow
import com.Meztido0.Dewgov.domain.model.RankTier
import com.Meztido0.Dewgov.presentation.common.grouped

@Composable
fun RewardsScreen(onMenu: () -> Unit, onBack: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by rewardsViewModel.state.collectAsStateWithLifecycle()

    RewardsContent(state = state, onMenu = onMenu, onBack = onBack)
}

@Composable
private fun RewardsContent(
    state: RewardsUiState,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.rewards_title,
        onBack = onBack
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(state.rankNameRes),
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonMagenta
                    )
                    Text(
                        text = stringResource(R.string.rewards_points, state.routePoints),
                        style = MaterialTheme.typography.labelSmall,
                        color = ChromeCyan
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    NeonProgressBar(progress = state.progress, accent = NeonMagenta)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(
                            R.string.rewards_to_next,
                            state.pointsToNext,
                            stringResource(state.nextRankNameRes)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = stringResource(state.nextRewardRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = StreetAmber
                    )
                }
            }
            item {
                SectionHeader(titleRes = R.string.rewards_ladder)
            }
            itemsIndexed(state.ladder) { index, tier ->
                LadderRow(tier = tier, reached = index <= state.rankIndex)
            }
            item {
                SectionHeader(
                    titleRes = R.string.rewards_board,
                    captionRes = R.string.rewards_board_caption
                )
            }
            itemsIndexed(state.board) { index, row ->
                BoardRowItem(position = index + 1, row = row)
            }
        }
        SecondaryButton(
            textRes = R.string.action_menu,
            onClick = onMenu,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        )
    }
}

@Composable
private fun LadderRow(tier: RankTier, reached: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceNight.copy(alpha = 0.78f), RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (reached) NeonMagenta.copy(alpha = 0.5f) else TextMuted.copy(alpha = 0.2f),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (reached) Icons.Filled.CheckCircle else Icons.Filled.Lock,
            contentDescription = null,
            tint = if (reached) NeonMagenta else TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(tier.nameRes),
                style = MaterialTheme.typography.titleMedium,
                color = if (reached) TextPrimary else TextMuted
            )
            Text(
                text = stringResource(tier.rewardRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 2
            )
        }
        Text(
            text = stringResource(R.string.rewards_threshold, tier.threshold),
            style = MaterialTheme.typography.labelSmall,
            color = ChromeCyan
        )
    }
}

@Composable
private fun BoardRowItem(position: Int, row: BoardRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceNight.copy(alpha = 0.72f), RoundedCornerShape(12.dp))
            .border(
                if (row.isPlayer) 2.dp else 1.dp,
                if (row.isPlayer) ChromeCyan else TextMuted.copy(alpha = 0.18f),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.board_position, position),
            style = MaterialTheme.typography.labelSmall,
            color = RoadYellow
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(row.nameRes),
            style = MaterialTheme.typography.bodyLarge,
            color = if (row.isPlayer) ChromeCyan else TextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
        Text(
            text = stringResource(R.string.rewards_points, row.routePoints),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
