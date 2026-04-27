package com.dongbv.alf_03_iptv.ui.main.home

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.dongbv.alf_03_iptv.model.HomeChannelItem
import com.dongbv.alf_03_iptv.model.listDemoContent
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ColumnParentContent
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.*
import com.dongbv.alf_03_iptv.ui.component.BlurView
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.mainColor

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    gotoInfo: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val listHomeChannel by viewModel.listHomeChannel.collectAsStateWithLifecycle(emptyList())

    HomeScreenContent(
        modifier = modifier,
        listHomeChannel = listHomeChannel,
        gotoInfo = gotoInfo,
        onBack = onBack
    )
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    listHomeChannel: List<HomeChannelItem> = emptyList(),
    gotoInfo: () -> Unit = {},
    onBack: () -> Unit = {}
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

    ColumnParentContent(onBack = onBack, modifier = modifier.background(IptvBgPrimary)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .paddingHorizontal(_12Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                stringResource(R.string.app_name),
                textAlign = TextAlign.Left,
                color = IptvTextPrimary,
                font = R.font.ss_800,
                textSize = _18Ssp,
                modifier = Modifier.weight(1f)
            )
            IconView(
                item = R.drawable.ic_app_info,
                icSize = 24,
                modifier = Modifier.size(_28Sdp),
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
                    .paddingHorizontal(_12Sdp)
                    .paddingTop(_10Sdp)
                    .clip(RoundedCornerShape(_10Sdp))
                    .border(_1Sdp, white.opacity(50), RoundedCornerShape(_10Sdp))
            ) {
                Image(
                    painter = painterResource(R.drawable.img_home_design),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image Home"
                )
                TextView(
                    text = stringResource(R.string.watch_now),
                    modifier = Modifier
                        .padding(_12Sdp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(grdStart, grdEnd)
                            ),
                            RoundedCornerShape(50)
                        )
                        .align(Alignment.BottomStart)
                        .padding(horizontal = _12Sdp, vertical = _8Sdp),
                    textSize = _12Ssp,
                    color = white,
                    font = R.font.ss_600
                )
            }

            Row(
                modifier = Modifier
                    .layoutId("tabBar")
                    .paddingVertical(_12Sdp)
                    .paddingStart(_12Sdp)
                    .height(_37Sdp)
            ) {
                ButtonAction(
                    icon = Icons.Rounded.AddCircle,
                    label = stringResource(R.string.add_new),
                    modifier = Modifier.wrapContentWidth()
                ) { }

                Row(
                    modifier = Modifier
                        .weight(0.8f)
                        .paddingStart(_12Sdp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    ButtonAction(
                        icon = R.drawable.ic_app_all,
                        label = stringResource(R.string.all),
                        modifier = Modifier.wrapContentWidth()
                    ) { }
                    ButtonAction(
                        icon = R.drawable.ic_app_url,
                        label = stringResource(R.string.url),
                        modifier = Modifier
                            .wrapContentWidth()
                            .paddingStart(_12Sdp)
                    ) { }
                    ButtonAction(
                        icon = R.drawable.ic_app_file,
                        label = stringResource(R.string.file),
                        modifier = Modifier
                            .wrapContentWidth()
                            .paddingStart(_12Sdp)
                    ) { }
                    ButtonAction(
                        icon = R.drawable.ic_app_storage,
                        label = stringResource(R.string.storage),
                        modifier = Modifier
                            .wrapContentWidth()
                            .paddingStart(_12Sdp)
                    ) { }
                }
            }

            Column(
                modifier = Modifier
                    .layoutId("content"),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listHomeChannel.contentIfEmpty(
                    transform = {
                        EmptyChannel()
                    },
                    defaultValue = {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(1f),
                            state = rememberLazyListState()
                        ) {
                            items(items = listHomeChannel, key = { it.id }) { item ->
                                HomeChannelView(item)
                            }
                        }

                    }
                )
            }
        }
    }
}

@Composable
fun HomeChannelView(homeChannel: HomeChannelItem) {

}

@Composable
fun EmptyChannel() {
    Row(
        modifier = Modifier
            .padding(_12Sdp)
            .fillMaxWidth()
            .background(white.opacity(25f), RoundedCornerShape(_10Sdp))
            .padding(_12Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconView(
            item = R.drawable.ic_app_how_use,
            tint = white,
            icSize = 28
        )
        Text(
            text = stringResource(R.string.how_to_add_new_channel),
            style = headline600(color = white),
            modifier = Modifier.fillMaxWidth()
        )
    }

    Box(Modifier.size(widthPercentDp(70f)), contentAlignment = Alignment.Center) {
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
        text = "No current playlist",
        style = headline400(color = white),
        modifier = Modifier.offset(y = -(getScreenHeight() * 0.025f).dp)
    )
}

@Composable
fun ButtonAction(
    icon: Any,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .border(_1Sdp, white, CircleShape)
            .height(_37Sdp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = _10Sdp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = icon,
                tint = white,
                icSize = 24,
            )
            Text(
                text = label,
                color = white,
                modifier = Modifier.paddingStart(_6Sdp)
            )
        }
        Box(Modifier.matchParentSize()) {
            onClick()
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
        HomeScreenContent(listHomeChannel = emptyList())
    }
}