package com.dongbv.alf_03_iptv.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.dongbv.alf_03_iptv.style._1Sdp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.style._24Sdp
import com.dongbv.alf_03_iptv.style._28Sdp
import com.dongbv.alf_03_iptv.style._2Sdp
import com.dongbv.alf_03_iptv.style._3Sdp
import com.dongbv.alf_03_iptv.style.gradientContent
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.lightGrey
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.transparent
import com.dongbv.alf_03_iptv.ui.component.ScreenRouter

enum class BottomNavItems(
    val index: Int = 0,
    val route: String = "",
    val title: Int = -1,
    val icon: Int,
) {
    HOME(
        index = 0,
        route = ScreenRouter.MainScreen.HomeScreen.route,
        title = R.string.home,
        icon = R.drawable.ic_nav_home,
    ),
    CHANNEL( index = 1,
        route = ScreenRouter.MainScreen.ChannelScreen.route,
        title = R.string.channel,
        icon = R.drawable.ic_nav_channel),
    FAVORITE(
        2,
        route = ScreenRouter.MainScreen.FavoriteScreen.route,
        title = R.string.favorite, R.drawable.ic_nav_favorite,
    ),
    SETTING(
        3,
        route = ScreenRouter.MainScreen.SettingScreen.route,
        title = R.string.setting,
        R.drawable.ic_nav_setting
    ),
}

@Composable
fun RowScope.NavItem(
    item: BottomNavItems,
    selected: Boolean,
    icon: Int,
    navController: NavHostController
) {

    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(icon),
                modifier = Modifier.size(_24Sdp).padding(_2Sdp).gradientContent( if(selected)listOf(grdStart,grdEnd) else listOf(gray,gray)),
                contentDescription = null
            )
            Text(
                text = LocalContext.current.getString(item.title),
                maxLines = 1,
                softWrap = true,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.ss_500)),
                color = if(selected) mainColor else gray,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
        }
        Box(
            Modifier
                .matchParentSize()
                .background(transparent)
                .onClick {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(ScreenRouter.MainScreen.HomeScreen.route) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                }
        )
    }
}

@Composable
fun BottomNavigationBar(
    navHostController: NavHostController,
    modifier: Modifier
) {
    val navBackStackEntry by navHostController.currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = navHostController.currentBackStackEntry)
    val currentDestination = navBackStackEntry?.destination

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(
            item = BottomNavItems.HOME,
            selected = currentDestination.isSelected(BottomNavItems.HOME.route),
            navController = navHostController,
            icon = BottomNavItems.HOME.icon
        )
        NavItem(
            item = BottomNavItems.CHANNEL,
            selected = currentDestination.isSelected(BottomNavItems.CHANNEL.route),
            navController = navHostController,
            icon = BottomNavItems.CHANNEL.icon
        )

        NavItem(
            item = BottomNavItems.FAVORITE,
            selected = currentDestination.isSelected(BottomNavItems.FAVORITE.route),
            navController = navHostController,
            icon = BottomNavItems.FAVORITE.icon
        )
        NavItem(
            item = BottomNavItems.SETTING,
            selected = currentDestination.isSelected(BottomNavItems.SETTING.route),
            navController = navHostController,
            icon = BottomNavItems.SETTING.icon
        )
    }
}

private fun NavDestination?.isSelected(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true
