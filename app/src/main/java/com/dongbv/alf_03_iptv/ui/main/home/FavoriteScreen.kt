package com.dongbv.alf_03_iptv.ui.main.home

import com.dongbv.alf_03_iptv.analytics.AppScreen
import com.dongbv.alf_03_iptv.analytics.TrackScreen
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.ui.component.ChannelSearchBar
import com.dongbv.alf_03_iptv.ui.component.LayoutToggle
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.backgroundState
import com.dongbv.alf_03_iptv.ui.component.borderState
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.viewmodel.FavoriteViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
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
import androidx.compose.ui.platform.LocalLocale

@Stable
interface FavoriteScreenActions {
    fun onTabSelected(tab: Int)
    fun onSearchQueryChanged(query: String)
    fun onGroupSelected(group: String?)
    fun onToggleLayout()
    fun onToggleFavorite(channelId: Long)
    fun onRemoveFromHistory(channelId: Long)

    companion object {
        val None = object : FavoriteScreenActions {
            override fun onTabSelected(tab: Int) = Unit
            override fun onSearchQueryChanged(query: String) = Unit
            override fun onGroupSelected(group: String?) = Unit
            override fun onToggleLayout() = Unit
            override fun onToggleFavorite(channelId: Long) = Unit
            override fun onRemoveFromHistory(channelId: Long) = Unit
        }
    }
}


@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    openChanel: (ChannelEntity) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: FavoriteViewModel = koinViewModel()
) {
    BackHandler(true, onBack)
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    TrackScreen(
        when (selectedTab) {
            0 -> AppScreen.FAVORITE_SAVED
            1 -> AppScreen.FAVORITE_HISTORY
            else -> AppScreen.FAVORITE_ALL
        }
    )
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGroup by viewModel.selectedGroup.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()
    val favoriteChannels by viewModel.favoriteChannels.collectAsStateWithLifecycle()
    val historyChannels by viewModel.historyChannels.collectAsStateWithLifecycle()
    val allChannels by viewModel.allChannels.collectAsStateWithLifecycle()
    val availableGroups by viewModel.availableGroups.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        object : FavoriteScreenActions {
            override fun onTabSelected(tab: Int) {
                viewModel.setTab(tab)
            }

            override fun onSearchQueryChanged(query: String) {
                viewModel.setSearchQuery(query)
            }

            override fun onGroupSelected(group: String?) {
                viewModel.setGroupFilter(group)
            }

            override fun onToggleLayout() {
                viewModel.toggleLayout()
            }

            override fun onToggleFavorite(channelId: Long) {
                viewModel.toggleFavorite(channelId)
            }

            override fun onRemoveFromHistory(channelId: Long) {
                viewModel.removeFromHistory(channelId)
            }
        }
    }

    FavoriteScreenContent(
        modifier = modifier,
        selectedTab = selectedTab,
        searchQuery = searchQuery,
        selectedGroup = selectedGroup,
        isGridView = isGridView,
        favoriteChannels = favoriteChannels,
        historyChannels = historyChannels,
        allChannels = allChannels,
        availableGroups = availableGroups,
        onOpenChannel = openChanel,
        actions = actions
    )
}


