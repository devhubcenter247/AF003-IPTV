package com.dongbv.alf_03_iptv.ui.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.dongbv.alf_03_iptv.BuildConfig
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.remoteconfig.remoteConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.GradientLinearProgressIndicator
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.getScreenWidthInDp
import com.dongbv.alf_03_iptv.style.widthPercent
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.ui.language.LanguageActivity
import com.dongbv.alf_03_iptv.ui.language.LanguageScreenType
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.mainColor
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun initialize() {
        lifecycleScope.launch(Dispatchers.Main) {
            awaitAll(
                async { remoteConfig.setupRemoteConfig(BuildConfig.DEBUG) }
            )
            delay(1500.milliseconds)
            goNextPage()
        }
    }


    override fun isFinishFirstFlow() = AppPreferences.isFinishFirstFlow
    fun goNextPage() {
        if (!AppPreferences.isFinishFirstFlow) {
            LanguageActivity.startLanguage(this@SplashActivity, LanguageScreenType.Language1)
            finish()
        } else {
            launchActivity<MainActivity>()
            finish()
        }
    }

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        BoxParentContent(onBack = {}, modifier = Modifier.fillMaxSize()) {
            SplashScreen(
                Modifier
                    .padding(top = statusBarHeight)
            )
        }
    }
}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun SplashPreview() {
    AppViewTheme {
        SplashScreen(Modifier)
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Column(
        Modifier
            .fillMaxSize()
            .backgroundResource(R.drawable.img_bg_app)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpaceH(h = getScreenWidthInDp() * 0.35f)

        Image(
            painter = painterResource(R.drawable.img_logo_splash),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(92.Sdp)
                .neonState(true, outerRadius = 4.Sdp, shape = RoundedCornerShape(12.Sdp))
                .clip(RoundedCornerShape(12.Sdp))
        )

        SpaceH(h = 12.Sdp)
        Text(
            text = stringResource(R.string.app_name),
            fontFamily = FontFamily(Font(R.font.ss_700)),
            color = IptvAccent,
            textAlign = TextAlign.Center,
            fontSize = 18.Ssp,
            letterSpacing = 1.5.sp
        )

        Spacer(Modifier.weight(1f))
        TextView(
            text = stringResource(R.string.this_action_may_be_constant_adverting),
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            textSize = 11.Ssp,
            color = colorSecondary,
            font = R.font.ss_400
        )
        GradientLinearProgressIndicator(
            colors = listOf(grdStart, grdEnd),
            modifier = Modifier
                .height(6.dp)
                .widthPercent(60f),
            strokeCap = StrokeCap.Round,
            trackColor = mainColor.copy(alpha = 0.2f)
        )
        SpaceH(h = 32.dp)
    }
}