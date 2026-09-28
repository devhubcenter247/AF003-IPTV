package com.dongbv.alf_03_iptv.ui.main.guide

import com.dongbv.alf_03_iptv.analytics.AppScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.AppNeonButton
import com.dongbv.alf_03_iptv.ui.main.add.AddChannelActivity
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvSurface
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextTertiary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline500
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.banner.params.BannerAdConfig
import com.dongbv.alf_03_iptv.ads.wrapper.banner.BannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.banner.rememberBannerPlacement
import com.lib.ads.gma.ads.helper.banner.preload.BannerAdCard
import com.lib.ads.gma.ads.helper.banner.preload.rememberBannerAd
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

class HowToAddChannelActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.HOW_TO_ADD_CHANNEL


    override fun initialize() = Unit

    @Composable
    override fun ContentView() {
        HowToAddChannelScreen(
            onBack = ::finish,
            onGetStarted = {
                navigateWithInterstitial {
                    launchActivity<AddChannelActivity>()
                    finish()
                }
            }
        )
    }
}

// ── Data ──────────────────────────────────────────────────────────────────────

private data class GuideMethod(
    val icon: Int,
    val title: String,
    val subtitle: String,
    val accent: Color,
    val steps: List<Pair<String, String>>,
    val tip: String
)

@Composable
private fun guideMethods(): List<GuideMethod> = listOf(
    GuideMethod(
        icon = R.drawable.ic_app_url,
        title = stringResource(R.string.guide_method_url_title),
        subtitle = stringResource(R.string.guide_method_url_subtitle),
        accent = IptvAccent,
        steps = listOf(
            stringResource(R.string.guide_url_step1_title) to stringResource(R.string.guide_url_step1_desc),
            stringResource(R.string.guide_url_step2_title) to stringResource(R.string.guide_url_step2_desc),
            stringResource(R.string.guide_url_step3_title) to stringResource(R.string.guide_url_step3_desc),
            stringResource(R.string.guide_url_step4_title) to stringResource(R.string.guide_url_step4_desc),
            stringResource(R.string.guide_url_step5_title) to stringResource(R.string.guide_url_step5_desc)
        ),
        tip = stringResource(R.string.guide_url_tip)
    ),
    GuideMethod(
        icon = R.drawable.ic_app_video,
        title = stringResource(R.string.guide_method_file_title),
        subtitle = stringResource(R.string.guide_method_file_subtitle),
        accent = IptvSuccess,
        steps = listOf(
            stringResource(R.string.guide_file_step1_title) to stringResource(R.string.guide_file_step1_desc),
            stringResource(R.string.guide_file_step2_title) to stringResource(R.string.guide_file_step2_desc),
            stringResource(R.string.guide_file_step3_title) to stringResource(R.string.guide_file_step3_desc),
            stringResource(R.string.guide_file_step4_title) to stringResource(R.string.guide_file_step4_desc),
            stringResource(R.string.guide_file_step5_title) to stringResource(R.string.guide_file_step5_desc)
        ),
        tip = stringResource(R.string.guide_file_tip)
    ),
    GuideMethod(
        icon = R.drawable.ic_app_storage,
        title = stringResource(R.string.guide_method_gallery_title),
        subtitle = stringResource(R.string.guide_method_gallery_subtitle),
        accent = colorSecondary,
        steps = listOf(
            stringResource(R.string.guide_gallery_step1_title) to stringResource(R.string.guide_gallery_step1_desc),
            stringResource(R.string.guide_gallery_step2_title) to stringResource(R.string.guide_gallery_step2_desc),
            stringResource(R.string.guide_gallery_step3_title) to stringResource(R.string.guide_gallery_step3_desc),
            stringResource(R.string.guide_gallery_step4_title) to stringResource(R.string.guide_gallery_step4_desc)
        ),
        tip = stringResource(R.string.guide_gallery_tip)
    )
)

private data class GuideFaq(val question: String, val answer: String)

@Composable
private fun guideFaqs(): List<GuideFaq> = listOf(
    GuideFaq(
        stringResource(R.string.guide_faq_q1),
        stringResource(R.string.guide_faq_a1)
    ),
    GuideFaq(
        stringResource(R.string.guide_faq_q2),
        stringResource(R.string.guide_faq_a2)
    ),
    GuideFaq(
        stringResource(R.string.guide_faq_q3),
        stringResource(R.string.guide_faq_a3)
    ),
    GuideFaq(
        stringResource(R.string.guide_faq_q4),
        stringResource(R.string.guide_faq_a4)
    )
)

