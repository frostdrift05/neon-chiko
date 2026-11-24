package com.Meztido0.Dewgov.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.BorderLine
import com.Meztido0.Dewgov.core.theme.ChromeCyan

@Composable
fun NeonProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    accent: Color = ChromeCyan,
    trackColor: Color = BorderLine,
    barHeight: Dp = 8.dp,
    animated: Boolean = true
) {
    val target = progress.coerceIn(0f, 1f)
    val value by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = if (animated) 420 else 0),
        label = "neonProgress"
    )
    val shape = RoundedCornerShape(barHeight / 2)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(trackColor, shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(value)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(listOf(accent.copy(alpha = 0.75f), accent)),
                    shape
                )
        )
    }
}
