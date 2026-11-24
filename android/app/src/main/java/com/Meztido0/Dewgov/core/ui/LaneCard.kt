package com.Meztido0.Dewgov.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary

@Composable
fun LaneCard(
    labelRes: Int,
    teaserIndex: Int,
    revealedTitleRes: Int?,
    revealedDetail: String,
    revealedAccent: Color,
    enabled: Boolean,
    onPick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val revealed = revealedTitleRes != null
    val borderColor = if (revealed) revealedAccent else ChromeCyan.copy(alpha = 0.45f)
    Card(
        modifier = modifier.clickable(enabled = enabled, onClick = onPick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceNight.copy(alpha = 0.9f)),
        border = BorderStroke(if (revealed) 2.dp else 1.dp, borderColor)
    ) {
        AnimatedContent(
            targetState = revealedTitleRes,
            transitionSpec = { (scaleIn(initialScale = 0.9f) + fadeIn()) togetherWith fadeOut() },
            label = "laneReveal"
        ) { titleRes ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = teaserIcon(teaserIndex),
                        contentDescription = null,
                        tint = if (titleRes != null) revealedAccent else ChromeCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = stringResource(labelRes),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(titleRes ?: R.string.lane_unknown),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (titleRes != null) revealedAccent else TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    if (titleRes != null && revealedDetail.isNotBlank()) {
                        Text(
                            text = revealedDetail,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

private fun teaserIcon(index: Int) = when (index % 3) {
    0 -> Icons.Filled.Star
    1 -> Icons.Filled.Place
    else -> Icons.Filled.Favorite
}