// ── Screen ────────────────────────────────────────────────────────────────────
// slide cần bấm save mới hiện slilde đã thêm, file xlsx  khi bấm bằng ở ô input -> chọn vaule trong ô sheet thì không có action gì, filde doc khi hiện bàn phím office view bị đè không cuộn xuống được cuối trang, khó chọn text để  bôi đen, triển khai thên tất cả các ui tính năng mà moduklde hỗ trợ để có thể làm, bấm lưu chưa lưu vào bộ nhớ nogaif
// docx có hỗ trợ thêm bảng không, chèn thêm ảnh, xlsx có hỗ trợ resize cột, hàng không,
// có thể thêm ảnh ... trong ppt khônng, thêm full các tính năng mà modudel hỗ trợ vào app
@Composable
fun HowToAddChannelScreen(
    onBack: () -> Unit,
    onGetStarted: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .backgroundResource(R.drawable.img_bg_app)
            .statusBarPadding()
    ) {
        GuideHeader(onBack = onBack)

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.Sdp)
        ) {
            SpaceH(8.Sdp)
            GuideIntro()

            SpaceH(24.Sdp)
            guideMethods().forEachIndexed { index, method ->
                GuideMethodCard(index = index + 1, method = method)
                SpaceH(16.Sdp)
            }

            SpaceH(8.Sdp)
            GuideAfterAddingSection()

            SpaceH(24.Sdp)
            GuideFaqSection()

            SpaceH(24.Sdp)
        }

        GuideBottomBar(onGetStarted = onGetStarted)

        BannerAdCard(
            holder = rememberBannerPlacement(BannerPlacement.BannerAll),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

@Composable
private fun GuideHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.Sdp, vertical = 4.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.Sdp)
                .clip(CircleShape)
                .background(white.opacity(8f))
                .onClick { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app_back),
                contentDescription = null,
                tint = IptvTextPrimary,
                modifier = Modifier.size(16.Sdp)
            )
        }
        SpaceW(10)
        Text(
            text = stringResource(R.string.how_to_add_new_channel),
            style = headline600(fontSize = 16.Ssp, color = IptvTextPrimary),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GuideIntro() {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.Sdp))
            .background(IptvAccent.copy(alpha = 0.1f))
            .border(1.dp, IptvAccent.copy(alpha = 0.3f), RoundedCornerShape(14.Sdp))
            .padding(14.Sdp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_app_how_use),
                contentDescription = null,
                tint = IptvAccent,
                modifier = Modifier.size(20.Sdp)
            )
            SpaceW(8)
            Text(
                text = stringResource(R.string.guide_intro_title),
                style = headline700(fontSize = 14.Ssp, color = IptvTextPrimary)
            )
        }
        SpaceH(8.Sdp)
        Text(
            text = stringResource(R.string.guide_intro_desc),
            style = headline400(fontSize = 12.Ssp, color = colorSecondary),
            lineHeight = 18.Ssp
        )
    }
}

@Composable
private fun GuideMethodCard(index: Int, method: GuideMethod) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.Sdp))
            .background(IptvSurface)
            .border(1.dp, IptvDivider, RoundedCornerShape(14.Sdp))
            .padding(14.Sdp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.Sdp)
                    .clip(RoundedCornerShape(10.Sdp))
                    .background(method.accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(method.icon),
                    contentDescription = null,
                    tint = method.accent,
                    modifier = Modifier.size(18.Sdp)
                )
            }
            SpaceW(10)
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.guide_method_format, index, method.title),
                    style = headline600(fontSize = 13.Ssp, color = IptvTextPrimary)
                )
                Text(
                    text = method.subtitle,
                    style = headline400(fontSize = 11.Ssp, color = IptvTextTertiary)
                )
            }
        }

        SpaceH(12.Sdp)

        method.steps.forEachIndexed { i, (title, desc) ->
            GuideStepRow(step = i + 1, title = title, desc = desc, accent = method.accent)
            if (i < method.steps.lastIndex) SpaceH(10.Sdp)
        }

        SpaceH(12.Sdp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.Sdp))
                .background(method.accent.copy(alpha = 0.08f))
                .padding(horizontal = 10.Sdp, vertical = 8.Sdp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "💡", fontSize = 13.Ssp)
            SpaceW(8)
            Text(
                text = method.tip,
                style = headline400(fontSize = 11.Ssp, color = method.accent.copy(alpha = 0.9f)),
                lineHeight = 15.Ssp
            )
        }
    }
}

