package com.Meztido0.Dewgov.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.ConfirmDialog
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionHeader
import com.Meztido0.Dewgov.domain.model.ReelSpeed

@Composable
fun SettingsScreen(onMenu: () -> Unit, onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onSpeed = settingsViewModel::setReelSpeed,
        onNearWin = settingsViewModel::setNearWin,
        onReducedMotion = settingsViewModel::setReducedMotion,
        onPayoutTable = settingsViewModel::setPayoutTable,
        onRunSpins = settingsViewModel::setRunSpins,
        onAskReset = settingsViewModel::askReset,
        onCancelReset = settingsViewModel::cancelReset,
        onConfirmReset = settingsViewModel::confirmReset,
        onMenu = onMenu,
        onBack = onBack
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onSpeed: (ReelSpeed) -> Unit,
    onNearWin: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onPayoutTable: (Boolean) -> Unit,
    onRunSpins: (Int) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.settings_title,
        onBack = onBack
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(titleRes = R.string.settings_gameplay)
            }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.settings_reel_speed),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingsChip(
                            labelRes = R.string.speed_relaxed,
                            selected = state.reelSpeed == ReelSpeed.RELAXED,
                            onClick = { onSpeed(ReelSpeed.RELAXED) }
                        )
                        SettingsChip(
                            labelRes = R.string.speed_standard,
                            selected = state.reelSpeed == ReelSpeed.STANDARD,
                            onClick = { onSpeed(ReelSpeed.STANDARD) }
                        )
                        SettingsChip(
                            labelRes = R.string.speed_turbo,
                            selected = state.reelSpeed == ReelSpeed.TURBO,
                            onClick = { onSpeed(ReelSpeed.TURBO) }
                        )
                    }
                }
            }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.settings_run_length),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingsChip(
                            labelRes = R.string.run_three,
                            selected = state.runSpins == 3,
                            onClick = { onRunSpins(3) }
                        )
                        SettingsChip(
                            labelRes = R.string.run_five,
                            selected = state.runSpins == 5,
                            onClick = { onRunSpins(5) }
                        )
                        SettingsChip(
                            labelRes = R.string.run_ten,
                            selected = state.runSpins == 10,
                            onClick = { onRunSpins(10) }
                        )
                    }
                }
            }
            item {
                SectionHeader(titleRes = R.string.settings_feedback)
            }
            item {
                ToggleRow(
                    titleRes = R.string.settings_near_win,
                    captionRes = R.string.settings_near_win_caption,
                    checked = state.nearWinHighlight,
                    onCheckedChange = onNearWin
                )
            }
            item {
                ToggleRow(
                    titleRes = R.string.settings_reduced_motion,
                    captionRes = R.string.settings_reduced_motion_caption,
                    checked = state.reducedMotion,
                    onCheckedChange = onReducedMotion
                )
            }
            item {
                ToggleRow(
                    titleRes = R.string.settings_payout_table,
                    captionRes = R.string.settings_payout_table_caption,
                    checked = state.payoutTableOnStart,
                    onCheckedChange = onPayoutTable
                )
            }
            item {
                SecondaryButton(
                    textRes = R.string.action_reset_progress,
                    onClick = onAskReset,
                    accent = NeonMagenta,
                    modifier = Modifier.fillMaxWidth()
                )
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
    if (state.showResetConfirm) {
        ConfirmDialog(
            titleRes = R.string.reset_progress_title,
            bodyRes = R.string.reset_progress_body,
            confirmRes = R.string.action_reset,
            onConfirm = onConfirmReset,
            onDismiss = onCancelReset
        )
    }
}

@Composable
private fun RowScope.SettingsChip(
    labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .background(
                if (selected) ChromeCyan.copy(alpha = 0.18f) else SurfaceNight,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (selected) ChromeCyan else TextMuted.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) ChromeCyan else TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ToggleRow(
    titleRes: Int,
    captionRes: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.76f)) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(captionRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextPrimary,
                    checkedTrackColor = NeonMagenta,
                    uncheckedTrackColor = SurfaceNight
                )
            )
        }
    }
}
