package com.Meztido0.Dewgov.presentation.routemap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.ChromeLight
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.SurfaceElevated
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton

@Composable
fun RouteMapScreen(
    onOpenLobby: () -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    val routeViewModel: RouteMapViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by routeViewModel.state.collectAsStateWithLifecycle()

    RouteMapContent(
        state = state,
        onToggle = routeViewModel::toggle,
        onOpenLobby = { machineId ->
            routeViewModel.selectMachine(machineId)
            onOpenLobby()
        },
        onMenu = onMenu,
        onBack = onBack
    )
}

@Composable
private fun RouteMapContent(
    state: RouteMapUiState,
    onToggle: (String) -> Unit,
    onOpenLobby: (String) -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.route_map_title,
        onBack = onBack
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .align(Alignment.TopStart)
            ) {
                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = ChromeLight.copy(alpha = 0.3f),
                        start = Offset(size.width / 2f, y),
                        end = Offset(size.width / 2f, y + 10f),
                        strokeWidth = 3f
                    )
                    y += 22f
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.nodes.reversed().forEach { node ->
                    RouteRow(
                        node = node,
                        badgeCount = state.badgeCount,
                        expanded = state.expandedId == node.district.id,
                        onToggle = { onToggle(node.district.id) },
                        onOpenLobby = { onOpenLobby(node.district.machineId) }
                    )
                }
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
private fun RouteRow(
    node: RouteNode,
    badgeCount: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    onOpenLobby: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = node.unlocked, onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        when {
                            node.current -> ChromeCyan.copy(alpha = 0.2f)
                            node.unlocked -> NeonMagenta.copy(alpha = 0.9f)
                            else -> SurfaceElevated
                        },
                        CircleShape
                    )
                    .border(
                        2.dp,
                        if (node.current) ChromeCyan else NeonMagenta.copy(alpha = 0.4f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        node.current -> Icons.Filled.Place
                        node.unlocked -> Icons.Filled.CheckCircle
                        else -> Icons.Filled.Lock
                    },
                    contentDescription = null,
                    tint = if (node.unlocked) TextPrimary else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        GlassCard(modifier = Modifier.weight(1f), contentPadding = 12.dp) {
            Text(
                text = stringResource(node.district.nameRes),
                style = MaterialTheme.typography.titleMedium,
                color = if (node.unlocked) TextPrimary else TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (node.current) {
                    stringResource(R.string.route_you_are_here)
                } else if (node.unlocked) {
                    stringResource(R.string.route_unlocked, node.district.badgeRequirement)
                } else {
                    stringResource(
                        R.string.route_locked,
                        node.district.badgeRequirement,
                        badgeCount
                    )
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (node.current) ChromeCyan else TextSecondary
            )
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(node.district.grantRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(
                    textRes = R.string.action_slot_lobby,
                    onClick = onOpenLobby,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
