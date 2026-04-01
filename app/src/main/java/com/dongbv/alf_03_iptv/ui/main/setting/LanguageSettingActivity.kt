package com.dongbv.alf_03_iptv.ui.main.setting

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.base.InnerPadding
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.ViewParentContent
import com.dongbv.alf_03_iptv.style._10Sdp
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._18Ssp
import com.dongbv.alf_03_iptv.style._4Sdp
import com.dongbv.alf_03_iptv.style._6Sdp
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.LanguageItem
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.ui.language.getLanguageArray
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.mainColor

class LanguageSettingActivity : BaseActivity() {
    override fun initialize() = Unit

    @Composable
    override fun BoxScope.ContentView(innerPadding: InnerPadding, context: Context) {
        val languages = remember { getLanguageArray().toList() }
        var selected by rememberSaveable {
            mutableStateOf(getLanguageArray().find { it ->
                it.code == (PreferenceData.languageCode)
            } ?: getLanguageArray()[0])
        }

        ViewParentContent(onBack = { finish() }) {
            LanguageSettingScreen(
                languages = languages,
                selected = selected,
                modifier = Modifier
                    .background(IptvBgPrimary)
                    .fillMaxSize(1f)
                    .padding(top = innerPadding.top + _6Sdp),
                onSelect = { selected = it },
                onConfirm = {
                    val code = selected?.code ?: "en"
                    PreferenceData.setLocale(code)
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
                    .paddingTop(_6Sdp)
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
            Modifier.padding(horizontal = _12Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = R.drawable.ic_app_back,
                icSize = 28, tint = mainColor,
            ) {
                onBack()
            }
            SpaceW(_4Sdp)
            TextView(
                text = R.string.language,
                font = R.font.rb_700,
                textSize = _18Ssp,
                color = mainColor
            )
            Spacer(Modifier.weight(1f))
            IconView(
                item = R.drawable.ic_app_check,
                icSize = 28,
                tint = mainColor,
            ) {
                if (selected != null) onConfirm()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = _12Sdp)
                .paddingTop(_12Sdp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            this.items(
                items = languages,
                key = { it.code }
            ) { item ->
                LanguageItem(currentLanguage = selected,item = item) { chosen ->
                    onSelect(chosen)
                }
            }
            item {
                SpaceH(_10Sdp)
            }
        }
    }
}
