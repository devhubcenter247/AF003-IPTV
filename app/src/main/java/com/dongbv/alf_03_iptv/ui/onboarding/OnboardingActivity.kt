package com.dongbv.alf_03_iptv.ui.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.SpaceF
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.backgroundResource

import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.style.heightPercentDp
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.ui.personalization.PersonalizationActivity
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.launch

class IntroActivity : BaseActivity() {
    override fun initialize() = Unit

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        BoxParentContent(onBack = { finishAffinity() }) {
            IntroScreen(
                modifier = Modifier.background(IptvBgPrimary),
                nextScreen = {
                    launchActivity<PersonalizationActivity>()
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
        modifier = Modifier
            .fillMaxSize()
            .backgroundResource(R.drawable.img_bg_app)
            .then(modifier)
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
                .background(
                    Brush.verticalGradient(
                        listOf(
                            IptvBgPrimary.copy(alpha = 0.1f),
                            IptvBgPrimary,
                            IptvBgPrimary,
                            IptvBgPrimary,
                        )
                    )
                )
                .paddingHorizontal(16.Sdp)
                .paddingTop(12.Sdp)
                .animateContentSize()
        ) {
            TextView(
                text = stringResource(currentModel.title),
                font = Font(R.font.ss_700),
                textSize = 22.Ssp,
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF00F5FF), // Cyan
                        Color(0xFF4FAAF2), // Sky blue
                        Color(0xFF3639FF), // Indigo
                        Color(0xFFC596FF), // Purple
                        Color(0xFFE363FF)
                    )
                ),
                minLine = 1,
                maxLine = 2,
                modifier = Modifier.fillMaxWidth()
            )

            TextView(
                text = stringResource(currentModel.content),
                font = Font(R.font.ss_400),
                textSize = 12.Ssp,
                color = colorSecondary,
                modifier = Modifier.paddingTop(6.Sdp),
                minLine = 2,
                maxLine = 2
            )
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
                        stringResource(R.string.get_started)
                    },
                    font = R.font.ss_600,
                    textSize = 13.Ssp,
                    color = IptvAccent,
                    modifier = Modifier
                        .heightIn(min = 32.Sdp)
                        .wrapContentHeight(Alignment.CenterVertically)
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
            SpaceH(heightPercentDp(18f))
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
                targetValue = if (index == currentPage) 16.Sdp else 8.Sdp,
                animationSpec = tween(220),
                label = "indicator_width"
            )

            val colors = if (index == currentPage) listOf(grdStart, grdEnd) else listOf(
                colorSecondary.copy(alpha = 0.4f), colorSecondary.copy(alpha = 0.4f)
            )
            Box(
                modifier = Modifier
                    .size(width = width, height = 8.Sdp)
                    .background(
                        brush = Brush.horizontalGradient(colors),
                        shape = CircleShape
                    )
            )

            if (index != totalPage - 1) {
                SpaceW(6.Sdp)
            }
        }
    }
}