package com.dongbv.alf_03_iptv.ui.main.playlist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.AppNeonButton
import com.dongbv.alf_03_iptv.ui.component.ChannelSearchBar
import com.dongbv.alf_03_iptv.ui.component.LayoutToggle
import com.dongbv.alf_03_iptv.viewmodel.PlayListViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// ─── Entry point ─────────────────────────────────────────────────────────────

@Composable
fun PlayListScreen(
    playlistId: Long,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onOpenChannel: (ChannelEntity) -> Unit = {},
    viewModel: PlayListViewModel = koinViewModel(key = "playlist_$playlistId") {
        parametersOf(playlistId)
    }
) {
    val playlist by viewModel.playlist.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGroup by viewModel.selectedGroup.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val availableGroups by viewModel.availableGroups.collectAsStateWithLifecycle()

    PlayListScreenContent(
        modifier = modifier,
        title = playlist?.name ?: "Playlist",
        searchQuery = searchQuery,
        selectedGroup = selectedGroup,
        isGridView = isGridView,
        channels = channels,
        availableGroups = availableGroups,
        onBack = onBack,
        onOpenChannel = onOpenChannel,
        onSearchQueryChanged = viewModel::setSearchQuery,
        onGroupSelected = viewModel::setGroupFilter,
        onToggleLayout = viewModel::toggleLayout,
        onToggleFavorite = viewModel::toggleFavorite
    )
}

// ─── Stateless content ───────────────────────────────────────────────────────

@Composable
fun PlayListScreenContent(
    modifier: Modifier = Modifier,
    title: String = "Playlist",
    searchQuery: String = "",
    selectedGroup: String? = null,
    isGridView: Boolean = true,
    channels: List<ChannelEntity> = emptyList(),
    availableGroups: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onOpenChannel: (ChannelEntity) -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onGroupSelected: (String?) -> Unit = {},
    onToggleLayout: () -> Unit = {},
    onToggleFavorite: (Long) -> Unit = {}
) {
    val hasFilter = searchQuery.isNotBlank() || selectedGroup != null

    Column(
        modifier = modifier
            .backgroundResource(R.drawable.img_bg_app)
            .fillMaxSize()
            .paddingTop(12.Sdp)
    ) {
       
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .paddingHorizontal(16.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.Sdp)
                    .background(white.opacity(8), CircleShape)
                    .onClick(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_back),
                    contentDescription = null,
                    tint = IptvTextPrimary,
                    modifier = Modifier.size(16.Sdp)
                )
            }
            Spacer(Modifier.width(10.Sdp))
            Text(
                text = title,
                style = headline700(fontSize = 18.Ssp, color = IptvTextPrimary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (channels.isNotEmpty()) {
                Text(
                    text = "${channels.size} Channel",
                    style = headline600(fontSize = 12.Ssp, color = IptvAccent)
                )
            }
            Spacer(Modifier.width(10.Sdp))
            LayoutToggle(isGridView = isGridView, onToggle = onToggleLayout)
        }

        Spacer(Modifier.height(12.Sdp))

       
        ChannelSearchBar(
            query = searchQuery,
            onQueryChanged = onSearchQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .paddingHorizontal(16.Sdp)
        )

       
        if (availableGroups.isNotEmpty()) {
            Spacer(Modifier.height(10.Sdp))
            GroupFilterRow(
                groups = availableGroups,
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected
            )
        }

        Spacer(Modifier.height(8.Sdp))

        when {
            channels.isEmpty() -> PlayListEmptyState(hasFilter = hasFilter)
            isGridView -> PlayListChannelsGrid(
                channels = channels,
                onToggleFavorite = onToggleFavorite,
                onOpenChannel = onOpenChannel
            )
            else -> PlayListChannelsList(
                channels = channels,
                onToggleFavorite = onToggleFavorite,
                onOpenChannel = onOpenChannel
            )
        }
    }
}

// ─── Group chips ─────────────────────────────────────────────────────────────

@Composable
private fun GroupFilterRow(
    groups: List<String>,
    selectedGroup: String?,
    onGroupSelected: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .paddingHorizontal(16.Sdp),
        horizontalArrangement = Arrangement.spacedBy(8.Sdp)
    ) {
        GroupChip("All", selectedGroup == null) { onGroupSelected(null) }
        groups.forEach { group ->
            GroupChip(
                group,
                selectedGroup == group
            ) { onGroupSelected(if (selectedGroup == group) null else group) }
        }
    }
}

@Composable
private fun GroupChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val textColor by animateColorAsState(
        if (isSelected) white else IptvTextSecondary,
        tween(200),
        label = "chipText"
    )
    AppNeonButton(
        text = label,
        onClick = onClick,
        textStyle = headline600(fontSize = 11.Ssp, color = textColor),
        colors = if (isSelected) listOf(IptvAccent, IptvAccent) else listOf(white.opacity(7), white.opacity(7)),
        enabled = true,
        neonEnabled = isSelected
    )
}


// ─── Channels grid ────────────────────────────────────────────────────────────

