package com.Meztido0.Dewgov.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.Meztido0.Dewgov.core.ui.AppIcons as Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.di.ServiceLocator
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.NeonMagenta
import com.Meztido0.Dewgov.core.theme.RoadYellow
import com.Meztido0.Dewgov.core.theme.SurfaceNight
import com.Meztido0.Dewgov.core.theme.TextMuted
import com.Meztido0.Dewgov.core.theme.TextPrimary
import com.Meztido0.Dewgov.core.theme.TextSecondary
import com.Meztido0.Dewgov.core.ui.EmptyStateMessage
import com.Meztido0.Dewgov.core.ui.ScreenScaffold
import com.Meztido0.Dewgov.core.ui.SecondaryButton
import com.Meztido0.Dewgov.core.ui.SectionHeader

@Composable
fun CollectionScreen(onMenu: () -> Unit, onBack: () -> Unit) {
    val collectionViewModel: CollectionViewModel =
        viewModel(factory = ServiceLocator.viewModelFactory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()

    CollectionContent(
        state = state,
        onEquip = collectionViewModel::equip,
        onUnlock = collectionViewModel::unlock,
        onMenu = onMenu,
        onBack = onBack
    )
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onEquip: (String) -> Unit,
    onUnlock: (String) -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    ScreenScaffold(
        titleRes = R.string.collection_title,
        onBack = onBack,
        trailing = {
            Text(
                text = stringResource(R.string.collection_shards, state.shards),
                style = MaterialTheme.typography.labelSmall,
                color = RoadYellow,
                maxLines = 1
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(
                    titleRes = R.string.collection_skins,
                    captionRes = R.string.collection_skins_caption
                )
            }
            items(state.skins.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { entry ->
                        SkinCard(
                            entry = entry,
                            shards = state.shards,
                            onEquip = { onEquip(entry.skin.id) },
                            onUnlock = { onUnlock(entry.skin.id) }
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            item {
                SectionHeader(
                    titleRes = R.string.collection_badges,
                    captionRes = R.string.collection_badges_caption
                )
            }
            if (state.unlockedBadges == 0) {
                item {
                    EmptyStateMessage(textRes = R.string.collection_empty)
                }
            }
            items(state.badges.chunked(3)) { triple ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    triple.forEach { entry ->
                        BadgeTile(entry = entry)
                    }
                    repeat(3 - triple.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        SecondaryButton(
            textRes = R.string.action_menu,
            onClick = onMenu,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        )
    }
}

@Composable
private fun RowScope.SkinCard(
    entry: SkinEntry,
    shards: Int,
    onEquip: () -> Unit,
    onUnlock: () -> Unit
) {
    val tint = Color(entry.skin.tint)
    Card(
        modifier = Modifier
            .weight(1f)
            .height(196.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceNight.copy(alpha = 0.88f)),
        border = BorderStroke(
            if (entry.equipped) 2.dp else 1.dp,
            if (entry.equipped) NeonMagenta else tint.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(entry.skin.nameRes),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    tint.copy(alpha = 0.4f),
                    androidx.compose.ui.graphics.BlendMode.Overlay
                ),
                modifier = Modifier
                    .size(56.dp)
                    .alpha(if (entry.unlocked) 1f else 0.4f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(entry.skin.nameRes),
                style = MaterialTheme.typography.titleMedium,
                color = if (entry.unlocked) TextPrimary else TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = stringResource(entry.skin.perkRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 3,
                modifier = Modifier.weight(1f)
            )
            when {
                entry.equipped -> Text(
                    text = stringResource(R.string.collection_equipped),
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonMagenta
                )
                entry.unlocked -> SecondaryButton(
                    textRes = R.string.action_equip,
                    onClick = onEquip,
                    modifier = Modifier.fillMaxWidth()
                )
                shards >= entry.skin.shardCost -> SecondaryButton(
                    textRes = R.string.action_unlock,
                    onClick = onUnlock,
                    accent = RoadYellow,
                    modifier = Modifier.fillMaxWidth()
                )
                else -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = stringResource(
                            R.string.collection_need_shards,
                            entry.skin.shardCost
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BadgeTile(entry: BadgeEntry) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(SurfaceNight.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (entry.unlocked) ChromeCyan.copy(alpha = 0.5f) else TextMuted.copy(alpha = 0.2f),
                RoundedCornerShape(14.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(entry.badge.nameRes),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(38.dp)
                .alpha(if (entry.unlocked) 1f else 0.25f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(entry.badge.nameRes),
            style = MaterialTheme.typography.labelSmall,
            color = if (entry.unlocked) TextPrimary else TextMuted,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        if (!entry.unlocked) {
            Text(
                text = stringResource(entry.badge.conditionRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