@Composable
fun FavoriteScreenContent(
    modifier: Modifier = Modifier,
    selectedTab: Int = 0,
    searchQuery: String = "",
    selectedGroup: String? = null,
    isGridView: Boolean = true,
    favoriteChannels: List<ChannelEntity> = emptyList(),
    historyChannels: List<ChannelEntity> = emptyList(),
    allChannels: List<ChannelEntity> = emptyList(),
    availableGroups: List<String> = emptyList(),
    onOpenChannel: (ChannelEntity) -> Unit = {},
    actions: FavoriteScreenActions = FavoriteScreenActions.None
) {
    val currentList = when (selectedTab) {
        0 -> favoriteChannels; 1 -> historyChannels; else -> allChannels
    }
    val hasFilter = searchQuery.isNotBlank() || selectedGroup != null

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp
        val horizontalPadding = if (isTablet) 24.dp else 16.Sdp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .backgroundResource(R.drawable.img_bg_app)
                .then(modifier)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingHorizontal(horizontalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val title = when (selectedTab) {
                    0 -> stringResource(R.string.favorites)
                    1 -> stringResource(R.string.watch_history_title)
                    else -> stringResource(R.string.all_channels_title)
                }
                Text(
                    text = title,
                    style = headline600(
                        fontSize = if (isTablet) 20.Ssp else 18.Ssp,
                        color = IptvTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                if (currentList.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.channel_count_format, currentList.size),
                        style = headline600(fontSize = 12.Ssp, color = IptvAccent)
                    )
                    Spacer(Modifier.width(10.Sdp))
                    LayoutToggle(
                        isGridView = isGridView,
                        onToggle = actions::onToggleLayout
                    )
                } else {
                    LayoutToggle(
                        isGridView = isGridView,
                        modifier = Modifier
                            .alpha(0f)
                            .width(0.dp),
                        onToggle = {}
                    )
                }
            }

            Spacer(Modifier.height(12.Sdp))


            if (isTablet) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(horizontalPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FavoriteTabBar(
                        selectedTab = selectedTab,
                        onTabSelected = actions::onTabSelected,
                        isTablet = isTablet,
                        modifier = Modifier
                    )
                    ChannelSearchBar(
                        query = searchQuery,
                        isTablet = true,
                        onQueryChanged = actions::onSearchQueryChanged,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                FavoriteTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = actions::onTabSelected,
                    isTablet = isTablet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(horizontalPadding)
                )

                Spacer(Modifier.height(12.Sdp))

                ChannelSearchBar(
                    query = searchQuery,
                    isTablet = false,
                    onQueryChanged = actions::onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(horizontalPadding)
                )
            }


            if (availableGroups.isNotEmpty()) {
                Spacer(Modifier.height(10.Sdp))
                GroupFilterRow(
                    groups = availableGroups,
                    selectedGroup = selectedGroup,
                    horizontalPadding = horizontalPadding,
                    onGroupSelected = actions::onGroupSelected
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
                        isTablet = isTablet,
                        horizontalPadding = horizontalPadding,
                        onToggleFavorite = actions::onToggleFavorite
                    ) {
                        onOpenChannel(it)
                    }

                    tab == 2 -> AllChannelsList(
                        channels = list,
                        isTablet = isTablet,
                        horizontalPadding = horizontalPadding,
                        onToggleFavorite = actions::onToggleFavorite,
                        openChanel = onOpenChannel
                    )

                    isGridView -> FavoriteHistoryGrid(
                        channels = list,
                        isHistory = tab == 1,
                        isTablet = isTablet,
                        horizontalPadding = horizontalPadding,
                        onToggleFavorite = actions::onToggleFavorite,
                        onRemoveFromHistory = actions::onRemoveFromHistory,
                        openChanel = onOpenChannel
                    )

                    else -> FavoriteHistoryList(
                        channels = list,
                        isHistory = tab == 1,
                        isTablet = isTablet,
                        horizontalPadding = horizontalPadding,
                        onToggleFavorite = actions::onToggleFavorite,
                        onRemoveFromHistory = actions::onRemoveFromHistory,
                        openChanel = onOpenChannel
                    )
                }
            }
        }
    }
}


