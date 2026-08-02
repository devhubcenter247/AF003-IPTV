package com.dongbv.alf_03_iptv.ui.main.setting

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.BuildConfig
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.VerScrollView
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.black
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.white
import androidx.core.net.toUri
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

class AboutUsActivity : BaseActivity() {
    override fun initialize() = Unit

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        BoxParentContent(
            onBack = { finish() },
            modifier = Modifier
                .fillMaxSize()

                .padding(top = statusBarHeight)
        ) {
            AboutUsScreen(
                onBack = { finish() },
                modifier = Modifier.paddingTop(statusBarHeight),
                onPrivacyPolicy = { openUrl(PRIVACY_POLICY_URL) },
                onTermsOfService = { openUrl(TERMS_OF_SERVICE_URL) },
                onShareApp = { shareApp() }
            )
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${getString(R.string.check_out_this_amazing_app)}$PLAY_STORE_URL$packageName"
                )
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val PRIVACY_POLICY_URL =
            "https://docs.google.com/document/d/e/2PACX-1vSMQvsGiOECJC7PHKJHbApn2M5cErV1UM8YmRgxBPJxmWuom1kEv9hLaAN_3vv1kg/pub"
        private const val TERMS_OF_SERVICE_URL =
            "https://docs.google.com/document/d/e/2PACX-1vQV1qgRCFPaoknWVvUK0iogrLXmOT8UvWsak9QCD0Ahkb9V6v3gpeMPyv6SZrH7ig/pub"
        private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="
    }
}

@Composable
fun AboutUsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onPrivacyPolicy: () -> Unit,
    onTermsOfService: () -> Unit,
    onShareApp: () -> Unit
) {
    val context: Context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .backgroundResource(R.drawable.img_bg_app)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AboutTopBar(onBack)

        VerScrollView(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            SpaceH(32)


            Image(
                painter = painterResource(R.drawable.img_logo_splash),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(100.dp)
                    .neonState(outerRadius = 6.Sdp, shape = RoundedCornerShape(10.Sdp))
                    .clip(RoundedCornerShape(10.Sdp))
                    .align(Alignment.CenterHorizontally)
            )

            SpaceH(16)


            TextView(
                text = stringResource(R.string.app_name),
                font = R.font.ss_700,
                color = IptvAccent,
                textSize = 16.Ssp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            SpaceH(8)


            TextView(
                text = stringResource(R.string.version_format, BuildConfig.VERSION_NAME),
                font = R.font.ss_400,
                color = white,
                textSize = 12.Ssp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            SpaceH(32)


            Column(
                Modifier
                    .fillMaxWidth()
                    .neonState(true, shape = RoundedCornerShape(12.Sdp), outerRadius = 6.Sdp)
                    .background(IptvBgPrimary, RoundedCornerShape(12.Sdp))
                    .padding(16.dp)
            ) {
                AboutMenuItem(
                    icon = R.drawable.ic_setting_privacy,
                    label = R.string.privacy_policy,
                    onClick = onPrivacyPolicy
                )

                AboutMenuItem(
                    icon = R.drawable.ic_setting_terms,
                    label = R.string.term_of_service,
                    onClick = onTermsOfService
                )

                AboutMenuItem(
                    icon = R.drawable.ic_setting_share,
                    label = R.string.share_app,
                    onClick = onShareApp
                )
            }

            SpaceH(32)


            TextView(
                text = stringResource(R.string.copyright).replace(
                    "%s",
                    stringResource(R.string.app_name)
                ),
                font = R.font.ss_400,
                color = gray,
                textSize = 10.Ssp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            SpaceH(16)
        }
    }
}

@Composable
private fun AboutTopBar(onBack: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .paddingHorizontal(8.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.Sdp)
                .background(white.opacity(8), CircleShape)
                .onClick(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_back),
                contentDescription = null,
                tint = IptvTextPrimary,
                modifier = Modifier.size(14.Sdp)
            )
        }

        TextView(
            text = R.string.about_us,
            font = R.font.ss_700,
            color = white,
            textSize = 16.Ssp
        )
    }
}

@Composable
private fun AboutMenuItem(
    @DrawableRes icon: Int,
    @StringRes label: Int,

    onClick: () -> Unit
) {
    Box(Modifier.padding(vertical = 12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconView(
                item = icon,
                tint = IptvAccent,
                modifier = Modifier
            )
            SpaceW(12)
            TextView(
                text = stringResource(label),
                font = R.font.ss_400,
                textSize = 12.Ssp,
                color = white
            )
            Spacer(Modifier.weight(1f))
            IconView(
                item = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                tint = black,
                modifier = Modifier
            )
        }
        Box(
            Modifier
                .matchParentSize()
                .onClick { onClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutUsScreenPreview() {
    Box(
        Modifier
            .fillMaxSize()

    ) {
        AboutUsScreen(
            modifier = Modifier.paddingTop(28.Sdp),
            onBack = {},
            onPrivacyPolicy = {},
            onTermsOfService = {},
            onShareApp = {}
        )
    }
}