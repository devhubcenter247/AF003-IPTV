package com.dongbv.alf_03_iptv.ui.main.home

import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.model.ContentType
import com.dongbv.alf_03_iptv.model.HomeChannelItem
import com.dongbv.alf_03_iptv.model.listDemoContent
import com.dongbv.alf_03_iptv.style.*
import com.dongbv.alf_03_iptv.ui.component.AppNeonButton
import com.dongbv.alf_03_iptv.ui.component.BlurView
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.mainColor
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeView
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onWatchNow: () -> Unit = {},
    onAddPlaylist: () -> Unit = {},
    onOpenSub: () -> Unit = {},
    onOpenChannel: (HomeChannelItem) -> Unit = {},
    onHowToAddChannel: () -> Unit = {},
    onBack: () -> Unit = {},
    nativeAdHolder: NativeAdHolderConfig? = null,
    nativePlacement: NativePlacement = NativePlacement.NativeSmallCtaRight,
) {
    val listHomeChannel by viewModel.listHomeChannelItem.collectAsStateWithLifecycle(emptyList())
    val currentSelectedType by viewModel.contentType.collectAsStateWithLifecycle(ContentType.ALL)
    HomeScreenContent(
        modifier = modifier,
        currentSelectedType = currentSelectedType,
        listHomeChannel = listHomeChannel,
        filterChanner = { type ->
            viewModel.selectContent(type)
            nativeAdHolder?.reload()
        },
        onWatchNow = onWatchNow,
        onOpenSub = onOpenSub,
        onAddPlaylist = onAddPlaylist,
        onOpenChannel = { homeChannelItem ->
            onOpenChannel(homeChannelItem)
        },
        onHowToAddChannel = onHowToAddChannel,
        onBack = onBack,
        nativeAdHolder = nativeAdHolder,
        nativePlacement = nativePlacement,
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    currentSelectedType: ContentType = ContentType.ALL,
    listHomeChannel: List<HomeChannelItem> = emptyList(),
    filterChanner: (ContentType) -> Unit = {},
    onWatchNow: () -> Unit = {},
    onAddPlaylist: () -> Unit = {},
    onOpenSub: () -> Unit = {},
    onOpenChannel: (HomeChannelItem) -> Unit = {},
    onHowToAddChannel: () -> Unit = {},
    onBack: () -> Unit = {},
    nativeAdHolder: NativeAdHolderConfig? = null,
    nativePlacement: NativePlacement = NativePlacement.NativeSmallCtaRight,
) {
    ColumnParentContent(
        onBack = onBack, modifier = modifier.statusBarPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeHeader(
                gotoInfo = onHowToAddChannel, onOpenSub = onOpenSub, modifier = Modifier
            )
            CompactHomeContent(
                modifier = Modifier.weight(1f),
                currentSelectedType = currentSelectedType,
                listHomeChannel = listHomeChannel,
                filterChannel = filterChanner,
                onWatchNow = onWatchNow,
                onAddPlaylist = onAddPlaylist,
                onOpenChannel = onOpenChannel,
                onHowToAddChannel = onHowToAddChannel,
                nativeAdHolder = nativeAdHolder,
                nativePlacement = nativePlacement,
            )
        }
    }
}

@Composable
private fun HomeHeader(
    modifier: Modifier = Modifier, onOpenSub: () -> Unit = {}, gotoInfo: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.Sdp)
            .then(modifier), verticalAlignment = Alignment.CenterVertically
    ) {
        TextView(
            stringResource(R.string.app_name),
            textAlign = TextAlign.Left,
            color = IptvTextPrimary,
            font = R.font.ss_700,
            textSize = 18.Ssp,
            modifier = Modifier.weight(1f)
        )

        if (remoteConfig.paywallEnabled && !AppPurchase.getInstance().isPurchased()) {
            IconView(
                item = R.drawable.ic_app_premium,
                icSize = 20.Sdp,
                modifier = Modifier.size(28.Sdp),
                tint = Color.Unspecified,
                onclick = onOpenSub
            )
        }

        IconView(
            item = R.drawable.ic_app_help,
            icSize = 20.Sdp,
            modifier = Modifier.size(28.Sdp),
            tint = white,
            onclick = gotoInfo
        )
    }
}

