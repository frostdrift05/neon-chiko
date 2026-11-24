package com.Meztido0.Dewgov.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.BorderLine
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceElevated
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import com.Meztido0.Dewgov.presentation.common.symbolDrawable
import com.Meztido0.Dewgov.presentation.common.symbolNameRes

@Composable
fun ReelCell(
    symbol: SlotSymbol,
    cellSize: Dp,
    modifier: Modifier = Modifier,
    winning: Boolean = false,
    nearMiss: Boolean = false,
    spinning: Boolean = false
) {
    val scale by animateFloatAsState(
        targetValue = if (winning) 1.06f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 700f),
        label = "cellScale"
    )
    val shape = RoundedCornerShape(14.dp)
    val ringColor = when {
        winning -> NeonMagenta
        nearMiss -> StreetAmber
        else -> BorderLine
    }
    Box(
        modifier = modifier
            .size(cellSize)
            .padding(3.dp)
            .scale(scale)
            .background(SurfaceElevated.copy(alpha = 0.95f), shape)
            .border(if (winning || nearMiss) 2.dp else 1.dp, ringColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(symbolDrawable(symbol)),
            contentDescription = stringResource(symbolNameRes(symbol)),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(cellSize - 24.dp)
                .alpha(if (spinning) 0.55f else 1f)
        )
    }
}
