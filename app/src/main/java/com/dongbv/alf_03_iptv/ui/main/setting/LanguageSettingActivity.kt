package com.dongbv.alf_03_iptv.ui.main.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.LanguageViewItem
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.ui.language.getLanguageArray
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.mainColor
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

class LanguageSettingActivity : BaseActivity() {
    override fun initialize() = Unit

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        val languages = remember { getLanguageArray().toList() }
        var selected by rememberSaveable {
            mutableStateOf(getLanguageArray().find { it ->
                it.code == (AppPreferences.languageCode)
            } ?: getLanguageArray()[0])
        }

        BoxParentContent(onBack = { finish() }) {
            LanguageSettingScreen(
                languages = languages,
                selected = selected,
                modifier = Modifier
                
                    .fillMaxSize(1f)
                    .padding(top = statusBarHeight),
                onSelect = { selected = it },
                onConfirm = {
                    val code = selected.code
                    AppPreferences.setLocale(code)
                    launchAndClearTask<MainActivity>()
                }, onBack = {
                    finish()
                }
            )
        }
    }
}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun GreetingPreview() {
    val listLanguages = getLanguageArray()
    AppViewTheme {
        LanguageSettingScreen(
            listLanguages, listLanguages[0], modifier =
                Modifier
                    .paddingTop(6.Sdp)
                    .background(white), onSelect = {}, onConfirm = {}, onBack = {}
        )
    }
}

@Composable
fun LanguageSettingScreen(
      languages: List<LanguageItem>,
      selected: LanguageItem?,
      @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
      onSelect: (LanguageItem) -> Unit,
      onConfirm: () -> Unit,
      onBack: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        listState.animateScrollToItem(languages.indexOf(selected))
    }
    Column(modifier) {
        Row(
            Modifier.padding(horizontal = 12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = R.drawable.ic_app_back,
                icSize = 28.Sdp, tint = mainColor,
            ) {
                onBack()
            }
            SpaceW(4.Sdp)
            TextView(
                text = R.string.language,
                font = R.font.ss_700,
                textSize = 18.Ssp,
                color = mainColor
            )
            Spacer(Modifier.weight(1f))
            IconView(
                item = R.drawable.ic_app_check,
                icSize = 28.Sdp,
                tint = mainColor,
            ) {
                if (selected != null) onConfirm()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.Sdp)
                .paddingTop(12.Sdp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            this.items(
                items = languages,
                key = { it.code }
            ) { item ->
                LanguageViewItem(currentLanguage = selected,item = item) { chosen ->
                    onSelect(chosen)
                }
            }
            item {
                SpaceH(10.dp)
            }
        }
    }
}
