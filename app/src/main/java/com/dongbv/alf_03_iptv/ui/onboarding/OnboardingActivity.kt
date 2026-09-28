package com.dongbv.alf_03_iptv.ui.onboarding

import com.dongbv.alf_03_iptv.analytics.TrackScreen
import com.dongbv.alf_03_iptv.analytics.AppScreen
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil3.compose.AsyncImage
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.AdPreloadManager
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeAdColors
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeFullView
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeViewOnboarding
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeShimmerByLayout
import com.dongbv.alf_03_iptv.app.isNetworkAvailable
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.SpaceF
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.paddingEnd
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.transparent
import com.dongbv.alf_03_iptv.ui.personalization.PersonalizationActivity
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdDisplayState
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.createNativeAdHolder
import com.lib.ads.gma.ads.helper.appopen.AppOpenManager
import com.lib.ads.gma.ads.helper.utils.AdOptionVisibility
import com.lib.ads.gma.ads.model.wrapper.ApNativeAd
import com.lib.ads.gma.ads.model.wrapper.NativeAdListener
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class IntroActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.ONBOARDING

    override fun initialize() {
        AdPreloadManager.preloadNativeAdConfig(
            NativePlacement.NativeFeature,1
        )
    }
    private var requestedNativeAdSlideIndex: Int? = null

    val nativeAdHolder: NativeAdHolderConfig by createNativeAdHolder(
        enabled = NativePlacement.Onboarding.canShowAds,
        scope = lifecycleScope,
        tag = NativePlacement.Onboarding.tag,
        options = NativeAdPreloadHolderOptions(
            adOptionVisibility = AdOptionVisibility.INVISIBLE,
            fallbackAdUnitIds = NativePlacement.Onboarding.listId,
            autoRequestOnStart = false,
            autoReloadOnResume = true
        )
    )
    val nativeFullAdHolder: NativeAdHolderConfig by createNativeAdHolder(
        enabled = NativePlacement.OnboardingFull.canShowAds &&
            !AppPurchase.getInstance().isPurchased(),
        scope = lifecycleScope,
        tag = NativePlacement.OnboardingFull.tag,
        options = NativeAdPreloadHolderOptions(
            fallbackAdUnitIds = NativePlacement.OnboardingFull.listId,
            autoRequestOnStart = false,
            autoReloadOnResume = true
        )
    )

    private fun isShowNativeFullScreen1(): Boolean {
        return remoteConfig.nativeObFscrConfig.enable
                && NativePlacement.OnboardingFull.listId.any { it.isNotBlank() }
                && isNetworkAvailable()
                && !AppPurchase.getInstance().isPurchased()
    }

    @Composable
    override fun ContentView() {
        val listIntroModel by remember {
            mutableStateOf(
                setupOnboardingPage(
                    isShowNativeFullScreen1(),
                    remoteConfig.onboardingScreen
                )
            )
        }
        val pagerState = rememberPagerState(initialPage = 0) { listIntroModel.size }
        val settledPage = pagerState.settledPage
        val pageScreen = if (listIntroModel[settledPage] is OnboardingPage.FullAd) {
            AppScreen.ONBOARDING_AD
        } else {
            val slideNumber = listIntroModel.take(settledPage + 1).count { it is OnboardingPage.Slide }
            when (slideNumber) {
                1 -> AppScreen.ONBOARDING_1
                2 -> AppScreen.ONBOARDING_2
                3 -> AppScreen.ONBOARDING_3
                else -> AppScreen.ONBOARDING_4
            }
        }
        TrackScreen(pageScreen)

        val scope = rememberCoroutineScope()
        BoxParentContent(onBack = {
            val pre = pagerState.currentPage
            if (pre == 0) {
                finishAffinity()
            } else {

                val target = nearestSlideIndex(listIntroModel, pre, -1)
                scope.launch { pagerState.animateScrollToPage(target) }
            }
        }) {
            IntroScreen(
                pagerState = pagerState,
                nativeAdHolder = nativeAdHolder to NativePlacement.Onboarding,
                nativeFullAdHolder = nativeFullAdHolder to NativePlacement.OnboardingFull,
                scope = scope,
                items = listIntroModel,
                modifier = Modifier.background(IptvBgPrimary),
                nextScreen = {
                    launchActivity<PersonalizationActivity>()
                    finish()
                },
                checkLoadAds = { page ->
                    checkLoadAds(page)
                },
            )
        }
    }

    private fun checkLoadAds(page: OnboardingPage) {
        when (page) {
            is OnboardingPage.FullAd -> {
                AppOpenManager.getInstance().disableAppResume()
                cancelAds()
            }

            is OnboardingPage.Slide -> {
                if (remoteConfig.appOpenAdConfig.enable) {
                    AppOpenManager.getInstance().enableAppResume()
                }

                if (isNetworkAvailable() && !AppPurchase.getInstance()
                        .isPurchased() && page.canShowAd
                ) {
                    requestAds(page.index)
                } else {
                    cancelAds()
                }
            }
        }
    }

    private fun requestAds(slideIndex: Int) {
        if (requestedNativeAdSlideIndex == slideIndex) return

        if (requestedNativeAdSlideIndex == null) {
            nativeAdHolder.request()
        } else {
            nativeAdHolder.reload()
        }
        requestedNativeAdSlideIndex = slideIndex
    }

    private fun cancelAds() {
        nativeAdHolder.cancel()
        requestedNativeAdSlideIndex = null
    }
}

