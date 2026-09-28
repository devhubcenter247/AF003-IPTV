package com.dongbv.alf_03_iptv.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.inappupdate.AppUpdateWrapper
import com.app.inappupdate.InAppUpdateManager
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.wrapper.banner.rememberBannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.banner.BannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.interstitial.InterstitialAdConfig
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.analytics.AppScreen
import com.dongbv.alf_03_iptv.analytics.TrackScreen
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.model.listDemoContent
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.paddingBottom
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.ui.component.DialogQuitApp
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.ui.iap.IapActivity
import com.dongbv.alf_03_iptv.ui.main.add.AddChannelActivity
import com.dongbv.alf_03_iptv.ui.main.guide.HowToAddChannelActivity
import com.dongbv.alf_03_iptv.ui.main.home.ChannelScreen
import com.dongbv.alf_03_iptv.ui.main.home.FavoriteScreen
import com.dongbv.alf_03_iptv.ui.main.home.FavoriteScreenContent
import com.dongbv.alf_03_iptv.ui.main.home.HomeScreen
import com.dongbv.alf_03_iptv.ui.main.home.HomeScreenContent
import com.dongbv.alf_03_iptv.ui.main.home.SettingScreen
import com.dongbv.alf_03_iptv.ui.main.play.PlayChannelActivity
import com.dongbv.alf_03_iptv.ui.main.playlist.PlayListActivity
import com.dongbv.alf_03_iptv.ui.main.setting.AboutUsActivity
import com.dongbv.alf_03_iptv.ui.main.setting.LanguageSettingActivity
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowActions
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowActivity
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowContent
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.widget.BottomNavigationBar
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.animComposable
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.createNativeAdHolder
import com.lib.ads.gma.ads.helper.banner.params.BannerSize
import com.lib.ads.gma.ads.helper.banner.preload.BannerAdCard
import com.sdp.ssp.android.Sdp
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.MAIN

    private val nativeAdHolder: NativeAdHolderConfig by createNativeAdHolder(
        enabled = !AppPurchase.getInstance()
            .isPurchased() && NativePlacement.NativeSmallCtaRight.canShowAds,
        scope = lifecycleScope,
        tag = "main_list_native",
        options = NativeAdPreloadHolderOptions(
            fallbackAdUnitIds = NativePlacement.NativeSmallCtaRight.listId,
            lifecycleOwner = this@MainActivity,
            autoReloadOnResume = true,
            autoRequestOnStart = false,
        )
    )


    private val updateManager by lazy {
        InAppUpdateManager(
            appUpdateWrapper = AppUpdateWrapper(AppUpdateManagerFactory.create(this))
        )
    }
    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        updateManager.onUpdateFlowResult(result.resultCode)
    }

    override fun initialize() {
        AppPreferences.isFinishFirstFlow = true
        lifecycle.addObserver(updateManager)
        updateManager.registerLauncher(updateLauncher)
        updateManager.checkForUpdate(
            remoteConfig.buildUpdateConfig(
                defaultTitle = getString(R.string.new_ver_avaliable),
                defaultMessage = getString(R.string.please_update_to_use),
                defaultUpdateText = getString(R.string.update_now),
                defaultDismissText = getString(R.string.latter),
            )
        )
        InterstitialAdConfig.loadInterAll(this)
        if (!AppPurchase.getInstance().isPurchased() && NativePlacement.NativeSmallAll.canShowAds) {
            nativeAdHolder.request()
        }
    }

    override fun onResume() {
        super.onResume()
        if (InterstitialAdConfig.consumePendingAfterPlayer()) {
            InterstitialAdConfig.showInterAll(this) {}
        }
    }

    private val viewModel: HomeViewModel by inject()
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            else -> {

                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @Composable
    override fun ContentView() {
        val bannerConfig = rememberBannerPlacement(
            BannerPlacement.BannerAll,
            autoReloadOnResume = true,
            bannerSize = BannerSize.InlineAdaptive()
        )
        val isCloseInter by InterstitialAdConfig.isCloseInterAdManager.collectAsStateWithLifecycle(
            false
        )
        LaunchedEffect(Unit, isCloseInter) {
            if (isCloseInter) {
                requestNotificationPermissionIfNeeded()
            }
        }
        var isShowQuitDialog by rememberSaveable { mutableStateOf(false) }
        val navController = rememberNavController()
        val currentEntry by navController.currentBackStackEntryAsState()
        val currentScreen = when (currentEntry?.destination?.route) {
            ScreenRouter.MainScreen.HomeScreen.route -> AppScreen.HOME
            ScreenRouter.MainScreen.ChannelScreen.route -> AppScreen.CHANNEL
            ScreenRouter.MainScreen.FavoriteScreen.route -> AppScreen.FAVORITE
            ScreenRouter.MainScreen.SettingScreen.route -> AppScreen.SETTING
            else -> null
        }
        currentScreen?.let { TrackScreen(it) }


        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxSize()
                    .backgroundResource(R.drawable.img_bg_app)
        ) {
            val (layout, nav, banner) = createRefs()
            NavHost(
                modifier = Modifier.constrainAs(layout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(nav.top, (-16).Sdp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                },
                navController = navController,
                startDestination = ScreenRouter.MainScreen.HomeScreen.route
            )
            {
                animComposable(ScreenRouter.MainScreen.HomeScreen.route) {
                    HomeScreen(
                        modifier = Modifier,
                        viewModel = viewModel,
                        nativeAdHolder = nativeAdHolder,
                        nativePlacement = NativePlacement.NativeSmallCtaRight,
                        onWatchNow = { navigateWithInterstitial { launchActivity<WatchNowActivity>() } },
                        onOpenSub = {
                            IapActivity.open(this@MainActivity)
                        },
                        onOpenChannel = { item ->
                            navigateWithInterstitial {
                                startActivity(
                                    PlayListActivity.newIntent(this@MainActivity, item.id.toLong())
                                )
                            }
                        },
                        onAddPlaylist = { navigateWithInterstitial { launchActivity<AddChannelActivity>() } },
                        onHowToAddChannel = { navigateWithInterstitial { launchActivity<HowToAddChannelActivity>() } },
                        onBack = { isShowQuitDialog = true }
                    )
                }
                animComposable(ScreenRouter.MainScreen.ChannelScreen.route) {
                    ChannelScreen {
                        isShowQuitDialog = true
                    }
                }
                animComposable(ScreenRouter.MainScreen.FavoriteScreen.route) {
                    FavoriteScreen(
                        modifier = Modifier.statusBarPadding(),
                        onBack = { isShowQuitDialog = true },
                        openChanel = { cn ->
                            val newIntent = PlayChannelActivity.newIntent(
                                this@MainActivity,
                                cn.id,
                                cn.name,
                                cn.url,
                                cn.logo,
                                cn.playlistId
                            )
                            navigateWithInterstitial { startActivity(newIntent) }
                        })

                }
                animComposable(ScreenRouter.MainScreen.SettingScreen.route) {
                    SettingScreen(
                        modifier = Modifier.statusBarPadding(),
                        gotoLangSetting = { navigateWithInterstitial { launchActivity<LanguageSettingActivity>() } },
                        gotoAboutUs = { navigateWithInterstitial { launchActivity<AboutUsActivity>() } },
                        gotoHowToAddChannel = { navigateWithInterstitial { launchActivity<HowToAddChannelActivity>() } }
                    ) {
                        isShowQuitDialog = true
                    }
                }
            }
            BottomNavigationBar(
                navHostController = navController, modifier = Modifier
                    .padding(horizontal = 16.Sdp)
                    .paddingBottom(12.Sdp)
                    .constrainAs(nav) {
                        bottom.linkTo(banner.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .height(54.Sdp)
                    .neonState(true, shape = CircleShape, outerRadius = 6.Sdp)
                    .background(IptvBgSecondary, CircleShape)
            )
            if (bannerConfig != null || BannerPlacement.BannerAll.canShowAds) {
                BannerAdCard(
                    holder = bannerConfig,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(banner) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.dp)
                        .constrainAs(banner) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
            }
        }

        DialogQuitApp(
            isShowDialog = isShowQuitDialog,
            onDismiss =
                { isShowQuitDialog = false }
        ) { finishAffinity() }
    }
}


@Composable
private fun MainScreenPreviewScaffold(startSrc: String = ScreenRouter.MainScreen.HomeScreen.route) {
    val navController = rememberNavController()
    ConstraintLayout(
        modifier =
            Modifier
                .fillMaxSize()
                .backgroundResource(R.drawable.img_bg_app)
    ) {
        val (layout, nav, banner) = createRefs()
        NavHost(
            modifier = Modifier.constrainAs(layout) {
                top.linkTo(parent.top)
                bottom.linkTo(nav.top, (-16).Sdp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            },
            navController = navController,
            startDestination = startSrc
        )
        {
            animComposable(ScreenRouter.MainScreen.HomeScreen.route) {
                HomeScreenContent(
                    listHomeChannel = listDemoContent,
                    modifier = Modifier.paddingTop(28.Sdp)
                )
            }
            animComposable(ScreenRouter.MainScreen.ChannelScreen.route) {
                WatchNowContent(
                    channels = ChannelEntity.fakeList(),
                    isMuted = false,
                    modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
                    pagePlayerMap = emptyMap(),
                    actions = WatchNowActions.None
                )
            }
            animComposable(ScreenRouter.MainScreen.FavoriteScreen.route) {
                FavoriteScreenContent(
                    selectedTab = 0,
                    modifier = Modifier.paddingTop(28.Sdp),
                    favoriteChannels = ChannelEntity.fakeList().filter { it.isFavorite },
                    availableGroups = listOf("News", "Entertainment", "Kids")
                )

            }
            animComposable(ScreenRouter.MainScreen.SettingScreen.route) {
                SettingScreen(
                    modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
                    gotoLangSetting = { /* no-op in preview */ },
                    onBack = { /* no-op */ }
                )
            }
        }
        BottomNavigationBar(
            navHostController = navController, modifier = Modifier
                .padding(horizontal = 16.Sdp)
                .paddingBottom(12.Sdp)
                .constrainAs(nav) {
                    bottom.linkTo(banner.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .height(54.Sdp)
                .neonState(true, shape = CircleShape, outerRadius = 6.Sdp)
                .background(IptvBgSecondary, CircleShape)
        )
        Box(Modifier.constrainAs(banner) {
            bottom.linkTo(parent.bottom)
        }) {}
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreview1() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold() }
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreview2() {
    AppViewTheme {
        Surface {
            MainScreenPreviewScaffold(
                ScreenRouter.MainScreen.ChannelScreen.route
            )
        }
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreview3() {
    AppViewTheme {
        Surface {
            MainScreenPreviewScaffold(
                ScreenRouter.MainScreen.FavoriteScreen.route
            )
        }
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreview4() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold(ScreenRouter.MainScreen.SettingScreen.route) }
    }
}
