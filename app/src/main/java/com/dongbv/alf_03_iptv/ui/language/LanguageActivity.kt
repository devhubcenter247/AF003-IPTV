package com.dongbv.alf_03_iptv.ui.language

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeView
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ColumnParentContent
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.ui.component.LanguageViewItem
import com.dongbv.alf_03_iptv.ui.onboarding.IntroActivity
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.createNativeAdHolder
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

abstract class LanguageActivity : BaseActivity() {
    companion object {
        private const val SCREEN_OFFSET = "screen_offset"
        var savedIndex: Int = 0
        var savedOffset: Int = 0
        var currentLanguage by mutableStateOf<LanguageItem?>(null)
        val listLanguages = getLanguageArray().toList()

        fun startLanguage(context: Context, screenType: LanguageScreenType, screenOffset: Int = 0) {
            val clazz = if (screenType == LanguageScreenType.Language1) {
                Language1Activity::class.java
            } else Language2Activity::class.java
            val intent = Intent(context, clazz)
            intent.putExtra(SCREEN_OFFSET, screenOffset)
            val options = ActivityOptions.makeCustomAnimation(
                context,
                0,
                0
            )
            context.startActivity(intent, options.toBundle())
        }
    }

    abstract val screenType: LanguageScreenType
    open fun nativePlacement(): NativePlacement = NativePlacement.Language1
    val nativeAdHolder: NativeAdHolderConfig by createNativeAdHolder(
        enabled = nativePlacement().canShowAds,
        scope = lifecycleScope,
        tag = nativePlacement().tag,
        options = NativeAdPreloadHolderOptions(
            fallbackAdUnitIds = nativePlacement().listId,
            lifecycleOwner = this@LanguageActivity,
            autoReloadOnResume = false,
            autoRequestOnStart = false
        )
    )

    override fun isFinishFirstFlow() = false

    @Composable
    override fun ContentView() {
        val lazyListState = rememberLazyListState(
            initialFirstVisibleItemIndex = savedIndex,
            initialFirstVisibleItemScrollOffset = savedOffset
        )
        ColumnParentContent(onBack = { finish() }) {
            LanguageScreen(
                languages = listLanguages,
                selected = currentLanguage,
                lazyListState = lazyListState,
                nativeAd = {
                    NativeComposeView(
                        nativePreloadConfig = (nativeAdHolder to nativePlacement())
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onSelect = {
                    currentLanguage = it
                    if (screenType == LanguageScreenType.Language1) {
                        savedIndex = lazyListState.firstVisibleItemIndex
                        savedOffset = lazyListState.firstVisibleItemScrollOffset
                        startLanguage(this@LanguageActivity, LanguageScreenType.Language2)
                        finish()
                    }
                },
                onConfirm = {
                    val code = currentLanguage?.code ?: "en"
                    AppPreferences.setLocale(code)
                    launchActivity<IntroActivity>()
                    finish()
                },
            )
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
                    .paddingTop(28.Sdp),
            onSelect = {},
            onConfirm = {},
            nativeAd = {
                NativeComposeView(
                    nativePreloadConfig = null
                )
            }
        )
    }
}

@Composable
fun LanguageScreen(
    languages: List<LanguageItem>,
    selected: LanguageItem?,
    nativeAd: @Composable () -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    onSelect: (LanguageItem) -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        Modifier
            .backgroundResource(R.drawable.img_bg_app)
            .statusBarPadding()
            .then(modifier)
    ) {
        Row(
            Modifier.padding(horizontal = 12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                text = R.string.language,
                font = R.font.ss_600,
                textSize = 18.Ssp,
                color = Color.White,
            )
            Spacer(Modifier.weight(1f))
            IconView(
                item = if (selected != null) Icons.Rounded.Done else Icons.Default.Done,
                icSize = 26.Sdp,
                tint = if (selected != null) colorSecondary else Color.Transparent,
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
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            this.items(
                items = languages,
                key = { it.code }
            ) { item ->
                LanguageViewItem(currentLanguage = selected, item = item) { chosen ->
                    onSelect(chosen)
                }
            }
        }
        nativeAd()
    }
}
