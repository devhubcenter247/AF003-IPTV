package com.dongbv.alf_03_iptv.ui.main.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.graphics.Brush
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
import com.dongbv.alf_03_iptv.ui.component.ChannelSearchBar
import com.dongbv.alf_03_iptv.ui.component.LayoutToggle
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.viewmodel.FavoriteViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.IptvTextTertiary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

// ─── Entry point ─────────────────────────────────────────────────────────────

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    openChanel: (ChannelEntity) -> Unit = {},
    viewModel: FavoriteViewModel = koinViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGroup by viewModel.selectedGroup.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()
    val favoriteChannels by viewModel.favoriteChannels.collectAsStateWithLifecycle()
    val historyChannels by viewModel.historyChannels.collectAsStateWithLifecycle()
    val allChannels by viewModel.allChannels.collectAsStateWithLifecycle()
    val availableGroups by viewModel.availableGroups.collectAsStateWithLifecycle()

    FavoriteScreenContent(
        modifier = modifier,
        selectedTab = selectedTab,
        searchQuery = searchQuery,
        selectedGroup = selectedGroup,
        isGridView = isGridView,
        favoriteChannels = favoriteChannels,
        historyChannels = historyChannels,
        allChannels = allChannels,
        openChanel = openChanel,
        availableGroups = availableGroups,
        onTabSelected = viewModel::setTab,
        onSearchQueryChanged = viewModel::setSearchQuery,
        onGroupSelected = viewModel::setGroupFilter,
        onToggleLayout = viewModel::toggleLayout,
        onToggleFavorite = viewModel::toggleFavorite,
        onRemoveFromHistory = viewModel::removeFromHistory
    )
}

// ─── Stateless content ───────────────────────────────────────────────────────

@Composable
private fun FavoriteScreenContent(
    modifier: Modifier = Modifier,
    selectedTab: Int = 0,
    searchQuery: String = "",
    selectedGroup: String? = null,
    isGridView: Boolean = true,
    favoriteChannels: List<ChannelEntity> = emptyList(),
    historyChannels: List<ChannelEntity> = emptyList(),
    allChannels: List<ChannelEntity> = emptyList(),
    availableGroups: List<String> = emptyList(),
    onTabSelected: (Int) -> Unit = {},
    openChanel: (ChannelEntity) -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onGroupSelected: (String?) -> Unit = {},
    onToggleLayout: () -> Unit = {},
    onToggleFavorite: (Long) -> Unit = {},
    onRemoveFromHistory: (Long) -> Unit = {}
) {
    val currentList = when (selectedTab) {
        0 -> favoriteChannels; 1 -> historyChannels; else -> allChannels
    }
    val hasFilter = searchQuery.isNotBlank() || selectedGroup != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .paddingTop(12.Sdp)
    ) {
       
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .paddingHorizontal(16.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val title = when (selectedTab) {
                0 -> "Favorites"; 1 -> "Watch History"; else -> "All Channels"
            }
            Text(
                text = title,
                style = headline700(fontSize = 18.Ssp, color = IptvTextPrimary),
                modifier = Modifier.weight(1f)
            )

            if (currentList.isNotEmpty()) {
                Text(
                    text = "${currentList.size} Channel",
                    style = headline600(fontSize = 12.Ssp, color = IptvAccent)
                )
            }
           
            if (selectedTab == 2) {
                Spacer(Modifier.width(10.Sdp))
                LayoutToggle(isGridView = isGridView, onToggle = onToggleLayout)
            }
        }

        Spacer(Modifier.height(12.Sdp))

       
        FavoriteTabBar(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier
                .fillMaxWidth()
                .paddingHorizontal(16.Sdp)
        )

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

       
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                val dir = if (targetState > initialState) 1 else -1
                (slideInHorizontally { it * dir } + fadeIn()) togetherWith (slideOutHorizontally { -it * dir } + fadeOut())
            },
            label = "favTab"
        ) { tab ->
            val list = when (tab) {
                0 -> favoriteChannels; 1 -> historyChannels; else -> allChannels
            }

            when {
                list.isEmpty() -> FavoriteEmptyState(tab = tab, hasFilter = hasFilter)
                tab == 2 && isGridView -> AllChannelsGrid(
                    channels = list,
                    onToggleFavorite = onToggleFavorite
                ) {
                    openChanel(it)
                }

                tab == 2 -> AllChannelsList(channels = list, onToggleFavorite = onToggleFavorite){
                    openChanel(it)
                }
                else -> FavoriteHistoryList(
                    channels = list,
                    isHistory = tab == 1,
                    onToggleFavorite = onToggleFavorite,
                    onRemoveFromHistory = onRemoveFromHistory
                ){
                    openChanel(it)
                }
            }
        }
    }
}

