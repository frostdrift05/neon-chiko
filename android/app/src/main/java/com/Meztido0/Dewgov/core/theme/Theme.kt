package com.Meztido0.Dewgov.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeonColorScheme = darkColorScheme(
    primary = NeonMagenta,
    onPrimary = TextPrimary,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = TextPrimary,
    secondary = ChromeCyan,
    onSecondary = AsphaltBlack,
    secondaryContainer = SurfaceCool,
    onSecondaryContainer = TextPrimary,
    tertiary = StreetAmber,
    onTertiary = AsphaltBlack,
    background = AsphaltBlack,
    onBackground = TextPrimary,
    surface = SurfaceNight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderLine,
    outlineVariant = BorderLine,
    error = DangerRed,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeonColorScheme,
        typography = AppTypography,
        content = content
    )
}
