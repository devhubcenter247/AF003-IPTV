package com.dongbv.alf_03_iptv.ui.language

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.ViewParentContent
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._18Ssp
import com.dongbv.alf_03_iptv.style._6Sdp
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.ColorTheme
import com.dongbv.alf_03_iptv.ui.component.LanguageItem
import com.dongbv.alf_03_iptv.ui.intro.IntroActivity
import com.dongbv.alf_03_iptv.widget.mainColor

class LanguageActivity : BaseActivity() {
    override fun initialize() = Unit
    override fun isFinishFirstFlow() = false
    @Composable
    override fun BoxScope.ContentView(innerPadding: InnerPadding, context: Context) {
        val languages = remember { getLanguageArray().toList() } // List bất biến
        var selected by rememberSaveable { mutableStateOf<LanguageItem?>(null) }
        val theme by PreferenceData.themeApp.flow.collectAsStateWithLifecycle(ColorTheme.BLUE)
        ViewParentContent(onBack = { finish() }) {
            Column(Modifier.fillMaxSize()) {
                LanguageScreen(
                    languages = languages,
                    selected = selected,
                    theme = theme,
                    modifier = Modifier
                        .background(theme.bgColor)
                        .fillMaxWidth()
                        .padding(top = innerPadding.top + _6Sdp)
                        .weight(1f),
                    onSelect = { selected = it },
                    onConfirm = {
                        val code = selected?.code ?: "en"
                        PreferenceData.setLocale(code)
                        launchAndClearTask<IntroActivity>()
                    }
                )
            }
        }
    }
}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun GreetingPreview() {
    val listLanguages = getLanguageArray()
    AppViewTheme {
        LanguageScreen(
            languages = listLanguages,
            selected = listLanguages[0],
            theme = ColorTheme.BLUE,
            modifier =
                Modifier
                    .paddingTop(_6Sdp)
                    .background(white),
            {}
        ) {}
    }
}

@Composable
fun LanguageScreen(
    languages: List<LanguageItem>,
    selected: LanguageItem?,
    theme: ColorTheme = ColorTheme.BLUE,
    modifier: Modifier = Modifier,
    onSelect: (LanguageItem) -> Unit,
    onConfirm: () -> Unit
) {
    val listState = rememberLazyListState()
    Column(modifier.background(theme.bgColor)) {
        Row(
            Modifier.padding(horizontal = _12Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                text = R.string.language,
                font = R.font.rb_700,
                textSize = _18Ssp,
                color = mainColor
            )
            Spacer(Modifier.weight(1f))
            IconView(
                item = if (selected != null) Icons.Rounded.Done else Icons.Default.Done,
                icSize = 32,
                tint = if (selected != null) mainColor else Color.Transparent,
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
                LanguageItem(currentLanguage = selected, theme = theme, item = item) { chosen ->
                    onSelect(chosen)
                }
            }
        }
    }
}
