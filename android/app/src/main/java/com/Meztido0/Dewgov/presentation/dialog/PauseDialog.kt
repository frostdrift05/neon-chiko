package com.Meztido0.Dewgov.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onEndRun: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceNight,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.pause_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                SecondaryButton(
                    textRes = R.string.action_end_run,
                    onClick = onEndRun,
                    accent = NeonMagenta,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    textRes = R.string.action_menu,
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.action_resume),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChromeCyan
                )
            }
        }
    )
}
