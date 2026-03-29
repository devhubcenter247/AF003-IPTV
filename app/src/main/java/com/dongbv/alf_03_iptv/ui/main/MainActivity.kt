package com.dongbv.alf_03_iptv.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.base.InnerPadding
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style._52Sdp
import com.dongbv.alf_03_iptv.style._60Sdp
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.ui.component.ColorTheme
import com.dongbv.alf_03_iptv.ui.component.DialogQuitApp
import com.dongbv.alf_03_iptv.ui.component.ScreenRouter
import com.dongbv.alf_03_iptv.ui.main.setting.AboutUsActivity
import com.dongbv.alf_03_iptv.ui.main.setting.LanguageSettingActivity
import com.dongbv.alf_03_iptv.ui.main.setting.SelectThemeActivity
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.widget.BottomNavigationBar
import com.dongbv.alf_03_iptv.widget.animComposable
import com.dongbv.alf_03_iptv.widget.navigationTo
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    override fun initialize() = Unit

    private val viewModel: HomeViewModel by inject()
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> { }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            else -> {

                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    @Composable
    override fun BoxScope.ContentView(
          innerPadding: InnerPadding,
          context: Context
    ) {
        LaunchedEffect(Unit) {
            requestNotificationPermissionIfNeeded()
        }
        var isShowQuitDialog by rememberSaveable { mutableStateOf(false) }
        val navController = rememberNavController()
        val currentTheme by PreferenceData.themeApp.flow.collectAsStateWithLifecycle(ColorTheme.BLUE)

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                NavHost(
                    modifier = Modifier.matchParentSize(),
                    navController = navController,
                    startDestination = ScreenRouter.MainScreen.CreateScreen.route

                ) {

                }

                Box(Modifier.size(_60Sdp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(
                            when (currentTheme) {
                                ColorTheme.BLUE -> R.drawable.ic_scan_view_1
                                ColorTheme.PINK -> R.drawable.ic_scan_view_2
                                ColorTheme.GREEN -> R.drawable.ic_scan_view_3
                                else -> R.drawable.ic_scan_view_4
                            }
                        ),
                        contentDescription = stringResource(R.string.scanner),
                        modifier = Modifier
                            .size(_52Sdp)
                            .onClick {
                                navController.navigationTo(route = ScreenRouter.MainScreen.ScannerScreen.route)
                            }
                    )
                }
            }

            BottomNavigationBar(
                navHostController = navController, currentTheme, modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }
        DialogQuitApp(
            isShowDialog = isShowQuitDialog,
            currentTheme,
            onDismiss = { isShowQuitDialog = false }
        ) {
            finishAffinity()
        }
    }
}

@Composable
private fun PreviewScreenBlock(title: String, tint: Color) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color.White)
                .padding(24.dp)
        ) {
            androidx.compose.material3.Text(text = title, color = tint)
        }
    }
}

@Composable
private fun MainScreenPreviewScaffold(theme: ColorTheme) {
    val navController = rememberNavController()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavHost(
                modifier = Modifier.matchParentSize(),
                navController = navController,
                startDestination = ScreenRouter.MainScreen.ScannerScreen.route
            ) {
                composable(ScreenRouter.MainScreen.CreateScreen.route) {
                    PreviewScreenBlock("Create Screen", theme.buttonColor)
                }
                composable(ScreenRouter.MainScreen.FavoriteScreen.route) {
                    PreviewScreenBlock("Favorite Screen", theme.buttonColor)
                }
                composable(ScreenRouter.MainScreen.ScannerScreen.route) {
                    PreviewScreenBlock("Scanner Screen", theme.buttonColor)
                }
                composable(ScreenRouter.MainScreen.HistoryScreen.route) {
                    PreviewScreenBlock("History Screen", theme.buttonColor)
                }
                composable(ScreenRouter.MainScreen.SettingScreen.route) {
                    PreviewScreenBlock("Setting Screen", theme.buttonColor)
                }
            }

            Box(Modifier.size(_60Sdp), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(
                        when (theme) {
                            ColorTheme.BLUE -> R.drawable.ic_scan_view_1
                            ColorTheme.PINK -> R.drawable.ic_scan_view_2
                            ColorTheme.GREEN -> R.drawable.ic_scan_view_3
                            else -> R.drawable.ic_scan_view_4
                        }
                    ),
                    contentDescription = stringResource(R.string.scanner),
                    modifier = Modifier
                        .size(_52Sdp)
                        .onClick {
                            navController.navigate(ScreenRouter.MainScreen.ScannerScreen.route) {
                                popUpTo(ScreenRouter.MainScreen.CreateScreen.route) {
                                    inclusive = true
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                )
            }
        }
        BottomNavigationBar(
            navHostController = navController, theme, modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )
    }
}

@Preview(name = "Main (BLUE)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreviewBlue() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold(theme = ColorTheme.BLUE) }
    }
}

@Preview(name = "Main (PINK)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreviewPink() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold(theme = ColorTheme.PINK) }
    }
}

@Preview(name = "Main (GREEN)", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
private fun MainPreviewGreen() {
    AppViewTheme {
        Surface { MainScreenPreviewScaffold(theme = ColorTheme.GREEN) }
    }
}

