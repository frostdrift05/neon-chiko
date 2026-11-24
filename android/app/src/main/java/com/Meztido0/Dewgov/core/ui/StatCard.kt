package com.Meztido0.Dewgov.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextSecondary

data class StatCardData(
    val labelRes: Int,
    val value: String,
    val accent: Color
)

@Composable
fun StatCard(
    data: StatCardData,
    modifier: Modifier = Modifier
) {
    if (data.value.isBlank()) {
        return
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceNight.copy(alpha = 0.86f)),
        border = BorderStroke(1.dp, data.accent.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .background(data.accent, CircleShape)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.value,
                color = data.accent,
                fontSize = 22.sp,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = stringResource(data.labelRes),
                color = TextSecondary,
                fontSize = 10.sp,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun StatCardRow(
    items: List<StatCardData>,
    modifier: Modifier = Modifier
) {
    val visible = items.filter { it.value.isNotBlank() }
    if (visible.size < 2) {
        return
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visible.forEach { item ->
            StatCard(data = item, modifier = Modifier.weight(1f))
        }
    }
}
