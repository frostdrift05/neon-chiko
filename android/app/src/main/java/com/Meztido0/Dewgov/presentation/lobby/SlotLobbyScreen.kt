package com.Meztido0.Dewgov.presentation.lobby

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.PrimaryButton
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.presentation.common.grouped
import com.Meztido0.Dewgov.presentation.common.spriteDrawable
import com.Meztido0.Dewgov.presentation.common.volatilityRes

@Composable
fun SlotLobbyScreen(onPlay: () -> Unit, onBack: () -> Unit) {
    val lobbyViewModel: SlotLobbyViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by lobbyViewModel.state.collectAsStateWithLifecycle()

    SlotLobbyContent(
        state = state,
        onSelect = lobbyViewModel::select,
        onPlay = {
            lobbyViewModel.startRun()
            onPlay()
        },
        onBack = onBack
    )
}

@Composable
private fun SlotLobbyContent(
    state: SlotLobbyUiState,
    onSelect: (String) -> Unit,
    onPlay: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.lobby_title,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            items(state.machines, key = { it.machine.id }) { entry ->
                MachineCard(
                    entry = entry,
                    badgeCount = state.badgeCount,
                    onSelect = { onSelect(entry.machine.id) }
                )
            }
        }
        PrimaryButton(
            textRes = R.string.action_play,
            onClick = onPlay,
            height = 60.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        )
    }
}

@Composable
private fun MachineCard(
    entry: LobbyMachine,
    badgeCount: Int,
    onSelect: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(enabled = entry.unlocked, onClick = onSelect),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = SurfaceNight.copy(alpha = 0.88f)),
        border = BorderStroke(
            if (entry.selected) 2.dp else 1.dp,
            when {
                entry.selected -> NeonMagenta
                entry.unlocked -> ChromeCyan.copy(alpha = 0.4f)
                else -> TextMuted.copy(alpha = 0.3f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(ChromeCyan.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .border(1.dp, ChromeCyan.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(spriteDrawable(entry.machine.art)),
                    contentDescription = stringResource(entry.machine.nameRes),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(52.dp)
                        .alpha(if (entry.unlocked) 1f else 0.45f)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(entry.machine.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (entry.unlocked) TextPrimary else TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.lobby_machine_line,
                        stringResource(volatilityRes(entry.machine.volatility)),
                        entry.machine.topLine
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChromeCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(entry.machine.weightingRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (entry.unlocked) {
                    Text(
                        text = if (entry.selected) {
                            stringResource(R.string.lobby_selected)
                        } else {
                            stringResource(R.string.lobby_tap_to_select)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (entry.selected) NeonMagenta else TextMuted
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = stringResource(
                                R.string.lobby_locked,
                                entry.machine.badgeRequirement,
                                badgeCount
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
