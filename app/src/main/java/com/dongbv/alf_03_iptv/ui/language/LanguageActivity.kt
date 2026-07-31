package com.dongbv.alf_03_iptv.ui.language

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.LanguageItem
import com.dongbv.alf_03_iptv.ui.intro.IntroActivity
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.mainColor
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

class LanguageActivity : BaseActivity() {
    override fun initialize() = Unit
    override fun isFinishFirstFlow() = false
    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        val languages = remember { getLanguageArray().toList() }
        var selected by rememberSaveable { mutableStateOf<LanguageItem?>(null) }
        BoxParentContent(onBack = { finish() }) {
            Column(Modifier.fillMaxSize()) {
                LanguageScreen(
                    languages = languages,
                    selected = selected,
                    modifier = Modifier
                        .background(IptvBgPrimary)
                        .fillMaxWidth()
                        .padding(top = statusBarHeight)
                        .weight(1f),
                    onSelect = { selected = it },
                    onConfirm = {
                        val code = selected?.code ?: "en"
                        AppPreferences.setLocale(code)
                        launchAndClearTask<IntroActivity>()
                    }
                )
            }
        }
    }
}

@ResponsivePreview
@Composable
fun GreetingPreview() {
    val listLanguages = getLanguageArray()
    AppViewTheme {
        LanguageScreen(
            languages = listLanguages,
            selected = listLanguages[0],
            modifier =
                Modifier
                    .paddingTop(6.Sdp)
                    .background(white),
            {}
        ) {}
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LanguageScreen(
    languages: List<LanguageItem>,
    selected: LanguageItem?,
    modifier: Modifier = Modifier,
    onSelect: (LanguageItem) -> Unit,
    onConfirm: () -> Unit
) {
    val listState = rememberLazyListState()
    val isTablet = LocalConfiguration.current.screenWidthDp >= 600
    Column(modifier.background(IptvBgPrimary)) {
        Row(
            Modifier.padding(horizontal = 12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                text = R.string.language,
                font = R.font.ss_700,
                textSize = 18.Ssp,
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

        if (isTablet) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.Sdp)
                    .paddingTop(12.Sdp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = languages,
                    key = { it.code }
                ) { item ->
                    LanguageItem(currentLanguage = selected, item = item) { chosen ->
                        onSelect(chosen)
                    }
                }
            }
        } else {
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
                    LanguageItem(currentLanguage = selected, item = item) { chosen ->
                        onSelect(chosen)
                    }
                }
            }
        }
    }
}