// ─── Tab bar ─────────────────────────────────────────────────────────────────

@Composable
private fun FavoriteTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        Pair(R.drawable.ic_nav_favorite, "Favorites"),
        Pair(R.drawable.ic_app_timer, "History"),
        Pair(R.drawable.ic_nav_channel, "All")
    )
    Row(
        modifier = modifier
            .background(white.opacity(5), RoundedCornerShape(50))
            .padding(4.Sdp),
        horizontalArrangement = Arrangement.spacedBy(4.Sdp)
    ) {
        tabs.forEachIndexed { index, (icon, label) ->
            val isSelected = selectedTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.Sdp)
                    .clip(RoundedCornerShape(50))
                    .then(
                        if (isSelected) Modifier.background(
                            Brush.horizontalGradient(
                                listOf(
                                    grdStart,
                                    grdEnd
                                )
                            )
                        ) else Modifier
                    )
                    .onClick { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.Sdp)
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (isSelected) white else IptvTextSecondary,
                        modifier = Modifier.size(15.Sdp)
                    )
                    Text(
                        text = label,
                        style = headline600(
                            fontSize = 12.Ssp,
                            color = if (isSelected) white else IptvTextSecondary
                        )
                    )
                }
            }
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
    val bg by animateColorAsState(
        if (isSelected) IptvAccent else white.opacity(7),
        tween(200),
        label = "chipBg"
    )
    val textColor by animateColorAsState(
        if (isSelected) white else IptvTextSecondary,
        tween(200),
        label = "chipText"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .border(1.dp, if (isSelected) IptvAccent else IptvDivider, RoundedCornerShape(50))
            .onClick(onClick = onClick)
            .padding(horizontal = 14.Sdp, vertical = 6.Sdp)
    ) {
        Text(label, style = headline600(fontSize = 11.Ssp, color = textColor))
    }
}

// ─── Favorites / History list ─────────────────────────────────────────────────

@Composable
private fun FavoriteHistoryList(
    channels: List<ChannelEntity>,
    isHistory: Boolean,
    onToggleFavorite: (Long) -> Unit,
    onRemoveFromHistory: (Long) -> Unit,
    openChanel: (ChannelEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.Sdp, end = 16.Sdp, top = 4.Sdp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(8.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            ChannelListCard(
                channel = channel,
                isHistory = isHistory,
                onToggleFavorite = { onToggleFavorite(channel.id) },
                onRemoveFromHistory = { onRemoveFromHistory(channel.id) }
            ){

            }
        }
    }
}

@Composable
private fun ChannelListCard(
    channel: ChannelEntity,
    isHistory: Boolean,
    onToggleFavorite: () -> Unit,
    onRemoveFromHistory: () -> Unit,
    openChanel: (ChannelEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .onClick{
                openChanel(channel)
            }
            .fillMaxWidth()
            .background(white.opacity(5), RoundedCornerShape(12.dp))
            .border(1.dp, IptvDivider, RoundedCornerShape(12.dp))
            .padding(12.Sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.Sdp)
    ) {
       
        Box(
            modifier = Modifier
                .size(50.Sdp)
                .background(white.opacity(10), RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .size(44.Sdp)
                        .clip(RoundedCornerShape(8.dp))
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
            Spacer(Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.Sdp)
            ) {
                if (!channel.groupName.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .background(
                                IptvAccent.copy(alpha = 0.15f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.Sdp, vertical = 2.Sdp)
                    ) {
                        Text(
                            channel.groupName,
                            style = headline400(fontSize = 9.Ssp, color = IptvAccent)
                        )
                    }
                }
                if (isHistory && channel.lastWatched != null) {
                    Text(
                        relativeTime(channel.lastWatched),
                        style = headline400(fontSize = 10.Ssp, color = IptvTextTertiary)
                    )
                }
            }
        }

       
        val actionIcon =
            if (isHistory) R.drawable.ic_app_delete else if (channel.isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add
        val actionTint =
            if (isHistory) IptvTextSecondary else if (channel.isFavorite) IptvAccent else IptvTextSecondary
        Box(
            modifier = Modifier
                .size(36.Sdp)
                .background(
                    if (channel.isFavorite && !isHistory) IptvAccent.copy(alpha = 0.12f) else white.opacity(
                        8
                    ), CircleShape
                )
                .border(
                    1.dp,
                    if (channel.isFavorite && !isHistory) IptvAccent.copy(alpha = 0.3f) else IptvDivider,
                    CircleShape
                )
                .onClick(onClick = if (isHistory) onRemoveFromHistory else onToggleFavorite),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(actionIcon),
                contentDescription = null,
                tint = actionTint,
                modifier = Modifier.size(16.Sdp)
            )
        }
    }
}

