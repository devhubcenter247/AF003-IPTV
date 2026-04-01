package com.dongbv.alf_03_iptv.ui.component

object
ScreenRouter {
    sealed class MainScreen(var route: String) {
        data object HomeScreen : MainScreen("homeScreen")
        data object ChannelScreen : MainScreen("channelScreen")
        data object FavoriteScreen : MainScreen("favoriteScreen")
        data object SettingScreen : MainScreen("settingScreen")
    }
}