@Composable
private fun CompactHomeContent(
    modifier: Modifier,
    currentSelectedType: ContentType,
    listHomeChannel: List<HomeChannelItem>,
    filterChannel: (ContentType) -> Unit,
    onWatchNow: () -> Unit,
    onAddPlaylist: () -> Unit,
    onOpenChannel: (HomeChannelItem) -> Unit,
    onHowToAddChannel: () -> Unit = {},
    nativeAdHolder: NativeAdHolderConfig? = null,
    nativePlacement: NativePlacement = NativePlacement.NativeSmallCtaRight,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HomeBanner(
            onWatchNow = onWatchNow,
            modifier = Modifier
                .padding(horizontal = 12.Sdp)
                .paddingTop(10.dp)
                .fillMaxWidth()
                .height(180.dp)
        )

        HomeActionBar(
            currentSelectedType = currentSelectedType,
            filterChannel = filterChannel,
            onAddPlaylist = onAddPlaylist,
            modifier = Modifier.padding(
                top = 12.Sdp, start = 12.Sdp, end = 0.dp, bottom = 12.Sdp
            )
        )

        HomeChannelContent(
            modifier = Modifier.weight(1f),
            listHomeChannel = listHomeChannel,
            onOpenChannel = onOpenChannel,
            onHowToAddChannel = onHowToAddChannel,
            nativeAdHolder = nativeAdHolder,
            nativePlacement = nativePlacement,
        )
    }
}

@Composable
private fun HomeBanner(
    onWatchNow: () -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .onClick(onClick = onWatchNow)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, white.opacity(50), RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.img_home_design),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        AppNeonButton(
            text = stringResource(R.string.watch_now), onClick = onWatchNow, textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.ss_600)), fontSize = 12.Ssp
            ), shape = CircleShape, modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.Sdp)

        )
    }
}

@Composable
private fun HomeActionBar(
    currentSelectedType: ContentType,
    filterChannel: (ContentType) -> Unit,
    onAddPlaylist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        ButtonAction(
            icon = R.drawable.ic_app_add,
            isAdded = true,
            label = stringResource(R.string.add_new),
            modifier = Modifier.wrapContentWidth(),
            onClick = onAddPlaylist
        )
        HomeFilterButtons(
            currentSelectedType = currentSelectedType,
            filterChannel = filterChannel,
            modifier = Modifier
                .weight(1f)
                .paddingStart(12.Sdp)
        )
    }
}

@Composable
private fun HomeFilterButtons(
    currentSelectedType: ContentType,
    filterChannel: (ContentType) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.Sdp)
    ) {
        ButtonAction(
            icon = R.drawable.ic_app_all,
            isSelect = currentSelectedType == ContentType.ALL,
            label = stringResource(R.string.all),
            modifier = Modifier.wrapContentWidth()
        ) { filterChannel(ContentType.ALL) }
        ButtonAction(
            icon = R.drawable.ic_app_url,
            isSelect = currentSelectedType == ContentType.URL,
            label = stringResource(R.string.url),
            modifier = Modifier.wrapContentWidth()
        ) { filterChannel(ContentType.URL) }
        ButtonAction(
            icon = R.drawable.ic_app_file,
            isSelect = currentSelectedType == ContentType.FILE,
            label = stringResource(R.string.file),
            modifier = Modifier.wrapContentWidth()
        ) { filterChannel(ContentType.FILE) }
        ButtonAction(
            icon = R.drawable.ic_app_storage,
            isSelect = currentSelectedType == ContentType.GALLERY,
            label = stringResource(R.string.storage),
            modifier = Modifier.wrapContentWidth()
        ) { filterChannel(ContentType.GALLERY) }
    }
}

@Composable
private fun PlaylistSectionHeader(itemCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.playlists),
            style = headline600(fontSize = 18.Ssp, color = IptvTextPrimary),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = itemCount.toString(),
            style = headline600(fontSize = 12.Ssp, color = mainColor),
            modifier = Modifier
                .background(mainColor.copy(alpha = 0.14f), CircleShape)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun HomeChannelContent(
    modifier: Modifier,
    listHomeChannel: List<HomeChannelItem>,
    onOpenChannel: (HomeChannelItem) -> Unit,
    nativeAdHolder: NativeAdHolderConfig? = null,
    nativePlacement: NativePlacement = NativePlacement.NativeSmallCtaRight,
    onHowToAddChannel: () -> Unit = {}
) {
    if (listHomeChannel.isEmpty()) {
        EmptyChannel(modifier = modifier, openHowUse = onHowToAddChannel)
    } else {
        NativeComposeView(
            nativePreloadConfig = nativeAdHolder to nativePlacement,
            nativeModifier = Modifier,
            modifier = Modifier
                .paddingBottom(8.Sdp)
                .padding(vertical = 4.dp)
                .paddingHorizontal(10.Sdp)
                .clip(
                    RoundedCornerShape(10.Sdp)
                )
        )
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(start = 12.Sdp, end = 12.Sdp, bottom = 12.Sdp),
            verticalArrangement = Arrangement.spacedBy(12.Sdp)
        ) {
            itemsIndexed(
                items = listHomeChannel, key = { _, item -> item.id }) { index, item ->
                HomeChannelView(
                    homeChannel = item,
                    index = index,
                    modifier = Modifier.onClick { onOpenChannel(item) })
            }
            item {
                Spacer(modifier = Modifier.heightPercent(10f))
            }
        }
    }
}