@Composable
private fun FavoriteTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isTablet: Boolean = false,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        Triple(R.drawable.ic_nav_channel, stringResource(R.string.all), 2),
        Triple(R.drawable.ic_nav_favorite, stringResource(R.string.favorites), 0),
        Triple(R.drawable.ic_app_timer, stringResource(R.string.history), 1),
    )
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .then(if (isTablet) Modifier.widthIn(max = 420.dp) else Modifier.fillMaxWidth())
                .background(white.opacity(5), RoundedCornerShape(50))
                .padding(4.Sdp),
            horizontalArrangement = Arrangement.spacedBy(4.Sdp)
        ) {
            tabs.forEach { (icon, label, index) ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .then(if (isTablet) Modifier.width(132.dp) else Modifier.weight(1f))
                        .height(if (isTablet) 28.Sdp else 38.Sdp)
                        .neonState(
                            neonEnabled = isSelected,
                            outerRadius = 6.Sdp,
                            shape = RoundedCornerShape(50)
                        )
                        .backgroundState(enabled = isSelected, CircleShape)
                        .clip(RoundedCornerShape(50))
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
                            tint = if (isSelected) white else colorSecondary,
                            modifier = Modifier.size(if (isTablet) 16.dp else 15.Sdp)
                        )
                        Text(
                            text = label,
                            style = headline600(
                                fontSize = if (isTablet) 12.sp else 12.Ssp,
                                color = if (isSelected) white else colorSecondary
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun GroupFilterRow(
    groups: List<String>,
    selectedGroup: String?,
    horizontalPadding: Dp = 16.Sdp,
    onGroupSelected: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .paddingHorizontal(horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(8.Sdp)
    ) {
        GroupChip(stringResource(R.string.all), selectedGroup == null) { onGroupSelected(null) }
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
        if (isSelected) white else colorSecondary,
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


@Composable
private fun FavoriteHistoryList(
    channels: List<ChannelEntity>,
    isHistory: Boolean,
    isTablet: Boolean,
    horizontalPadding: Dp = 16.Sdp,
    onToggleFavorite: (Long) -> Unit,
    onRemoveFromHistory: (Long) -> Unit,
    openChanel: (ChannelEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = if (isTablet) GridCells.Adaptive(minSize = 300.dp) else GridCells.Fixed(1),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 4.Sdp,
            bottom = 90.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.Sdp),
        verticalArrangement = Arrangement.spacedBy(8.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "native_ad_view") {

        }
        items(channels, key = { it.id }) { channel ->
            ChannelListCard(
                channel = channel,
                isHistory = isHistory,
                isTablet = isTablet,
                onToggleFavorite = { onToggleFavorite(channel.id) },
                onRemoveFromHistory = { onRemoveFromHistory(channel.id) },
                openChanel = openChanel
            )
        }
    }
}

@Composable
private fun ChannelListCard(
    channel: ChannelEntity,
    isHistory: Boolean,
    isTablet: Boolean = false,
    onToggleFavorite: () -> Unit,
    onRemoveFromHistory: () -> Unit,
    openChanel: (ChannelEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .onClick {
                openChanel(channel)
            }
            .background(white.opacity(5), RoundedCornerShape(12.dp))
            .border(1.dp, IptvDivider, RoundedCornerShape(12.dp))
            .padding(if (isTablet) 12.dp else 12.Sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (isTablet) 12.dp else 12.Sdp)
    ) {

        Box(
            modifier = Modifier
                .size(if (isTablet) 46.dp else 50.Sdp)
                .background(white.opacity(10), RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            var isLogoLoadError by remember(channel.logo) { mutableStateOf(false) }
            if (!channel.logo.isNullOrBlank() && !isLogoLoadError) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    onState = { state -> isLogoLoadError = state is AsyncImagePainter.State.Error },
                    modifier = Modifier
                        .size(if (isTablet) 40.dp else 44.Sdp)
                        .clip(RoundedCornerShape(8.dp))

                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_channel),
                    contentDescription = null,
                    tint = colorSecondary,
                    modifier = Modifier.size(if (isTablet) 22.Sdp else 26.Sdp)
                )
            }
        }


        Column(modifier = Modifier.weight(1f)) {
            Text(
                channel.name,
                style = headline600(
                    fontSize = if (isTablet) 13.sp else 13.Ssp,
                    color = IptvTextPrimary
                ),
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
                            style = headline400(
                                fontSize = if (isTablet) 9.sp else 9.Ssp,
                                color = IptvAccent
                            )
                        )
                    }
                }
                if (isHistory && channel.lastWatched != null) {
                    Text(
                        relativeTime(channel.lastWatched),
                        style = headline400(
                            fontSize = if (isTablet) 10.sp else 10.Ssp,
                            color = IptvTextTertiary
                        )
                    )
                }
            }
        }


        val actionIcon =
            if (isHistory) R.drawable.ic_app_delete else if (channel.isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add
        val actionTint =
            if (isHistory) colorSecondary else if (channel.isFavorite) IptvAccent else colorSecondary
        Box(
            modifier = Modifier
                .size(if (isTablet) 34.dp else 36.Sdp)
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
                modifier = Modifier.size(if (isTablet) 15.dp else 16.Sdp)
            )
        }
    }
}

