package com.dongbv.alf_03_iptv.ui.main.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.model.ContentType
import com.dongbv.alf_03_iptv.model.HomeChannelItem
import com.dongbv.alf_03_iptv.model.listDemoContent
import com.dongbv.alf_03_iptv.style.*
import com.dongbv.alf_03_iptv.ui.component.AppNeonButton
import com.dongbv.alf_03_iptv.ui.component.BlurView
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.mainColor
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    gotoInfo: () -> Unit = {},
    onWatchNow: () -> Unit = {},
    onAddPlaylist: () -> Unit = {},
    onOpenChannel: (HomeChannelItem) -> Unit ={},
    onBack: () -> Unit = {}
) {
    val listHomeChannel by viewModel.listHomeChannelItem.collectAsStateWithLifecycle(emptyList())
    val currentSelectedType by viewModel.contentType.collectAsStateWithLifecycle(ContentType.ALL)
    HomeScreenContent(
        modifier = modifier,
        currentSelectedType = currentSelectedType,
        listHomeChannel = listHomeChannel,
        filterChanner = { type -> viewModel.selectContent(type) },
        gotoInfo = gotoInfo,
        onWatchNow = onWatchNow,
        onAddPlaylist = onAddPlaylist,
        onOpenChannel = { homeChannelItem ->
            onOpenChannel(homeChannelItem)
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    currentSelectedType: ContentType = ContentType.ALL,
    listHomeChannel: List<HomeChannelItem> = emptyList(),
    filterChanner: (ContentType) -> Unit = {},
    onWatchNow: () -> Unit = {},
    gotoInfo: () -> Unit = {},
    onAddPlaylist: () -> Unit = {},
    onOpenChannel: (HomeChannelItem) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val bannerHeightDp = 180.dp
    val bannerHeightPx = with(density) { bannerHeightDp.toPx() }
    val targetProgress by remember {
        derivedStateOf {
            (scrollState.value / bannerHeightPx).coerceIn(0f, 1f)
        }
    }
    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 180),
        label = "motionProgress"
    )

    ColumnParentContent(onBack = onBack, modifier = modifier.backgroundResource(R.drawable.img_bg_app)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .paddingHorizontal(12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                stringResource(R.string.app_name),
                textAlign = TextAlign.Left,
                color = IptvTextPrimary,
                font = R.font.ss_800,
                textSize = 18.Ssp,
                modifier = Modifier.weight(1f)
            )
            IconView(
                item = R.drawable.ic_app_info,
                icSize = 24,
                modifier = Modifier.size(28.Sdp),
                tint = white
            ) {
                gotoInfo()
            }
        }

        MotionLayout(
            start = ConstraintSet {
                val banner = createRefFor("banner")
                val tabBar = createRefFor("tabBar")
                val content = createRefFor("content")

                constrain(banner) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(bannerHeightDp)
                    alpha = 1f
                }
                constrain(tabBar) {
                    top.linkTo(banner.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                constrain(content) {
                    top.linkTo(tabBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            },
            end = ConstraintSet {
                val banner = createRefFor("banner")
                val tabBar = createRefFor("tabBar")
                val content = createRefFor("content")

                constrain(banner) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(0.dp)
                    alpha = 0f
                }
                constrain(tabBar) {
                    top.linkTo(banner.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                constrain(content) {
                    top.linkTo(tabBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            },
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .layoutId("banner")
                    .paddingHorizontal(12.Sdp)
                    .paddingTop(10.dp)
                    .onClick{
                        onWatchNow()
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.Sdp, white.opacity(50), RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.img_home_design),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image Home"
                )
                AppNeonButton(
                    text = stringResource(R.string.watch_now),
                    onClick = onWatchNow,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(12.Sdp)
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 12.Sdp, vertical = 8.Sdp),
                )
            }

            Row(
                modifier = Modifier
                    .layoutId("tabBar")
                    .paddingVertical(12.Sdp)
                    .paddingStart(12.Sdp)
                    .height(37.Sdp)
            ) {
                ButtonAction(
                    icon = R.drawable.ic_rounded_add,
                    isAdded = true,
                    label = stringResource(R.string.add_new),
                    modifier = Modifier.wrapContentWidth()
                ) { onAddPlaylist() }

                Row(
                    modifier = Modifier
                        .weight(0.8f)
                        .paddingStart(12.Sdp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    ButtonAction(
                        icon = R.drawable.ic_app_all,
                        isSelect = currentSelectedType == ContentType.ALL,
                        label = stringResource(R.string.all),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        filterChanner(ContentType.ALL)
                    }
                    ButtonAction(
                        icon = R.drawable.ic_app_url,
                        isSelect = currentSelectedType == ContentType.URL,
                        label = stringResource(R.string.url),
                        modifier = Modifier
                            .wrapContentWidth()
                            .paddingStart(12.Sdp)
                    ) {
                        filterChanner(ContentType.URL)
                    }
                    ButtonAction(
                        icon = R.drawable.ic_app_file,
                        isSelect = currentSelectedType == ContentType.FILE,
                        label = stringResource(R.string.file),
                        modifier = Modifier
                            .wrapContentWidth()
                            .paddingStart(12.Sdp)
                    ) {
                        filterChanner(ContentType.FILE)
                    }
                    ButtonAction(
                        icon = R.drawable.ic_app_storage,
                        isSelect = currentSelectedType == ContentType.GALLERY,
                        label = stringResource(R.string.storage),
                        modifier = Modifier
                            .wrapContentWidth()
                            .paddingStart(12.Sdp)
                    ) {
                        filterChanner(ContentType.GALLERY)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .layoutId("content"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(listHomeChannel.isEmpty()){
                    EmptyChannel()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(1f),
                        state = rememberLazyListState()
                    ) {
                        itemsIndexed(
                            items = listHomeChannel,
                            key = { index, item -> item.id }) { index, item ->
                            HomeChannelView(
                                item, index,
                                modifier = Modifier.onClick {
                                    onOpenChannel(item)
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeChannelView(homeChannel: HomeChannelItem, index: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .paddingHorizontal(12.Sdp)
            .paddingBottom(12.Sdp)
            .fillMaxWidth()
            .background(white.opacity(5f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.Sdp, vertical = 8.Sdp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(22.Sdp), contentAlignment = Alignment.Center) {
            BlurView(
                brush = Brush.radialGradient(
                    listOf(mainColor, mainColor),
                ), CircleShape
            )
            Text(
                text = "$index",
                fontSize = 11.Ssp,
                style = headline600(color = white),
                textAlign = TextAlign.Center,

                modifier = Modifier
                    .matchParentSize()
                    .background(mainColor, CircleShape)
                    .border(1.dp, white, CircleShape)
                    .paddingTop(4.Sdp)
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
                style = headline400(color = IptvTextSecondary),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .paddingStart(11.Sdp)
            )
        }

    }
}

@Composable
fun EmptyChannel(openHowUse: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(12.Sdp)
            .fillMaxWidth()
            .onClick{
                openHowUse()
            }
            .background(white.opacity(25f), RoundedCornerShape(10.dp))
            .padding(12.Sdp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconView(
            item = R.drawable.ic_app_how_use,
            tint = white,
            icSize = 28
        )
        Text(
            text = stringResource(R.string.how_to_add_new_channel),
            style = headline600(color = white),
            modifier = Modifier
                .weight(1f)
                .paddingStart(12.Sdp)
        )
    }

    Box(
        Modifier
            .size(widthPercentDp(70f))
            .paddingTop(40.Sdp), contentAlignment = Alignment.Center
    ) {
        BlurView(
            brush = Brush.radialGradient(
                listOf(mainColor, transparent),
            ), CircleShape
        )
        IconView(
            item = R.drawable.ic_nav_channel,
            icSize = 120,
            tint = white
        )
    }
    Text(
        text = stringResource(R.string.no_current_playlist),
        style = headline400(color = white),
        modifier = Modifier.offset(y = -(getScreenHeight() * 0.025f).dp)
    )
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

    Box(
        modifier = modifier
            .wrapContentWidth()
            .height(34.Sdp)
            .then(borderSelected)
            .onClick{
                onClick()
            }
            .paddingHorizontal(4.Sdp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = icon,
                tint = white,
                icSize = 24,
                modifier = Modifier.gradientContent(
                    if (isSelect) listOf(
                        grdStart,
                        grdEnd
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
                color = if (isSelect) mainColor else white.opacity(if (isAdded) 1f else 0.75f),
                modifier = Modifier.paddingStart(6.Sdp)
            )
        }
    }
}

@ResponsivePreview
@Composable
fun HomeScreenMainPreview() {
    AppViewTheme {
        HomeScreenContent(listHomeChannel = listDemoContent)
    }
}

@ResponsivePreview
@Composable
fun HomeScreenEmptyMainPreview() {
    AppViewTheme {
        HomeScreenContent()
    }
}