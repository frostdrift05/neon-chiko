package com.Meztido0.Dewgov.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceElevated

@Composable
fun ChromeRing(
    size: Dp,
    modifier: Modifier = Modifier,
    glow: Color = NeonMagenta,
    glowElevation: Dp = 18.dp,
    containerColor: Color = SurfaceElevated.copy(alpha = 0.8f),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(size / 2)
    Box(
        modifier = modifier
            .size(size)
            .shadow(glowElevation, shape, ambientColor = glow, spotColor = ChromeCyan)
            .background(containerColor, shape)
            .border(
                width = 2.dp,
                brush = Brush.sweepGradient(
                    listOf(ChromeCyan, NeonMagenta, StreetAmber, ChromeCyan)
                ),
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