fun nearestSlideIndex(items: List<OnboardingPage>, fromIndex: Int, direction: Int): Int {
    var t = fromIndex + direction
    while (t in items.indices && items[t] !is OnboardingPage.Slide) t += direction
    if (t !in items.indices) {

        t = fromIndex - direction
        while (t in items.indices && items[t] !is OnboardingPage.Slide) t -= direction
    }
    return t.coerceIn(0, items.lastIndex)
}


@Composable
fun DotIndicator(
    totalPage: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPage) { index ->
            val width by animateDpAsState(
                targetValue = if (index == currentPage) 16.Sdp else 8.Sdp,
                animationSpec = tween(220),
                label = "indicator_width"
            )

            val colors = if (index == currentPage) listOf(grdStart, grdEnd) else listOf(
                colorSecondary.copy(alpha = 0.4f), colorSecondary.copy(alpha = 0.4f)
            )
            Box(
                modifier = Modifier
                    .size(width = width, height = 8.Sdp)
                    .background(
                        brush = Brush.horizontalGradient(colors),
                        shape = CircleShape
                    )
            )

            if (index != totalPage - 1) {
                SpaceW(6.Sdp)
            }
        }
    }
}


@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun IntroPreview() {
    AppViewTheme {
        val pagerState = rememberPagerState(initialPage = 1) { setupOnboardingPage(false).size }
        IntroScreen(
            pagerState = pagerState,
            scope = rememberCoroutineScope(),
            items = setupOnboardingPage(),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(
    pagerState: PagerState,
    scope: CoroutineScope,
    nativeAdHolder: Pair<NativeAdHolderConfig, NativePlacement>? = null,
    nativeFullAdHolder: Pair<NativeAdHolderConfig, NativePlacement>? = null,
    modifier: Modifier = Modifier,
    items: List<OnboardingPage> = setupOnboardingPage(),
    checkLoadAds: (OnboardingPage) -> Unit = {},
    nextScreen: () -> Unit = {},
) {
    val page by remember(items) { derivedStateOf { pagerState.currentPage } }
    val onboardingModel by remember(items) {
        derivedStateOf { items[page.coerceIn(0, items.lastIndex)] }
    }
    val overscroll = rememberOverscrollEffect()
    var lastPage by remember { mutableIntStateOf(-1) }

    var navDirection by remember { mutableIntStateOf(0) }
    var isNavigating by remember { mutableStateOf(false) }

    var scrollJob by remember { mutableStateOf<Job?>(null) }
    val latestCheckLoadAds by rememberUpdatedState(checkLoadAds)
    val navigateTo: (Int) -> Unit = remember(pagerState, items, scope) {
        { targetPage ->
            if (targetPage in items.indices && targetPage != pagerState.currentPage) {
                scrollJob?.cancel()
                scrollJob = scope.launch {
                    isNavigating = true
                    try {
                        pagerState.animateScrollToPage(targetPage)
                    } finally {
                        isNavigating = false
                    }
                }
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { current ->
            if (current != lastPage) {
                if (lastPage != -1) navDirection = if (current > lastPage) 1 else -1
                lastPage = current
                latestCheckLoadAds(items[current])
            }
        }
    }

    ConstraintLayout(
        modifier.fillMaxSize()
    ) {
        val (swipe, _, content) = createRefs()
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = !isNavigating,
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(swipe) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .overscroll(overscroll),
            beyondViewportPageCount = 0
        ) { index ->

            val isCurrentPage = pagerState.currentPage == index
            when (items[index]) {
                is OnboardingPage.FullAd -> {
                    NativeFullScreenView(
                        nativeFullAdHolder = nativeFullAdHolder?.first,
                        isActive = isCurrentPage,
                        modifier = Modifier.fillMaxSize(1f)
                    ) {

                        if (isCurrentPage) {
                            val dir = if (navDirection == 0) 1 else navDirection
                            navigateTo(nearestSlideIndex(items, index, dir))
                        }
                    }
                }

                is OnboardingPage.Slide -> {
                    IntroParallaxPage(
                        index,
                        pagerState,
                        model = items[index] as OnboardingPage.Slide,
                        modifier = Modifier.fillMaxSize().animateContentSize()
                    )
                }
            }
        }

        if (onboardingModel is OnboardingPage.Slide) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(content) {
                        bottom.linkTo(parent.bottom)
                    }
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                IptvBgPrimary.copy(alpha = 0.1f),
                                IptvBgPrimary,
                                IptvBgPrimary,
                                IptvBgPrimary,
                            )
                        )
                    )
                    .paddingTop(12.Sdp)
                    .animateContentSize()
            )
            {
                TextView(
                    text = stringResource((onboardingModel as OnboardingPage.Slide).title),
                    font = Font(R.font.ss_700),
                    textSize = 22.Ssp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF00F5FF), // Cyan
                            Color(0xFF4FAAF2), // Sky blue
                            Color(0xFF3639FF), // Indigo
                            Color(0xFFC596FF), // Purple
                            Color(0xFFE363FF)
                        )
                    ),
                    minLine = 1,
                    maxLine = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(16.Sdp)
                )

                TextView(
                    text = stringResource((onboardingModel as OnboardingPage.Slide).content),
                    font = Font(R.font.ss_400),
                    textSize = 12.Ssp,
                    color = colorSecondary,
                    modifier = Modifier
                        .paddingTop(6.Sdp)
                        .paddingHorizontal(16.Sdp),
                    minLine = 2,
                    maxLine = 2
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(16.Sdp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {
                    DotIndicator(
                        totalPage = items.count { it is OnboardingPage.Slide },
                        currentPage = items.take(page).count { it is OnboardingPage.Slide },
                        modifier = Modifier
                    )
                    SpaceF()
                    TextView(
                        text = if (page != items.lastIndex) {
                            stringResource(R.string.next)
                        } else {
                            stringResource(R.string.get_started)
                        },
                        font = R.font.ss_600,
                        textSize = 13.Ssp,
                        color = IptvAccent,
                        modifier = Modifier
                            .heightIn(min = 32.Sdp)
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) {
                        if (page >= items.lastIndex) {
                            nextScreen()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        }
                    }

                }
                if ((onboardingModel as OnboardingPage.Slide).canShowAd){
                    NativeComposeViewOnboarding(
                        nativePreloadConfig = nativeAdHolder,
                        nativeModifier = Modifier.background(
                            if ((onboardingModel as OnboardingPage.Slide).canShowAd)  NativeAdColors.colorBgNativeAds
                            else transparent
                        )
                    )
                } else{
                    NativeShimmerByLayout(NativePlacement.Onboarding, false, Modifier.fillMaxWidth())
                }

            }
        }
    }
}