@Composable
private fun FavoriteHistoryGrid(
    channels: List<ChannelEntity>,
    isHistory: Boolean,
    isTablet: Boolean,
    horizontalPadding: Dp = 16.Sdp,
    onToggleFavorite: (Long) -> Unit,
    onRemoveFromHistory: (Long) -> Unit,
    openChanel: (ChannelEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = if (isTablet) GridCells.Adaptive(minSize = 150.dp) else GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 4.Sdp,
            bottom = 90.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.Sdp),
        verticalArrangement = Arrangement.spacedBy(14.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            FavoriteHistoryGridCard(
                channel = channel,
                isHistory = isHistory,
                isTablet = isTablet,
                onToggleFavorite = { onToggleFavorite(channel.id) },
                onRemoveFromHistory = { onRemoveFromHistory(channel.id) },
                openChanel = { openChanel(channel) }
            )
        }
    }
}

@Composable
private fun FavoriteHistoryGridCard(
    channel: ChannelEntity,
    isHistory: Boolean,
    isTablet: Boolean = false,
    onToggleFavorite: () -> Unit,
    onRemoveFromHistory: () -> Unit,
    openChanel: () -> Unit
) {
    Column(
        modifier = Modifier.onClick(onClick = openChanel)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(10.dp))
                .background(white.opacity(10))
        ) {

            var isLogoLoadError by remember(channel.logo) { mutableStateOf(false) }
            if (!channel.logo.isNullOrBlank() && !isLogoLoadError) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    onState = { state -> isLogoLoadError = state is AsyncImagePainter.State.Error },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.Sdp)

                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_channel),
                        contentDescription = null,
                        tint = colorSecondary,
                        modifier = Modifier.size(if (isTablet) 28.dp else 32.Sdp)
                    )
                }
            }

            val actionIcon =
                if (isHistory) R.drawable.ic_app_delete else if (channel.isFavorite) R.drawable.ic_app_favorite_add else R.drawable.ic_app_favorite_minus
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(7.dp)
                    .size(if (isTablet) 20.dp else 24.Sdp)
                    .backgroundState(false,CircleShape)
                    .onClick(onClick = if (isHistory) onRemoveFromHistory else onToggleFavorite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(actionIcon),
                    contentDescription = null,
                    tint = if (channel.isFavorite && !isHistory) Color.Red else Color(0xFF555555),
                    modifier = Modifier.size(if (isTablet) 13.dp else 15.Sdp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            channel.name,
            style = headline600(
                fontSize = if (isTablet) 11.sp else 11.Ssp,
                color = IptvTextPrimary
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (isHistory && channel.lastWatched != null) {
            Text(
                relativeTime(channel.lastWatched),
                style = headline400(
                    fontSize = if (isTablet) 9.sp else 9.Ssp,
                    color = IptvTextTertiary
                )
            )
        }
    }
}


@Composable
private fun AllChannelsGrid(
    channels: List<ChannelEntity>,
    isTablet: Boolean,
    horizontalPadding: Dp = 16.Sdp,
    onToggleFavorite: (Long) -> Unit,
    openChanel: (ChannelEntity) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = if (isTablet) GridCells.Adaptive(minSize = 150.dp) else GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 4.Sdp,
            bottom = 90.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.Sdp),
        verticalArrangement = Arrangement.spacedBy(14.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            AllChannelGridCard(
                channel = channel,
                isTablet = isTablet,
                onToggleFavorite = { onToggleFavorite(channel.id) }) {
                openChanel(channel)
            }
        }
    }
}