private const val HOME_BANNER_ASPECT_RATIO = 1080f / 628f

@Composable
fun HomeChannelView(
    homeChannel: HomeChannelItem,
    index: Int,
    modifier: Modifier = Modifier,
    isTablet: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (isTablet) 76.dp else 0.dp)
            .background(white.opacity(5f), RoundedCornerShape(12.Sdp))
            .border(1.dp, white.opacity(8f), RoundedCornerShape(12.Sdp))
            .padding(
                horizontal = if (isTablet) 16.dp else 12.Sdp,
                vertical = if (isTablet) 12.dp else 8.Sdp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(if (isTablet) 20.Sdp else 22.Sdp)
                .background(IptvAccent, CircleShape)
                .border(1.dp, white, CircleShape), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${index + 1}",
                fontSize = if (isTablet) 12.Ssp else 11.Ssp,
                style = headline600(color = white),
                textAlign = TextAlign.Center
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = homeChannel.title,
                style = headline600(color = white),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .paddingStart(12.Sdp)
            )
            Text(
                text = "${homeChannel.count} " + stringResource(R.string.channel),
                style = headline400(color = colorSecondary),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .paddingStart(11.Sdp)
            )
        }
        IconView(
            item = homeChannel.icon,
            tint = colorSecondary,
            icSize = if (isTablet) 22.Sdp else 18.Sdp
        )
    }
}

@Composable
fun EmptyChannel(
    modifier: Modifier = Modifier, openHowUse: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(12.Sdp)
                .fillMaxWidth()
                .onClick(onClick = openHowUse)
                .background(white.opacity(25f), RoundedCornerShape(10.dp))
                .padding(12.Sdp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconView(
                item = R.drawable.ic_app_how_use, tint = white, icSize = 28.Sdp
            )
            Text(
                text = stringResource(R.string.how_to_add_new_channel),
                style = headline600(color = white),
                modifier = Modifier
                    .weight(1f)
                    .paddingStart(12.Sdp)
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
            val artworkSize = (maxWidth * 0.55f).coerceAtMost(260.dp)
            Box(
                modifier = Modifier.size(artworkSize), contentAlignment = Alignment.Center
            ) {
                BlurView(
                    brush = Brush.radialGradient(
                        listOf(IptvAccent, transparent),
                    ), shape = CircleShape
                )
                IconView(
                    item = R.drawable.ic_nav_channel, icSize = 120.Sdp, tint = white
                )
                Text(
                    text = stringResource(R.string.no_current_playlist),
                    style = headline400(color = white),
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }


        }

    }
}

@SuppressLint("RememberReturnType")
@Composable
fun ButtonAction(
    icon: Any,
    isSelect: Boolean = false,
    isAdded: Boolean = false,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val borderSelected = if (!isSelect) {
        Modifier.border(1.Sdp, if (isAdded) white else white.opacity(0.75f), CircleShape)
    } else {
        Modifier.border(
            1.Sdp, brush = Brush.linearGradient(
                colors = listOf(grdStart, gdEnd)
            ), CircleShape
        )
    }
    Row(
        modifier = modifier
            .paddingHorizontal(4.Sdp)
            .then(borderSelected)
            .paddingVertical(8.Sdp)
            .paddingHorizontal(8.Sdp)
            .onClick {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconView(
            item = icon, tint = white, icSize = 20.Sdp, modifier = Modifier.gradientContent(
                if (isSelect) listOf(
                    grdStart, grdEnd
                ) else {
                    if (isAdded) {
                        listOf(white, white)
                    } else {
                        listOf(white.opacity(0.75f), white.opacity(0.75f))
                    }

                }
            )
        )
        Text(
            text = label,
            fontSize = 12.Ssp,
            color = if (isSelect) IptvAccent else white.opacity(if (isAdded) 1f else 0.75f),
            modifier = Modifier.paddingStart(6.Sdp)
        )
    }
}

@ResponsivePreview
@Composable
fun HomeScreenMainPreview() {
    AppViewTheme {
        HomeScreenContent(
            listHomeChannel = listDemoContent,
            modifier = Modifier
                .backgroundResource(R.drawable.img_bg_app)
                .paddingTop(28.Sdp)
        )
    }
}

@ResponsivePreview
@Composable
fun HomeScreenEmptyMainPreview() {
    AppViewTheme {
        HomeScreenContent()
    }
}
