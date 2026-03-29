package com.dongbv.alf_03_iptv.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dongbv.alf_03_iptv.style._1Sdp
import com.dongbv.alf_03_iptv.style._5Sdp
import com.dongbv.alf_03_iptv.style._15Sdp
import com.dongbv.alf_03_iptv.style._18Sdp
import com.dongbv.alf_03_iptv.style._20Sdp
import com.dongbv.alf_03_iptv.style._28Sdp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.style._2Sdp
import com.dongbv.alf_03_iptv.style._3Sdp
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.lightGrey
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.transparent
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.ColorTheme
import com.dongbv.alf_03_iptv.ui.component.ScreenRouter

enum class BottomNavItems(
    val index: Int = 0,
    val route: String = "",
    val title: Int = -1,
    val icon: Int,
    var defaultIcon: Int
) {
    HOME(
        0,
        route = ScreenRouter.MainScreen.CreateScreen.route,
        title = R.string.create,
        R.drawable.hom_enb_1, R.drawable.hom_dis_1
    ),
    FAVORITE(
        1,
        route = ScreenRouter.MainScreen.FavoriteScreen.route,
        title = R.string.favorite, R.drawable.hom_enb_2, R.drawable.hom_dis_2
    ),
    NULLS(2, route = "", title = -1, icon = 0, 0),
    HISTORY(
        3,
        route = ScreenRouter.MainScreen.HistoryScreen.route,
        title = R.string.history,
        R.drawable.hom_enb_3, R.drawable.hom_dis_3
    ),
    SETTING(
        4,
        route = ScreenRouter.MainScreen.SettingScreen.route,
        title = R.string.setting, R.drawable.hom_enb_4,
        R.drawable.hom_dis_4
    ),
}

class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2
        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 2f
            val cutoutLeftX = cutoutCenterX - cutoutEdgeOffset
            val cutoutRightX = cutoutCenterX + cutoutEdgeOffset
            moveTo(x = 0F, y = size.height)
            if (cutoutLeftX > 0) {
                val realLeftCornerDiameter = if (cutoutLeftX >= cornerRadiusPx) {
                    cornerDiameter
                } else {
                    // rounded corner and cutout overlap
                    cutoutLeftX * 2
                }
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = realLeftCornerDiameter,
                        bottom = realLeftCornerDiameter
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            lineTo(cutoutLeftX, 0f)
            // cutout
            cubicTo(
                x1 = cutoutCenterX - cutoutRadius,
                y1 = 0f,
                x2 = cutoutCenterX - cutoutRadius,
                y2 = cutoutRadius,
                x3 = cutoutCenterX,
                y3 = cutoutRadius,
            )
            cubicTo(
                x1 = cutoutCenterX + cutoutRadius,
                y1 = cutoutRadius,
                x2 = cutoutCenterX + cutoutRadius,
                y2 = 0f,
                x3 = cutoutRightX,
                y3 = 0f,
            )
            if (cutoutRightX < size.width) {
                val realRightCornerDiameter = if (cutoutRightX <= size.width - cornerRadiusPx) {
                    cornerDiameter
                } else {
                    (size.width - cutoutRightX) * 2
                }
                arcTo(
                    rect = Rect(
                        left = size.width - realRightCornerDiameter,
                        top = 0f,
                        right = size.width,
                        bottom = realRightCornerDiameter
                    ),
                    startAngleDegrees = -90.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            lineTo(x = size.width, y = size.height)
            close()
        }
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomNavItems,
    selected: Boolean,
    themeApp: ColorTheme,
    navController: NavHostController
) {
    val iconRes = remember(item.index, themeApp, selected) {
        if (selected) getIndexByTheme(item.index, themeApp) else item.defaultIcon
    }

    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(iconRes),
                modifier = Modifier.size(_28Sdp).padding(_3Sdp),
                contentDescription = null
            )
            Text(
                text = LocalContext.current.getString(item.title),
                maxLines = 1,
                softWrap = true,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.rb_500)),
                color = if (selected) themeApp.buttonColor else gray,
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
                            popUpTo(ScreenRouter.MainScreen.CreateScreen.route) {
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


fun getIndexByTheme(index: Int, themeApp: ColorTheme = ColorTheme.BLUE): Int {
    return when (index) {
        1 -> {
            when (themeApp) {
                ColorTheme.BLUE -> R.drawable.hom_enb_2
                ColorTheme.PINK -> R.drawable.hom_enb_2_2
                ColorTheme.GREEN -> R.drawable.hom_enb_2_3
                else -> R.drawable.hom_enb_2_4
            }
        }
        3 -> {
            when (themeApp) {
                ColorTheme.BLUE -> R.drawable.hom_enb_3
                ColorTheme.PINK -> R.drawable.hom_enb_3_2
                ColorTheme.GREEN -> R.drawable.hom_enb_3_3
                else -> R.drawable.hom_enb_3_4
            }
        }
        4 -> {
            when (themeApp) {
                ColorTheme.BLUE -> R.drawable.hom_enb_4
                ColorTheme.PINK -> R.drawable.hom_enb_4_2
                ColorTheme.GREEN -> R.drawable.hom_enb_4_3
                else -> R.drawable.hom_enb_4_4
            }
        }
        else -> {
            when (themeApp) {
                ColorTheme.BLUE -> R.drawable.hom_enb_1
                ColorTheme.PINK -> R.drawable.hom_enb_1_2
                ColorTheme.GREEN -> R.drawable.hom_enb_1_3
                else -> R.drawable.hom_enb_1_4
            }
        }
    }
}



@Composable
fun BottomNavigationBar(
    navHostController: NavHostController,
    themeApp: ColorTheme,
    modifier: Modifier
) {
    val navBackStackEntry by navHostController.currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = navHostController.currentBackStackEntry)
    val currentDestination = navBackStackEntry?.destination
    var barWidthPx by remember { mutableIntStateOf(0) }
    val barShape = remember(barWidthPx, themeApp) {
        BarShape(
            offset = barWidthPx / 2f,
            circleRadius = 15.dp,
            cornerRadius = 18.dp,
            circleGap = 20.dp,
        )
    }

    Row(
        modifier = modifier
            .onSizeChanged { barWidthPx = it.width }
            .border(width = _1Sdp, shape = barShape, color = lightGrey.copy(0.8f))
            .background(white, barShape),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AddItem(
            item = BottomNavItems.HOME,
            selected = currentDestination.isSelected(BottomNavItems.HOME.route),
            themeApp = themeApp,
            navController = navHostController
        )
        AddItem(
            item = BottomNavItems.FAVORITE,
            selected = currentDestination.isSelected(BottomNavItems.FAVORITE.route),
            themeApp = themeApp,
            navController = navHostController
        )

        Spacer(Modifier.weight(1f).fillMaxHeight())

        AddItem(
            item = BottomNavItems.HISTORY,
            selected = currentDestination.isSelected(BottomNavItems.HISTORY.route),
            themeApp = themeApp,
            navController = navHostController
        )
        AddItem(
            item = BottomNavItems.SETTING,
            selected = currentDestination.isSelected(BottomNavItems.SETTING.route),
            themeApp = themeApp,
            navController = navHostController
        )
    }
}

private fun NavDestination?.isSelected(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true
