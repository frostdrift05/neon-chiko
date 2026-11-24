package com.Meztido0.Dewgov.presentation.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.EmberOrange
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.StreetAmber
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.ChromeRing
import com.Meztido0.Dewgov.core.ui.GlassCard
import com.Meztido0.Dewgov.core.ui.NeonProgressBar
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.presentation.common.grouped

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onLobby: () -> Unit,
    onBonusRoad: () -> Unit,
    onRouteMap: () -> Unit,
    onCollection: () -> Unit,
    onMissions: () -> Unit,
    onRewards: () -> Unit,
    onResponsiblePlay: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onPlay = {
            menuViewModel.startRun()
            onPlay()
        },
        onLobby = onLobby,
        onBonusRoad = onBonusRoad,
        onRouteMap = onRouteMap,
        onCollection = onCollection,
        onMissions = onMissions,
        onRewards = onRewards,
        onResponsiblePlay = onResponsiblePlay,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLobby: () -> Unit,
    onBonusRoad: () -> Unit,
    onRouteMap: () -> Unit,
    onCollection: () -> Unit,
    onMissions: () -> Unit,
    onRewards: () -> Unit,
    onResponsiblePlay: () -> Unit,
    onSettings: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.menu_title,
        backgroundAlpha = 0.55f,
        leading = {
            ChromeRing(size = 40.dp, glowElevation = 10.dp) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home_mark),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        trailing = {
            RankPill(rankNameRes = state.rankNameRes, routePoints = state.routePoints)
        }
    ) {
        CreditBar(state = state)
        Spacer(modifier = Modifier.height(12.dp))
        BentoGrid(
            state = state,
            onPlay = onPlay,
            onLobby = onLobby,
            onBonusRoad = onBonusRoad,
            onRouteMap = onRouteMap,
            onCollection = onCollection,
            onMissions = onMissions,
            onRewards = onRewards,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.menu_hint),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                textRes = R.string.action_responsible_play,
                onClick = onResponsiblePlay,
                icon = Icons.Filled.Warning,
                accent = StreetAmber,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                textRes = R.string.action_settings,
                onClick = onSettings,
                icon = Icons.Filled.Settings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RankPill(rankNameRes: Int, routePoints: Int) {
    Box(
        modifier = Modifier
            .background(SurfaceNight.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
            .border(1.dp, ChromeCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = stringResource(R.string.rank_pill, stringResource(rankNameRes), routePoints),
            style = MaterialTheme.typography.labelSmall,
            color = ChromeCyan,
            maxLines = 1
        )
    }
}

@Composable
private fun CreditBar(state: MenuUiState) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = RoadYellow.copy(alpha = 0.25f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = RoadYellow,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.credits_value, grouped(state.credits)),
                    style = MaterialTheme.typography.displaySmall,
                    color = RoadYellow,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth(0.44f)
            ) {
                Text(
                    text = stringResource(
                        R.string.lane_boost_value,
                        state.laneBoost,
                        state.laneBoostMax
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChromeCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                NeonProgressBar(
                    progress = state.laneBoost.toFloat() / state.laneBoostMax,
                    barHeight = 6.dp,
                    animated = !state.reducedMotion
                )
            }
        }
    }
}

@Composable
private fun BentoGrid(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLobby: () -> Unit,
    onBonusRoad: () -> Unit,
    onRouteMap: () -> Unit,
    onCollection: () -> Unit,
    onMissions: () -> Unit,
    onRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeroTile(
            bet = state.bet,
            reducedMotion = state.reducedMotion,
            onPlay = onPlay,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SpriteTile(
                labelRes = R.string.tile_slot_lobby,
                caption = stringResource(
                    R.string.tile_lobby_caption,
                    state.machinesTotal,
                    state.machinesTotal - state.machinesUnlocked
                ),
                sprite = R.drawable.sprite_3,
                accent = ChromeCyan,
                onClick = onLobby
            )
            SpriteTile(
                labelRes = R.string.tile_bonus_road,
                caption = stringResource(R.string.tile_bonus_caption),
                sprite = R.drawable.sprite_5,
                accent = StreetAmber,
                onClick = onBonusRoad
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconTile(
                labelRes = R.string.tile_route_map,
                caption = stringResource(
                    R.string.tile_route_caption,
                    state.districtIndex,
                    state.totalDistricts
                ),
                icon = Icons.Filled.Place,
                accent = NeonMagenta,
                onClick = onRouteMap
            )
            SpriteTile(
                labelRes = R.string.tile_collection,
                caption = stringResource(
                    R.string.tile_collection_caption,
                    state.skinsUnlocked + state.badgeCount,
                    state.totalSkins + state.totalBadges
                ),
                sprite = R.drawable.sprite_1,
                accent = RoadYellow,
                onClick = onCollection
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconTile(
                labelRes = R.string.tile_missions,
                caption = stringResource(
                    R.string.tile_missions_caption,
                    state.missionsActive,
                    state.totalMissions
                ),
                icon = Icons.AutoMirrored.Filled.List,
                accent = ChromeCyan,
                onClick = onMissions
            )
            IconTile(
                labelRes = R.string.tile_rewards,
                caption = stringResource(R.string.tile_rewards_caption),
                icon = Icons.Filled.Star,
                accent = StreetAmber,
                onClick = onRewards
            )
        }
    }
}

@Composable
private fun HeroTile(
    bet: Int,
    reducedMotion: Boolean,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val transition = rememberInfiniteTransition(label = "heroSheen")
    val sheen by transition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sheenAlpha"
    )
    val sheenAlpha = if (reducedMotion) 0.12f else sheen
    Box(
        modifier = modifier
            .shadow(16.dp, shape, ambientColor = NeonMagenta, spotColor = StreetAmber)
            .background(
                Brush.horizontalGradient(listOf(NeonMagenta, EmberOrange, StreetAmber)),
                shape
            )
            .border(1.dp, Color.White.copy(alpha = 0.25f), shape)
            .clickable(onClick = onPlay)
            .padding(18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .align(Alignment.TopStart)
                .alpha(sheenAlpha)
                .background(Color.White, RoundedCornerShape(12.dp))
        )
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = stringResource(R.string.action_play),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
            }
            Text(
                text = stringResource(R.string.tile_play_caption, bet),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun RowScope.SpriteTile(
    labelRes: Int,
    caption: String,
    sprite: Int,
    accent: Color,
    onClick: () -> Unit
) {
    TileFrame(accent = accent, onClick = onClick) {
        Image(
            painter = painterResource(sprite),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(36.dp)
        )
        TileLabels(labelRes = labelRes, caption = caption, accent = accent)
    }
}

@Composable
private fun RowScope.IconTile(
    labelRes: Int,
    caption: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit
) {
    TileFrame(accent = accent, onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(28.dp)
        )
        TileLabels(labelRes = labelRes, caption = caption, accent = accent)
    }
}

@Composable
private fun RowScope.TileFrame(
    accent: Color,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(SurfaceNight.copy(alpha = 0.82f), shape)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(ChromeCyan.copy(alpha = 0.35f), accent.copy(alpha = 0.45f))
                    )
                ),
                shape
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
        content = content
    )
}

@Composable
private fun TileLabels(labelRes: Int, caption: String, accent: Color) {
    Text(
        text = stringResource(labelRes),
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
        maxLines = 2
    )
    Text(
        text = caption,
        style = MaterialTheme.typography.labelSmall,
        color = accent.copy(alpha = 0.9f),
        maxLines = 2
    )
}