@Composable
private fun PlayListChannelsGrid(
    channels: List<ChannelEntity>,
    onToggleFavorite: (Long) -> Unit,
    onOpenChannel: (ChannelEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.Sdp, end = 16.Sdp, top = 4.Sdp, bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(10.Sdp),
        verticalArrangement = Arrangement.spacedBy(14.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            PlayListChannelGridCard(
                channel = channel,
                onToggleFavorite = { onToggleFavorite(channel.id) },
                onOpenChannel = { onOpenChannel(channel) }
            )
        }
    }
}

@Composable
private fun PlayListChannelGridCard(
    channel: ChannelEntity,
    onToggleFavorite: () -> Unit,
    onOpenChannel: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(10.dp))
                .background(white.opacity(10))
                .onClick(onClick = onOpenChannel)
        ) {
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.None,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_channel),
                        contentDescription = null,
                        tint = IptvTextSecondary,
                        modifier = Modifier.size(32.Sdp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(7.dp)
                    .size(30.Sdp)
                    .background(if (channel.isFavorite) IptvAccent else white, CircleShape)
                    .onClick(onClick = onToggleFavorite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(if (channel.isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add),
                    contentDescription = null,
                    tint = if (channel.isFavorite) white else Color(0xFF555555),
                    modifier = Modifier.size(15.Sdp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            channel.name,
            style = headline600(fontSize = 11.Ssp, color = IptvTextPrimary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─── Channels list ────────────────────────────────────────────────────────────

@Composable
private fun PlayListChannelsList(
    channels: List<ChannelEntity>,
    onToggleFavorite: (Long) -> Unit,
    onOpenChannel: (ChannelEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.Sdp, end = 16.Sdp, top = 4.Sdp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(8.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            PlayListChannelListRow(
                channel = channel,
                onToggleFavorite = { onToggleFavorite(channel.id) },
                onOpenChannel = { onOpenChannel(channel) }
            )
        }
    }
}

@Composable
private fun PlayListChannelListRow(
    channel: ChannelEntity,
    onToggleFavorite: () -> Unit,
    onOpenChannel: () -> Unit
) {
    Row(
        modifier = Modifier
            .onClick(onClick = onOpenChannel)
            .fillMaxWidth()
            .background(white.opacity(5), RoundedCornerShape(12.dp))
            .border(1.dp, IptvDivider, RoundedCornerShape(12.dp))
            .padding(10.Sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.Sdp)
    ) {
        Box(
            modifier = Modifier
                .size(52.Sdp)
                .background(white.opacity(10), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_channel),
                    contentDescription = null,
                    tint = IptvTextSecondary,
                    modifier = Modifier.size(26.Sdp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                channel.name,
                style = headline600(fontSize = 13.Ssp, color = IptvTextPrimary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!channel.groupName.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(IptvAccent.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.Sdp, vertical = 2.Sdp)
                ) {
                    Text(
                        channel.groupName,
                        style = headline400(fontSize = 9.Ssp, color = IptvAccent)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(36.Sdp)
                .background(
                    if (channel.isFavorite) IptvAccent.copy(alpha = 0.12f) else white.opacity(8),
                    CircleShape
                )
                .border(
                    1.dp,
                    if (channel.isFavorite) IptvAccent.copy(alpha = 0.3f) else IptvDivider,
                    CircleShape
                )
                .onClick(onClick = onToggleFavorite),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(if (channel.isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add),
                contentDescription = null,
                tint = if (channel.isFavorite) IptvAccent else IptvTextSecondary,
                modifier = Modifier.size(16.Sdp)
            )
        }
    }
}

// ─── Empty state ──────────────────────────────────────────────────────────────

@Composable
private fun PlayListEmptyState(hasFilter: Boolean) {
    val title = if (hasFilter) "No results found" else "No channels yet"
    val subtitle = if (hasFilter) "Try a different search or group filter"
        else "This playlist doesn't have any channels"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.Sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.Sdp)
                .background(white.opacity(8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_nav_channel),
                contentDescription = null,
                tint = IptvTextSecondary,
                modifier = Modifier.size(36.Sdp)
            )
        }
        Spacer(Modifier.height(16.Sdp))
        Text(title, style = headline600(fontSize = 15.Ssp, color = IptvTextPrimary))
        Spacer(Modifier.height(8.Sdp))
        Text(
            subtitle,
            style = headline400(fontSize = 12.Ssp, color = IptvTextSecondary),
            textAlign = TextAlign.Center,
            modifier = Modifier.paddingHorizontal(32.Sdp)
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@ResponsivePreview
@Composable
private fun PlayListGridPreview() {
    AppViewTheme {
        PlayListScreenContent(
            title = "My Playlist",
            isGridView = true,
            channels = ChannelEntity.fakeList(),
            availableGroups = listOf("News", "Entertainment", "Kids", "Regional")
        )
    }
}

@ResponsivePreview
@Composable
private fun PlayListListPreview() {
    AppViewTheme {
        PlayListScreenContent(
            title = "My Playlist",
            isGridView = false,
            channels = ChannelEntity.fakeList(),
            availableGroups = listOf("News", "Entertainment")
        )
    }
}
