package com.dongbv.alf_03_iptv.ui.main.home

import android.R.attr.maxWidth
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.dongbv.alf_03_iptv.ui.component.borderState
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.ui.language.getLanguageArray
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline600
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

@FullPreview
@Composable
private fun SettingScreenPreview() {

    AppViewTheme {
        Surface(color = Color.White) {
            SettingScreen(
                modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
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
                .backgroundResource(R.drawable.img_bg_app)
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
    gotoAboutUs: () -> Unit = {},
    gotoHowToAddChannel: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    val currentLanguage by remember {
        mutableStateOf(
            if (isPreview) {
                getLanguageArray().find { it.code == "en" } ?: getLanguageArray()[0]
            } else {
                getLanguageArray().find { it.code == AppPreferences.languageCode }
                    ?: getLanguageArray()[0]
            }
        )
    }
    val isRateApp by if (isPreview) {
        remember { mutableStateOf(false) }
    } else {
        AppPreferences.isRatingApp.collectAsStateWithLifecycle()
    }
    var isShowRating by remember {
        mutableStateOf(false)
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp
        ColumnParentContent(
            onBack = {
                onBack.invoke()
            }, modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.setting),
                style = headline600(
                    fontSize = if (isTablet) 20.Ssp else 18.Ssp,
                    color = IptvTextPrimary
                ),
                modifier = Modifier.weight(1f)
            )
            VerScrollView(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                TextView(
                    R.string.setting,
                    font = R.font.ss_600,
                    color = white,
                    textSize = 18.Ssp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                SpaceH(16)


                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())

                ) {
                    LangSettingItem(currentLanguage) {
                        gotoLangSetting.invoke()
                    }
                    if (!isRateApp) {
                        SettingItem(R.drawable.ic_setting_rate, R.string.rate_us) {
                            isShowRating = true
                        }
                        SpaceH(12)
                    }

                    SettingItem(R.drawable.ic_app_how_use, R.string.how_to_add_new_channel) {
                        gotoHowToAddChannel.invoke()
                    }
                    SpaceH(12)
                    SettingItem(R.drawable.ic_setting_info, R.string.about_us) {
                        gotoAboutUs.invoke()
                    }
                }
            }
        }
    }
    RatingDialog(isShowRating, { isShowRating = false }, {
        isShowRating = false
        if (!isPreview) {
            AppPreferences.isRatingApp.value = true
        }
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
            Modifier
                .fillMaxWidth()
                .borderState(enabled = true, shape = RoundedCornerShape(12.Sdp))
                .background(IptvBgPrimary, RoundedCornerShape(12.Sdp))
                .padding(12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = R.drawable.ic_setting_lang,
                modifier = Modifier.size(26.Sdp).gradientContent(
                    listOf(grdStart, grdEnd)
                )
            )
            SpaceW(12)
            TextView(
                text = R.string.language,
                font = R.font.ss_400,
                textSize = 12.Ssp,
                color = white
            )
            Spacer(Modifier.weight(1f))
            TextView(
                text = currentLanguage.name,
                font = R.font.ss_500,
                textSize = 12.Ssp,
                color = IptvAccent
            )
            SpaceW(8)
            IconView(
                item = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                tint = IptvAccent,
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
    Row(
        Modifier
            .fillMaxWidth()
            .borderState(enabled = true, shape = RoundedCornerShape(12.Sdp))
            .background(IptvBgPrimary, RoundedCornerShape(12.Sdp))
            .padding(12.Sdp)
            .onClick {
                action.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconView(
            item = icon,
            modifier = Modifier.size(26.Sdp).gradientContent(
                listOf(grdStart, grdEnd)
            )
        )
        SpaceW(12)
        TextView(
            color = white,
            text = stringResource(label),
            txtStyle = TextStyle.Default.fontFamily(R.font.ss_500).fontPadding(false).copy(
                color = white, fontSize = 11.Ssp
            )
        )
    }
}