@Composable
private fun AllChannelGridCard(
    channel: ChannelEntity,
    isTablet: Boolean = false,
    onToggleFavorite: () -> Unit,
    openChanel: () -> Unit
) {
    Column(
        modifier = Modifier.onClick(onClick = openChanel)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(10.dp))
                .background(white.opacity(10))
        ) {
            var isLogoLoadError by remember(channel.logo) { mutableStateOf(false) }
            if (!channel.logo.isNullOrBlank() && !isLogoLoadError) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    onState = { state -> isLogoLoadError = state is AsyncImagePainter.State.Error },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.Sdp)

                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_channel),
                        contentDescription = null,
                        tint = colorSecondary,
                        modifier = Modifier.size(if (isTablet) 28.dp else 32.Sdp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(7.dp)
                    .size(if (isTablet) 20.dp else 24.Sdp)
                    .backgroundState(false,CircleShape)
                    .onClick(onClick = onToggleFavorite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(if (channel.isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add),
                    contentDescription = null,
                    tint = if (channel.isFavorite) Color.Red else Color(0xFF555555),
                    modifier = Modifier.size(if (isTablet) 13.dp else 15.Sdp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(7.dp)
                    .size(if (isTablet) 22.dp else 26.Sdp)
                    .onClick {
                        openChanel()
                    }
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_menu),
                    contentDescription = null,
                    tint = white,
                    modifier = Modifier.size(if (isTablet) 12.dp else 14.Sdp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            channel.name,
            style = headline600(
                fontSize = if (isTablet) 11.sp else 11.Ssp,
                color = IptvTextPrimary
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}


@Composable
private fun AllChannelsList(
    channels: List<ChannelEntity>,
    isTablet: Boolean,
    horizontalPadding: Dp = 16.Sdp,
    onToggleFavorite: (Long) -> Unit,
    openChanel: (ChannelEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = if (isTablet) GridCells.Adaptive(minSize = 300.dp) else GridCells.Fixed(1),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 4.Sdp,
            bottom = 90.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.Sdp),
        verticalArrangement = Arrangement.spacedBy(8.Sdp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels, key = { it.id }) { channel ->
            AllChannelListRow(
                channel = channel,
                isTablet = isTablet,
                onToggleFavorite = { onToggleFavorite(channel.id) }) {
                openChanel(it)
            }
        }
    }
}

@Composable
private fun AllChannelListRow(
    channel: ChannelEntity,
    isTablet: Boolean = false,
    onToggleFavorite: () -> Unit,
    openChanel: (ChannelEntity) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .onClick {
                openChanel(channel)
            }
            .background(white.opacity(5), RoundedCornerShape(12.dp))
            .border(1.dp, IptvDivider, RoundedCornerShape(12.dp))
            .padding(if (isTablet) 10.dp else 10.Sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (isTablet) 12.dp else 12.Sdp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isTablet) 48.dp else 52.Sdp)
                .background(white.opacity(10), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            var isLogoLoadError by remember(channel.logo) { mutableStateOf(false) }
            if (!channel.logo.isNullOrBlank() && !isLogoLoadError) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    onState = { state -> isLogoLoadError = state is AsyncImagePainter.State.Error },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.Sdp)

                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_channel),
                    contentDescription = null,
                    tint = colorSecondary,
                    modifier = Modifier.size(if (isTablet) 22.dp else 26.Sdp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                channel.name,
                style = headline600(
                    fontSize = if (isTablet) 13.sp else 13.Ssp,
                    color = IptvTextPrimary
                ),
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
                        style = headline400(
                            fontSize = if (isTablet) 9.sp else 9.Ssp,
                            color = IptvAccent
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(7.dp)
                .size(if (isTablet) 20.dp else 24.Sdp)
                .backgroundState(false,CircleShape)
                .onClick(onClick = onToggleFavorite),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(if (channel.isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add),
                contentDescription = null,
                tint = if (channel.isFavorite) Color.Red else Color(0xFF555555),
                modifier = Modifier.size(if (isTablet) 13.dp else 15.Sdp)
            )
        }

        Box(
            modifier = Modifier
                .size(if (isTablet) 34.dp else 36.Sdp)
                .background(white.opacity(8), CircleShape)
                .border(1.dp, IptvDivider, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_menu),
                contentDescription = null,
                tint = colorSecondary,
                modifier = Modifier.size(if (isTablet) 15.dp else 16.Sdp)
            )
        }
    }
}


