package com.Meztido0.Dewgov.presentation.responsible

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
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.AsphaltBlack
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SuccessGreen
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.ConfirmDialog
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionHeader
import com.Meztido0.Dewgov.presentation.common.clock
import com.Meztido0.Dewgov.presentation.dialog.BreakReminderDialog

@Composable
fun ResponsiblePlayScreen(onMenu: () -> Unit, onBack: () -> Unit) {
    val responsibleViewModel: ResponsiblePlayViewModel =
        viewModel(factory = ServiceLocator.viewModelFactory)
    val state by responsibleViewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        responsibleViewModel.resumeSession()
        onDispose { responsibleViewModel.pauseSession() }
    }

    ResponsiblePlayContent(
        state = state,
        onReminderChange = responsibleViewModel::setReminderEnabled,
        onIntervalChange = responsibleViewModel::setInterval,
        onAskReset = responsibleViewModel::askResetCredits,
        onCancelReset = responsibleViewModel::cancelResetCredits,
        onConfirmReset = responsibleViewModel::confirmResetCredits,
        onDismissReminder = responsibleViewModel::dismissReminder,
        onMenu = onMenu,
        onBack = onBack
    )
}

@Composable
private fun ResponsiblePlayContent(
    state: ResponsiblePlayUiState,
    onReminderChange: (Boolean) -> Unit,
    onIntervalChange: (Int) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismissReminder: () -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.responsible_title,
        onBack = onBack
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AsphaltBlack.copy(alpha = 0.85f), RoundedCornerShape(18.dp))
                        .border(2.dp, StreetAmber, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = StreetAmber,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.responsible_banner),
                            style = MaterialTheme.typography.headlineMedium,
                            color = StreetAmber
                        )
                        Text(
                            text = stringResource(R.string.responsible_banner_sub),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
            item {
                SectionHeader(titleRes = R.string.responsible_facts)
            }
            item {
                FactRow(textRes = R.string.responsible_fact_1)
            }
            item {
                FactRow(textRes = R.string.responsible_fact_2)
            }
            item {
                FactRow(textRes = R.string.responsible_fact_3)
            }
            item {
                FactRow(textRes = R.string.responsible_fact_4)
            }
            item {
                SessionCard(state = state)
            }
            item {
                ReminderCard(
                    state = state,
                    onReminderChange = onReminderChange,
                    onIntervalChange = onIntervalChange
                )
            }
            item {
                SecondaryButton(
                    textRes = R.string.action_reset_credits,
                    onClick = onAskReset,
                    accent = StreetAmber,
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
            titleRes = R.string.reset_credits_title,
            bodyRes = R.string.reset_credits_body,
            confirmRes = R.string.action_reset,
            onConfirm = onConfirmReset,
            onDismiss = onCancelReset
        )
    }
    if (state.showReminder) {
        BreakReminderDialog(
            minutes = state.intervalMinutes,
            onKeepPlaying = onDismissReminder,
            onMenu = {
                onDismissReminder()
                onMenu()
            }
        )
    }
}

@Composable
private fun SessionCard(state: ResponsiblePlayUiState) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.responsible_session),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Text(
            text = clock(state.sessionSeconds),
            style = MaterialTheme.typography.displaySmall,
            color = ChromeCyan,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.responsible_spins_today, state.spinsToday),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}

@Composable
private fun ReminderCard(
    state: ResponsiblePlayUiState,
    onReminderChange: (Boolean) -> Unit,
    onIntervalChange: (Int) -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.responsible_break_reminder),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Switch(
                checked = state.reminderEnabled,
                onCheckedChange = onReminderChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextPrimary,
                    checkedTrackColor = NeonMagenta,
                    uncheckedTrackColor = SurfaceNight
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IntervalChip(
                minutes = 15,
                selected = state.intervalMinutes == 15,
                enabled = state.reminderEnabled,
                onClick = { onIntervalChange(15) }
            )
            IntervalChip(
                minutes = 30,
                selected = state.intervalMinutes == 30,
                enabled = state.reminderEnabled,
                onClick = { onIntervalChange(30) }
            )
            IntervalChip(
                minutes = 60,
                selected = state.intervalMinutes == 60,
                enabled = state.reminderEnabled,
                onClick = { onIntervalChange(60) }
            )
        }
    }
}

@Composable
private fun FactRow(textRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceNight.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
    }
}

@Composable
private fun IntervalChip(
    minutes: Int,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) ChromeCyan.copy(alpha = 0.18f) else SurfaceNight,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (selected) ChromeCyan else TextMuted.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.responsible_interval, minutes),
            style = MaterialTheme.typography.labelSmall,
            color = when {
                !enabled -> TextMuted
                selected -> ChromeCyan
                else -> TextSecondary
            }
        )
    }
}