@Composable
private fun GuideStepRow(step: Int, title: String, desc: String, accent: Color) {
    Row {
        Box(
            modifier = Modifier
                .size(20.Sdp)
                .clip(CircleShape)
                .background(accent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step.toString(),
                style = headline600(fontSize = 11.Ssp, color = white)
            )
        }
        SpaceW(10)
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = headline500(fontSize = 12.Ssp, color = IptvTextPrimary)
            )
            SpaceH(2)
            Text(
                text = desc,
                style = headline400(fontSize = 11.Ssp, color = colorSecondary),
                lineHeight = 16.Ssp
            )
        }
    }
}

@Composable
private fun GuideAfterAddingSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.Sdp))
            .background(IptvSurface)
            .border(1.dp, IptvDivider, RoundedCornerShape(14.Sdp))
            .padding(14.Sdp)
    ) {
        Text(
            text = stringResource(R.string.guide_after_adding_title),
            style = headline600(fontSize = 13.Ssp, color = IptvTextPrimary)
        )
        SpaceH(10.Sdp)

        val bullets = listOf(
            R.string.guide_after_adding_point1,
            R.string.guide_after_adding_point2,
            R.string.guide_after_adding_point3,
            R.string.guide_after_adding_point4
        )
        bullets.forEachIndexed { i, res ->
            GuideBullet(text = stringResource(res))
            if (i < bullets.lastIndex) SpaceH(8.Sdp)
        }
    }
}

@Composable
private fun GuideBullet(text: String) {
    Row {
        Box(
            modifier = Modifier
                .padding(top = 6.Sdp)
                .size(5.Sdp)
                .clip(CircleShape)
                .background(IptvAccent)
        )
        SpaceW(10)
        Text(
            text = text,
            style = headline400(fontSize = 11.Ssp, color = colorSecondary),
            lineHeight = 17.Ssp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GuideFaqSection() {
    Column {
        Text(
            text = stringResource(R.string.guide_faq_title),
            style = headline600(fontSize = 13.Ssp, color = IptvTextPrimary)
        )
        SpaceH(10.Sdp)

        val faqs = guideFaqs()
        faqs.forEachIndexed { i, faq ->
            GuideFaqItem(faq)
            if (i < faqs.lastIndex) SpaceH(10.Sdp)
        }
    }
}

@Composable
private fun GuideFaqItem(faq: GuideFaq) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.Sdp))
            .background(IptvSurface)
            .border(1.dp, IptvDivider, RoundedCornerShape(12.Sdp))
            .padding(12.Sdp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                painter = painterResource(R.drawable.ic_app_info),
                contentDescription = null,
                tint = IptvAccent,
                modifier = Modifier.size(14.Sdp)
            )
            SpaceW(8)
            Text(
                text = faq.question,
                style = headline500(fontSize = 12.Ssp, color = IptvTextPrimary),
                modifier = Modifier.weight(1f)
            )
        }
        SpaceH(6.Sdp)
        Text(
            text = faq.answer,
            style = headline400(fontSize = 11.Ssp, color = colorSecondary),
            lineHeight = 16.Ssp,
            modifier = Modifier.padding(start = 22.Sdp)
        )
    }
}

@Composable
private fun GuideBottomBar(onGetStarted: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(IptvBgPrimary)
            .padding(horizontal = 16.Sdp, vertical = 12.Sdp)
    ) {
        AppNeonButton(
            text = stringResource(R.string.guide_cta_button),
            onClick = onGetStarted,
            textStyle = headline600(fontSize = 13.Ssp, color = white),
            modifier = Modifier
                .fillMaxWidth()
                .height(42.Sdp)
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0D0D1A, heightDp = 1400)
@Composable
private fun HowToAddChannelScreenPreview() {
    AppViewTheme {
        HowToAddChannelScreen(
            onBack = {},
            onGetStarted = {}
        )
    }
}