private fun isLastSlide(items: List<OnboardingPage>, page: Int): Boolean {
    if (page !in items.indices || items[page] !is OnboardingPage.Slide) return false
    for (i in (page + 1)..items.lastIndex) {
        if (items[i] is OnboardingPage.Slide) return false
    }
    return true
}

@Composable
fun NativeFullScreenView(
    nativeFullAdHolder: NativeAdHolderConfig? = null,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    onNextPage: () -> Unit = {}
) {
    var isShowSkip by remember(nativeFullAdHolder) { mutableStateOf(false) }
    var hasRequestedNext by remember(nativeFullAdHolder, isActive) { mutableStateOf(false) }
    val latestOnNextPage by rememberUpdatedState(onNextPage)
    val adState = nativeFullAdHolder?.state?.collectAsStateWithLifecycle()?.value
    val adCallback = remember(nativeFullAdHolder) {
        object : NativeAdListener {
            override fun onImpression(ad: ApNativeAd) {
                super.onImpression(ad)
                isShowSkip = true
            }
        }
    }

    LaunchedEffect(nativeFullAdHolder, adState, isActive) {
        val unavailable = nativeFullAdHolder == null ||
            adState is NativeAdDisplayState.Error || adState is NativeAdDisplayState.Cancelled
        if (isActive && unavailable && !hasRequestedNext) {
            hasRequestedNext = true
            latestOnNextPage()
        }
    }

    DisposableEffect(nativeFullAdHolder) {
        nativeFullAdHolder?.registerAdCallback(adCallback)
        nativeFullAdHolder?.request()
        onDispose {
            nativeFullAdHolder?.unregisterAdCallback(adCallback)
            nativeFullAdHolder?.cancel()
        }
    }

    Box(modifier) {
        NativeComposeFullView(
            nativePreloadConfig = nativeFullAdHolder,
            modifier = Modifier.fillMaxSize()
        )

        if (isShowSkip) {
            TextView(
                text = stringResource(R.string.skip),
                color = Color.LightGray,
                modifier = Modifier
                    .paddingTop(32.Sdp)
                    .paddingEnd(16.Sdp)
                    .border(width = 1.Sdp, Color.LightGray, CircleShape)
                    .padding(horizontal = 16.Sdp, vertical = 4.Sdp)
                    .align(Alignment.TopEnd)
            ) {
                if (isActive && !hasRequestedNext) {
                    hasRequestedNext = true
                    latestOnNextPage()
                }
            }
        }
    }
}


@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun NativeFullScreen2AdViewPreview() {
    AppViewTheme {
        NativeFullScreenView()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroParallaxPage(
    page: Int, pagerState: PagerState, model: OnboardingPage.Slide, modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    Box(modifier.fillMaxSize()) {
        if (model.image != 0) {
            AsyncImage(
                model.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        val rawOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        translationX = rawOffset * 24f * density.density
                    }
            )
        }
    }
}
