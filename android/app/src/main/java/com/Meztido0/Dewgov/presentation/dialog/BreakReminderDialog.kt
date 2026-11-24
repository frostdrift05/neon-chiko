package com.Meztido0.Dewgov.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary

@Composable
fun BreakReminderDialog(
    minutes: Int,
    onKeepPlaying: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onKeepPlaying,
        containerColor = SurfaceNight,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.break_title),
                style = MaterialTheme.typography.titleMedium,
                color = StreetAmber
            )
        },
        text = {
            Text(
                text = stringResource(R.string.break_body, minutes),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onKeepPlaying) {
                Text(
                    text = stringResource(R.string.action_keep_playing),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChromeCyan
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onMenu) {
                Text(
                    text = stringResource(R.string.action_menu),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }
    )
}
