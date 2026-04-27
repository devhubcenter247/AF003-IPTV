package com.dongbv.alf_03_iptv.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style._11Ssp
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._18Ssp
import com.dongbv.alf_03_iptv.style.getScreenWidthInDp
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.ui.language.LanguageActivity
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun initialize() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            goNextPage()
        }
    }



    override fun isFinishFirstFlow() = PreferenceData.isFinishFirstFlow
    fun goNextPage() {
        if (!PreferenceData.isFinishFirstFlow) {
            launchActivity<LanguageActivity>()
            finish()
        } else {
            launchActivity<MainActivity>()
            finish()
        }
    }
    @Composable
    override fun BoxScope.ContentView(statusBarHeight: Dp) {
        BoxParentContent(onBack = {}, modifier = Modifier.fillMaxSize()) {
            SplashScreen(
                Modifier
                    .background(IptvBgPrimary)
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
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpaceH(h = getScreenWidthInDp() * 0.35f)

        Image(
            painter = painterResource(R.drawable.img_logo_splash),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(_12Sdp))
        )

        SpaceH(h = _12Sdp)
        Text(
            text = stringResource(R.string.app_name),
            fontFamily = FontFamily(Font(R.font.ss_700)),
            color = IptvAccent,
            textAlign = TextAlign.Center,
            fontSize = _18Ssp,
            letterSpacing = 1.5.sp
        )

        Spacer(Modifier.weight(1f))
        TextView(
            text = stringResource(R.string.this_action_may_be_constant_adverting),
            modifier = Modifier.padding(bottom = 12.dp),
            textSize = _11Ssp,
            color = IptvTextSecondary,
            font = R.font.ss_400
        )
        LinearProgressIndicator(
            modifier = Modifier.height(6.dp),
            color = IptvAccent,
            trackColor = IptvAccent.opacity(.25f),
            strokeCap = StrokeCap.Round
        )
        SpaceH(h = 32.dp)
    }
}