@Composable
private fun FavoriteEmptyState(tab: Int, hasFilter: Boolean) {
    val icon = when (tab) {
        0 -> R.drawable.ic_app_favorite_add; 1 -> R.drawable.ic_app_timer; else -> R.drawable.ic_nav_channel
    }
    val title = when {
        hasFilter -> stringResource(R.string.no_results_found)
        tab == 0 -> stringResource(R.string.no_favorites_yet)
        tab == 1 -> stringResource(R.string.no_watch_history_yet)
        else -> stringResource(R.string.no_channels_yet)
    }
    val subtitle = when {
        hasFilter -> stringResource(R.string.try_different_search_or_group_filter)
        tab == 0 -> stringResource(R.string.tap_heart_to_save_favorite)
        tab == 1 -> stringResource(R.string.channels_you_watch_will_appear_here)
        else -> stringResource(R.string.add_playlist_from_home_tab_to_get_started)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.Sdp),
        verticalArrangement = Arrangement.Center,
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
                tint = colorSecondary,
                modifier = Modifier.size(36.Sdp)
            )
        }
        Spacer(Modifier.height(16.Sdp))
        Text(title, style = headline600(fontSize = 15.Ssp, color = IptvTextPrimary))
        Spacer(Modifier.height(8.Sdp))
        Text(
            subtitle,
            style = headline400(fontSize = 12.Ssp, color = colorSecondary),
            textAlign = TextAlign.Center,
            modifier = Modifier.paddingHorizontal(32.Sdp)
        )
    }
}


@Composable
private fun relativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> stringResource(R.string.time_just_now)
        diff < TimeUnit.HOURS.toMillis(1) -> stringResource(
            R.string.time_minutes_ago,
            TimeUnit.MILLISECONDS.toMinutes(diff)
        )

        diff < TimeUnit.DAYS.toMillis(1) -> stringResource(
            R.string.time_hours_ago,
            TimeUnit.MILLISECONDS.toHours(diff)
        )

        diff < TimeUnit.DAYS.toMillis(7) -> stringResource(
            R.string.time_days_ago,
            TimeUnit.MILLISECONDS.toDays(diff)
        )

        else -> SimpleDateFormat("MMM d", LocalLocale.current.platformLocale).format(Date(timestamp))
    }
}


@ResponsivePreview
@Composable
private fun FavoritesPreview() {
    AppViewTheme {
        FavoriteScreenContent(
            selectedTab = 0,
            modifier = Modifier.paddingTop(28.Sdp),
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
            modifier = Modifier.paddingTop(28.Sdp),
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
            modifier = Modifier.paddingTop(28.Sdp),
            availableGroups = listOf("News", "Entertainment")
        )
    }
}