// ─── All Channels grid ────────────────────────────────────────────────────────

@Composable
private fun AllChannelsGrid(
    channels: List<ChannelEntity>,
    onToggleFavorite: (Long) -> Unit,
    openChanel: (ChannelEntity) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.Sdp, end = 16.Sdp, top = 4.Sdp, bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(10.Sdp),
        verticalArrangement = Arrangement.spacedBy(14.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            AllChannelGridCard(
                channel = channel,
                onToggleFavorite = { onToggleFavorite(channel.id) }) {
                openChanel(channel)
            }
        }
    }
}

@Composable
private fun AllChannelGridCard(
    channel: ChannelEntity,
    onToggleFavorite: () -> Unit,
    openChanel: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(10.dp))
                .background(white.opacity(10))
        ) {
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
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
           
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(7.dp)
                    .size(26.Sdp)
                    .onClick{
                        openChanel()
                    }
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_menu),
                    contentDescription = null,
                    tint = white,
                    modifier = Modifier.size(14.Sdp)
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

// ─── All Channels list ────────────────────────────────────────────────────────

@Composable
private fun AllChannelsList(channels: List<ChannelEntity>, onToggleFavorite: (Long) -> Unit,openChanel: (ChannelEntity) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.Sdp, end = 16.Sdp, top = 4.Sdp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(8.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            AllChannelListRow(
                channel = channel,
                onToggleFavorite = { onToggleFavorite(channel.id) }){
openChanel(it)
            }
        }
    }
}

@Composable
private fun AllChannelListRow(
    channel: ChannelEntity,
    onToggleFavorite: () -> Unit,
    openChanel: (ChannelEntity) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .onClick {
                openChanel(channel)
            }
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
                        .background(
                            IptvAccent.copy(alpha = 0.15f),
                            RoundedCornerShape(4.dp)
                        )
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
                    if (channel.isFavorite) IptvAccent.copy(alpha = 0.12f) else white.opacity(
                        8
                    ), CircleShape
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

        Box(
            modifier = Modifier
                .size(36.Sdp)
                .background(white.opacity(8), CircleShape)
                .border(1.dp, IptvDivider, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_menu),
                contentDescription = null,
                tint = IptvTextSecondary,
                modifier = Modifier.size(16.Sdp)
            )
        }
    }
}

// ─── Empty state ──────────────────────────────────────────────────────────────

@Composable
private fun FavoriteEmptyState(tab: Int, hasFilter: Boolean) {
    val icon = when (tab) {
        0 -> R.drawable.ic_nav_favorite; 1 -> R.drawable.ic_app_timer; else -> R.drawable.ic_nav_channel
    }
    val title = when {
        hasFilter -> "No results found"
        tab == 0 -> "No favorites yet"
        tab == 1 -> "No watch history yet"
        else -> "No channels yet"
    }
    val subtitle = when {
        hasFilter -> "Try a different search or group filter"
        tab == 0 -> "Tap the heart on any channel to save it here"
        tab == 1 -> "Channels you watch will appear here"
        else -> "Add a playlist from the Home tab to get started"
    }
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
                painter = painterResource(icon),
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

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun relativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m ago"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h ago"
        diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}d ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@ResponsivePreview
@Composable
private fun FavoritesPreview() {
    AppViewTheme {
        FavoriteScreenContent(
            selectedTab = 0,
            favoriteChannels = ChannelEntity.fakeList().filter { it.isFavorite },
            availableGroups = listOf("News", "Entertainment", "Kids")
        )
    }
}

@ResponsivePreview
@Composable
private fun AllChannelsGridPreview() {
    AppViewTheme {
        FavoriteScreenContent(
            selectedTab = 2,
            isGridView = true,
            allChannels = ChannelEntity.fakeList(),
            availableGroups = listOf("News", "Entertainment", "Kids", "Regional")
        )
    }
}

@ResponsivePreview
@Composable
private fun AllChannelsListPreview() {
    AppViewTheme {
        FavoriteScreenContent(
            selectedTab = 2,
            isGridView = false,
            allChannels = ChannelEntity.fakeList(),
            availableGroups = listOf("News", "Entertainment")
        )
    }
}
