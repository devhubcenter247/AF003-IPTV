package com.dongbv.alf_03_iptv.ui.personalization

import com.dongbv.alf_03_iptv.analytics.AppScreen
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.AdPreloadManager
import com.dongbv.alf_03_iptv.ads.wrapper.interstitial.InterstitialAdConfig
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeView
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.CategoryModel
import com.dongbv.alf_03_iptv.data.local.entity.listCategoryModel
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ColumnParentContent
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.black
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.paddingBottom
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.paddingVertical
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.style.transparent
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.backgroundState
import com.dongbv.alf_03_iptv.ui.component.borderState
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import com.dongbv.alf_03_iptv.viewmodel.PersonalizationViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.mainColor
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.createNativeAdHolder
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PersonalizationActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.PERSONALIZATION
    private val viewModel: PersonalizationViewModel by viewModel()
    val nativeAdHolder: NativeAdHolderConfig by createNativeAdHolder(
        enabled = NativePlacement.NativeFeature.canShowAds,
        scope = lifecycleScope,
        tag = NativePlacement.NativeFeature.tag,
        options = NativeAdPreloadHolderOptions(
            fallbackAdUnitIds = NativePlacement.NativeFeature.listId,
            lifecycleOwner = this@PersonalizationActivity,
            autoReloadOnResume = false,
            autoRequestOnStart = false
        )
    )

    override fun initialize() {
        nativeAdHolder.request()
        InterstitialAdConfig.loadInterAll(this@PersonalizationActivity)
    }

    private var navigationCompleted = false

    fun goNextPage() {
        if (navigationCompleted || isFinishing || isDestroyed) return
        navigationCompleted = true
        launchActivity<MainActivity>()
        finish()
    }

    @Composable
    override fun ContentView() {
        var listFeatureContent by remember {
            mutableStateOf(listCategoryModel.map { it.copy() })
        }

        val saveState by viewModel.saveState.collectAsStateWithLifecycle()

        LaunchedEffect(saveState) {
            val state = saveState
            if (state is PersonalizationViewModel.SaveState.Success) {
                if (state.blockedCount > 0) {
                    Toast.makeText(
                        this@PersonalizationActivity,
                        getString(R.string.restricted_channels_excluded, state.blockedCount),
                        Toast.LENGTH_LONG
                    ).show()
                }
                InterstitialAdConfig.showInterAll(this@PersonalizationActivity) {
                    goNextPage()
                }
            }
        }

        ColumnParentContent(onBack = { finishAffinity() }) {
            FeatureScreen(
                listFeatureContent = listFeatureContent,
                isLoading = saveState is PersonalizationViewModel.SaveState.Loading,
                modifier = Modifier
                    .weight(1f)
                    .statusBarPadding(),
                onNextAction = {
                    viewModel.saveSelectedCategories(
                        this@PersonalizationActivity,
                        listFeatureContent.filter { it.select })
                },
                onSelectContent = { content ->
                    listFeatureContent = listFeatureContent.map {
                        if (it.index == content.index) it.copy(select = !it.select) else it
                    }
                },
            )
            NativeComposeView(
                nativePreloadConfig = (nativeAdHolder to NativePlacement.NativeFeature)
            )
        }
    }
}

@Composable
fun FeatureScreen(
    listFeatureContent: List<CategoryModel>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onNextAction: () -> Unit = {},
    onSelectContent: (CategoryModel) -> Unit = {},
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .backgroundResource(R.drawable.img_bg_app)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.personalize_your_experience),
            style = headline600(fontSize = 16.Ssp, color = IptvTextPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .paddingHorizontal(12.Sdp)
        )
        Text(
            text = stringResource(R.string.tell_us_your_preferences),
            style = headline400(
                fontSize = 12.Ssp,
                color = IptvTextPrimary
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .paddingTop(8.Sdp)
                .fillMaxWidth()
                .paddingHorizontal(12.Sdp)
        )

        FeatureItemRow(
            listFeatureContent = listFeatureContent,
            onSelectContent = onSelectContent,
            modifier = Modifier
                .paddingTop(6.Sdp)
                .fillMaxWidth()
                .paddingHorizontal(12.Sdp)
                .paddingVertical(12.Sdp)
                .weight(1f, true)
                .verticalScroll(rememberScrollState())
        )

        ContinueButton(
            isEnable = listFeatureContent.any { it.select } && !isLoading,
            isLoading = isLoading,
            onNextAction = onNextAction,
            context = context
        )
    }
}

@Composable
private fun FeatureItemRow(
    listFeatureContent: List<CategoryModel>,
    onSelectContent: (CategoryModel) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.Sdp),
        verticalArrangement = Arrangement.spacedBy(8.Sdp)
    ) {
        listFeatureContent.forEach { item ->
            key(item.index, item.select) {
                ItemFeatureContent(
                    feature = item,
                    isSelect = item.select,
                    modifier = Modifier.wrapContentWidth(),
                    onSelectContent = onSelectContent
                )
            }
        }
    }
}

@Composable
private fun ContinueButton(
    isEnable: Boolean = false,
    isLoading: Boolean = false,
    onNextAction: () -> Unit,
    context: Context
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .paddingVertical(6.Sdp)
            .wrapContentSize()
            .paddingHorizontal(12.Sdp)
            .paddingBottom(12.Sdp)
            .onClick {
                when {
                    isLoading -> Unit
                    isEnable -> onNextAction()
                    else -> Toast.makeText(
                        context,
                        context.getString(R.string.please_select_at_least_1_feature),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = IptvAccent)
        } else {
            Text(
                text = stringResource(R.string.continues),
                style = headline600(if (isEnable) IptvAccent else colorSecondary),
            )
        }
    }
}

@Composable
fun ItemFeatureContent(
    feature: CategoryModel,
    isSelect: Boolean,
    modifier: Modifier = Modifier,
    onSelectContent: (CategoryModel) -> Unit = {}
) {
    Box(
        modifier = modifier
            .borderState(enabled = isSelect)
            .backgroundState(enabled = isSelect)
            .onClick {
                onSelectContent(feature)
            }
            .paddingHorizontal(16.Sdp)
            .paddingVertical(8.Sdp),
    ) {
        TextView(
            feature.title,
            textSize = 12.Ssp,
            color = white,
            font = R.font.ss_500,
            maxLine = 1,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun GreetingPreview() {
    AppViewTheme {
        val listFeatureContent by remember { mutableStateOf(listCategoryModel.map { it.copy() }) }
        Column(Modifier.fillMaxSize()) {
            FeatureScreen(
                listFeatureContent = listFeatureContent,
                modifier = Modifier
                    .weight(1f)
                    .paddingTop(28.Sdp)
            )
            NativeComposeView()
        }

    }
}
