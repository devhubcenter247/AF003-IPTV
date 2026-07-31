package com.dongbv.alf_03_iptv.ui.main.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.collectAsStateWithLifecycle
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.FullPreview
import com.dongbv.alf_03_iptv.style.*
import com.dongbv.alf_03_iptv.ui.component.RatingDialog
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.ui.language.getLanguageArray
import com.sdp.ssp.android.Ssp

@FullPreview
@Composable
private fun SettingScreenPreview() {

    AppViewTheme {
        Surface(color = Color.White) {
            SettingScreen(
                gotoLangSetting = { /* no-op in preview */ },
                onBack = { /* no-op */ }
            )
        }
    }
}

@Preview(name = "ScanSetting")
@Composable
private fun ScanSettingPreview() {
    AppViewTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
        }
    }
}

@Preview(name = "SettingItem")
@Composable
private fun SettingItemPreview() {
    AppViewTheme {
        SettingItem(
            icon = R.drawable.ic_setting_info,
            label = R.string.about_us,
            action = { }
        )
    }
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    gotoLangSetting: () -> Unit = {},
    gotoSettingTheme: () -> Unit = {},
    gotoAboutUs: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    val currentLanguage by remember {
        mutableStateOf(getLanguageArray().find { it ->
            it.code == (if(isPreview) "en" else AppPreferences.languageCode)
        } ?: getLanguageArray()[0])
    }
    val isRateApp by AppPreferences.isRatingApp.collectAsStateWithLifecycle()
    var isShowRating by remember {
        mutableStateOf(false)
    }
    BoxParentContent(
        onBack = {
            onBack.invoke()
        }, modifier = modifier.fillMaxSize()
    ) {
        VerScrollView(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            TextView(
                R.string.setting,
                font = R.font.ss_600,
                color = black,
                textSize = 18.Ssp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            SpaceH(16)


            Column(
                Modifier
                    .fillMaxWidth()
                    .background(white, shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                LangSettingItem(currentLanguage) {
                    gotoLangSetting.invoke()
                }
                if (!isRateApp) {
                    SettingItem(R.drawable.ic_setting_rate, R.string.rate_us) {
                        isShowRating = true
                    }
                }
                SettingItem(R.drawable.ic_setting_info, R.string.about_us) {
                    gotoAboutUs.invoke()
                }
            }
        }
    }
    RatingDialog(isShowRating, { isShowRating = false }, {
        isShowRating = false
        AppPreferences.isRatingApp.value = true
    }) {
    }
}

@Composable
fun LangSettingItem(
    currentLanguage: LanguageItem,
    action: () -> Unit
) {
    Box(Modifier.padding(vertical = 12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = R.drawable.ic_setting_lang,
                modifier = Modifier
            )
            SpaceW(12)
            TextView(
                text = R.string.language,
                font = R.font.ss_400,
                textSize = 12.Ssp,
                color = black
            )
            Spacer(Modifier.weight(1f))
            TextView(
                text = currentLanguage.name,
                font = R.font.ss_500,
                textSize = 12.Ssp,
                color = burgundy
            )
            SpaceW(8)
            IconView(
                item = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                tint = textColor,
                modifier = Modifier
            )
        }
        Box(
            Modifier
                .matchParentSize()
                .onClick { action.invoke() })
    }
}

@Composable
fun SettingItem(@DrawableRes icon: Int, @StringRes label: Int, action: () -> Unit) {
    Box(Modifier.padding(vertical = 12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = icon,
                modifier = Modifier
            )
            SpaceW(12)
            TextView(
                text = stringResource(label),
                txtStyle = TextStyle.Default.fontFamily(R.font.ss_500).fontPadding(false).copy(
                    color = black, fontSize = 11.Ssp
                )
            )
        }
        Box(
            Modifier
                .matchParentSize()
                .onClick {
                    action.invoke()
                })
    }
}