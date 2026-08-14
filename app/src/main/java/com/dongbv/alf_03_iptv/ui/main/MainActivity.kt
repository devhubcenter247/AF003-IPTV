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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.inappupdate.AppUpdateWrapper
import com.app.inappupdate.InAppUpdateManager
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.remoteconfig.remoteConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.model.listDemoContent
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ConstraintParentContent
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.paddingBottom
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.ui.component.DialogQuitApp
import com.dongbv.alf_03_iptv.ui.component.borderState
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.ui.main.home.ChannelScreen
import com.dongbv.alf_03_iptv.ui.main.home.FavoriteScreen
import com.dongbv.alf_03_iptv.ui.main.home.HomeScreen
import com.dongbv.alf_03_iptv.ui.main.home.SettingScreen
import com.dongbv.alf_03_iptv.ui.main.add.AddChannelActivity
import com.dongbv.alf_03_iptv.ui.main.guide.HowToAddChannelActivity
import com.dongbv.alf_03_iptv.ui.main.home.FavoriteScreenContent
import com.dongbv.alf_03_iptv.ui.main.home.HomeScreenContent
import com.dongbv.alf_03_iptv.ui.main.play.PlayChannelActivity
import com.dongbv.alf_03_iptv.ui.main.playlist.PlayListActivity
import com.dongbv.alf_03_iptv.ui.main.setting.AboutUsActivity
import com.dongbv.alf_03_iptv.ui.main.setting.LanguageSettingActivity
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowActions
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowActivity
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowContent
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.widget.BottomNavigationBar
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.animComposable
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.sdp.ssp.android.Sdp
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : BaseActivity() {
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
    override fun ContentView(statusBarHeight: Dp) {
        LaunchedEffect(Unit) {
            requestNotificationPermissionIfNeeded()
        }
        var isShowQuitDialog by rememberSaveable { mutableStateOf(false) }
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
                    bottom.linkTo(nav.top,(-16).Sdp)
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
                        modifier = Modifier.paddingTop(statusBarHeight),
                        viewModel = viewModel,
                        onWatchNow = { launchActivity<WatchNowActivity>() },
                        onOpenChannel = { item ->
                            startActivity(
                                PlayListActivity.newIntent(this@MainActivity, item.id.toLong())
                            )
                        },
                        onAddPlaylist = { launchActivity<AddChannelActivity>() },
                        onHowToAddChannel = { launchActivity<HowToAddChannelActivity>() }
                    ) {
                        isShowQuitDialog = true
                    }
                }
                animComposable(ScreenRouter.MainScreen.ChannelScreen.route) {
                    ChannelScreen(
                        statusBarHeight = statusBarHeight
                    ) {
                        isShowQuitDialog = true
                    }
                }
                animComposable(ScreenRouter.MainScreen.FavoriteScreen.route) {
                    FavoriteScreen(
                        modifier = Modifier.paddingTop(statusBarHeight),
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
                            startActivity(newIntent)
                        })

                }
                animComposable(ScreenRouter.MainScreen.SettingScreen.route) {
                    SettingScreen(
                        modifier = Modifier.paddingTop(statusBarHeight),
                        gotoLangSetting = { launchActivity<LanguageSettingActivity>() },
                        gotoAboutUs = { launchActivity<AboutUsActivity>() },
                        gotoHowToAddChannel = { launchActivity<HowToAddChannelActivity>() }
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
                    .background(IptvBgSecondary,CircleShape)
            )
            Box(Modifier.constrainAs(banner) {
                bottom.linkTo(parent.bottom)
            }) {}
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
                bottom.linkTo(nav.top,(-16).Sdp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            },
            navController = navController,
            startDestination = startSrc
        )
        {
            animComposable(ScreenRouter.MainScreen.HomeScreen.route) {
                HomeScreenContent(listHomeChannel = listDemoContent, modifier = Modifier.paddingTop(28.Sdp))
            }
            animComposable(ScreenRouter.MainScreen.ChannelScreen.route) {
                WatchNowContent(
                    channels = ChannelEntity.fakeList(),
                    isMuted = false,
                    modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
                    pagePlayerMap = emptyMap(),
                    statusBarHeight = 24.dp,
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
                .background(IptvBgSecondary,CircleShape)
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
        Surface { MainScreenPreviewScaffold(
            ScreenRouter.MainScreen.ChannelScreen.route
        ) }
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreview3() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold(
            ScreenRouter.MainScreen.FavoriteScreen.route
        ) }
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreview4() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold(ScreenRouter.MainScreen.SettingScreen.route) }
    }
}