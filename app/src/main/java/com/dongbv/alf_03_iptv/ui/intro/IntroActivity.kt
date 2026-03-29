package com.dongbv.alf_03_iptv.ui.intro

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.base.InnerPadding
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.SpaceF
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.ViewParentContent
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._12Ssp
import com.dongbv.alf_03_iptv.style._13Ssp
import com.dongbv.alf_03_iptv.style._16Sdp
import com.dongbv.alf_03_iptv.style._24Sdp
import com.dongbv.alf_03_iptv.style._48Sdp
import com.dongbv.alf_03_iptv.style._6Sdp
import com.dongbv.alf_03_iptv.style._8Sdp
import com.dongbv.alf_03_iptv.style.IptvAccent
import com.dongbv.alf_03_iptv.style.IptvBgPrimary
import com.dongbv.alf_03_iptv.style.IptvBgSecondary
import com.dongbv.alf_03_iptv.style.IptvTextPrimary
import com.dongbv.alf_03_iptv.style.IptvTextSecondary
import com.dongbv.alf_03_iptv.style.heightPercentDp
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import kotlinx.coroutines.launch

class IntroActivity : BaseActivity() {
    override fun initialize() = Unit

    @Composable
    override fun BoxScope.ContentView(innerPadding: InnerPadding, context: Context) {
        ViewParentContent(onBack = { finishAffinity() }) {
            IntroScreen(
                modifier = Modifier.background(IptvBgPrimary),
                nextScreen = {
                    PreferenceData.isFinishFirstFlow = true
                    launchActivity<MainActivity>()
                    finish()
                }
            )
        }
    }
}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun IntroPreview() {
    AppViewTheme {
        IntroScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun IntroScreen(
    modifier: Modifier = Modifier,
    items: List<IntroModel> = listIntroModel,
    nextScreen: () -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = 0) { items.size }
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage.coerceIn(0, items.lastIndex)
    val currentModel = remember(currentPage, items) { items[currentPage] }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IptvBgPrimary)
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            IntroContent(
                model = items[page],
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(IptvBgSecondary)
                .statusBarsPadding()
                .navigationBarsPadding()
                .paddingHorizontal(_12Sdp)
                .paddingTop(_12Sdp)
                .animateContentSize()
        ) {
            TextView(
                text = stringResource(currentModel.title),
                font = Font(R.font.rb_600),
                textSize = _13Ssp,
                color = IptvTextPrimary,
                minLine = 1,
                modifier = Modifier.fillMaxWidth()
            )

            TextView(
                text = stringResource(currentModel.content),
                font = Font(R.font.rb_400),
                textSize = _12Ssp,
                color = IptvTextSecondary,
                modifier = Modifier.paddingTop(_6Sdp),
                minLine = 2
            )

            SpaceH(_8Sdp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                DotIndicator(
                    totalPage = items.size,
                    currentPage = currentPage,
                    modifier = Modifier
                )

                SpaceF()

                TextView(
                    text = if (currentPage != items.lastIndex) {
                        stringResource(R.string.next)
                    } else {
                        stringResource(R.string.continues)
                    },
                    font = R.font.rb_600,
                    textSize = _13Ssp,
                    color = IptvAccent
                ) {
                    if (currentPage >= items.lastIndex) {
                        nextScreen()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    }
                }

            }
            SpaceH(heightPercentDp(25f))
        }
    }
}

@Composable
fun IntroContent(
    model: IntroModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(model.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DotIndicator(
    totalPage: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPage) { index ->
            val width by animateDpAsState(
                targetValue = if (index == currentPage) _16Sdp else _8Sdp,
                animationSpec = tween(220),
                label = "indicator_width"
            )

            val color = if (index == currentPage) IptvAccent else IptvTextSecondary.copy(alpha = 0.4f)
            Box(
                modifier = Modifier
                    .size(width = width, height = _8Sdp)
                    .background(
                        Brush.linearGradient(listOf(color, color)),
                        shape = CircleShape
                    )
            )

            if (index != totalPage - 1) {
                SpaceW(_6Sdp)
            }
        }
    }
}