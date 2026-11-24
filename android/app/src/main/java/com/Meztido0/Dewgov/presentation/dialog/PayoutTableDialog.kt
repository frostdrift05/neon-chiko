package com.Meztido0.Dewgov.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import com.Meztido0.Dewgov.presentation.common.grouped
import com.Meztido0.Dewgov.presentation.common.symbolAccent
import com.Meztido0.Dewgov.presentation.common.symbolDrawable
import com.Meztido0.Dewgov.presentation.common.symbolNameRes
import com.Meztido0.Dewgov.presentation.common.symbolRoleRes

@Composable
fun PayoutTableDialog(bet: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceNight,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.payout_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SampleData.payoutOrder.forEach { symbol ->
                    PayoutRow(symbol = symbol, bet = bet)
                }
                Text(
                    text = stringResource(R.string.payout_note),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.action_close),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChromeCyan
                )
            }
        }
    )
}

@Composable
private fun PayoutRow(symbol: SlotSymbol, bet: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(symbolDrawable(symbol)),
            contentDescription = stringResource(symbolNameRes(symbol)),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(28.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(symbolNameRes(symbol)),
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            Text(
                text = stringResource(symbolRoleRes(symbol)),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        Text(
            text = if (symbol.payout > 0) {
                stringResource(R.string.payout_value, symbol.payout, grouped(bet * symbol.payout))
            } else {
                stringResource(R.string.payout_scatter)
            },
            style = MaterialTheme.typography.labelSmall,
            color = symbolAccent(symbol)
        )
    }
}
