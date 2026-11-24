package com.Meztido0.Dewgov.presentation.missions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.SuccessGreen
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.NeonProgressBar
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionHeader
import com.Meztido0.Dewgov.domain.model.MissionProgress
import com.Meztido0.Dewgov.presentation.common.grouped

@Composable
fun MissionsScreen(onMenu: () -> Unit, onBack: () -> Unit) {
    val missionsViewModel: MissionsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by missionsViewModel.state.collectAsStateWithLifecycle()

    MissionsContent(
        state = state,
        onClaim = missionsViewModel::claim,
        onMenu = onMenu,
        onBack = onBack
    )
}

@Composable
private fun MissionsContent(
    state: MissionsUiState,
    onClaim: (String) -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.missions_title,
        onBack = onBack,
        trailing = {
            Text(
                text = stringResource(R.string.credits_value, grouped(state.credits)),
                style = MaterialTheme.typography.labelSmall,
                color = RoadYellow,
                maxLines = 1
            )
        }
    ) {
        SectionHeader(
            titleRes = R.string.missions_daily,
            captionRes = R.string.missions_reset
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.missions, key = { it.mission.id }) { entry ->
                MissionCard(entry = entry, onClaim = { onClaim(entry.mission.id) })
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
private fun MissionCard(entry: MissionProgress, onClaim: () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(entry.mission.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(entry.mission.descriptionRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
            Text(
                text = stringResource(
                    R.string.missions_counter,
                    entry.capped,
                    entry.mission.goal
                ),
                style = MaterialTheme.typography.labelSmall,
                color = ChromeCyan
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        NeonProgressBar(
            progress = entry.capped.toFloat() / entry.mission.goal,
            accent = if (entry.complete) NeonMagenta else ChromeCyan,
            barHeight = 6.dp
        )
        Spacer(modifier = Modifier.height(10.dp))
        when {
            entry.claimed -> Text(
                text = stringResource(R.string.missions_claimed),
                style = MaterialTheme.typography.labelSmall,
                color = SuccessGreen
            )
            entry.complete -> SecondaryButton(
                textRes = R.string.action_claim,
                onClick = onClaim,
                accent = NeonMagenta,
                modifier = Modifier.fillMaxWidth()
            )
            else -> Text(
                text = stringResource(R.string.missions_in_progress),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
