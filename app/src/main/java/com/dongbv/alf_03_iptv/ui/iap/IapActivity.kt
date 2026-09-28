package com.dongbv.alf_03_iptv.ui.iap

import com.dongbv.alf_03_iptv.analytics.AppScreen
import android.app.Activity
import android.content.Intent
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.IAPPurchase
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.PhonePreview
import com.dongbv.alf_03_iptv.style.SpaceF
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.heightPercent
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.billing.PurchaseEvent

class IapActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.IAP

    companion object {
        /** Returns false when the remote switch disables the paywall. No interstitial here. */
        fun open(activity: Activity): Boolean {
            if (!remoteConfig.paywallEnabled || activity.isFinishing || activity.isDestroyed) return false
            activity.startActivity(Intent(activity, IapActivity::class.java))
            return true
        }
    }

    private val purchase = AppPurchase.getInstance()
    private var prices by mutableStateOf(PlanPrices())
    private var selectedPlan by mutableStateOf(Plan.YEARLY)

    override fun initialize() {
        if (!remoteConfig.paywallEnabled) {
            finish()
            return
        }
        purchase.setBillingListener {
            refreshPrices()
            if (it == 0) refreshPrices()
        }
        refreshPrices()
    }

    private fun refreshPrices() {
        prices = PlanPrices(
            weekly = purchase.getPriceSub("premium_weekly").ifBlank { "—" },
            monthly = purchase.getPriceSub("premium_monthly").ifBlank { "—" },
            yearly = purchase.getPriceSub("premium_yearly").ifBlank { "—" },
        )
    }

    private fun buySelected() {
        if (!remoteConfig.paywallEnabled) {
            finish()
            return
        }
        val id = selectedPlan.productId
        purchase.subscribe(this, id) { event ->
            when (event) {
                is PurchaseEvent.Success -> finish()
                is PurchaseEvent.Pending -> Toast.makeText(
                    this,
                    "Purchase is pending",
                    Toast.LENGTH_SHORT
                ).show()

                PurchaseEvent.Cancelled -> Unit
                is PurchaseEvent.Error -> Toast.makeText(this, event.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    @Composable
    override fun ContentView() {
        if (!remoteConfig.paywallEnabled) return
        IapScreen(
            prices = prices,
            selectedPlan = selectedPlan,
            onSelectPlan = { selectedPlan = it },
            onPurchase = ::buySelected,
            onClose = ::finish,
        )
    }
}

private enum class Plan(val productId: String, val label: String) {
    YEARLY("premium_yearly", "Yearly"),
    MONTHLY("premium_monthly", "Monthly"),
    WEEKLY("premium_weekly", "Weekly")
}

private data class PlanPrices(
    val weekly: String = "—",
    val monthly: String = "—",
    val yearly: String = "—",
)

@Composable
private fun IapScreen(
    prices: PlanPrices,
    selectedPlan: Plan,
    onSelectPlan: (Plan) -> Unit,
    onPurchase: () -> Unit,
    onClose: () -> Unit,
) {
    val dark = IptvBgPrimary
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(dark)
            .verticalScroll(rememberScrollState())
            .backgroundResource(R.drawable.img_bg_iap),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(end = 16.Sdp, bottom = 76.Sdp)
                .clip(RoundedCornerShape(10.Sdp))
                .background(Color(0xFFFF3342))
                .padding(horizontal = 11.Sdp, vertical = 6.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(8.Sdp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            SpaceW(7.Sdp)
            TextView("Live", color = IptvTextPrimary, font = R.font.ss_700, textSize = 12.Ssp)
        }
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Close",
            tint = Color.White,
            modifier = Modifier
                .statusBarPadding()
                .padding(top = 12.Sdp, start = 20.Sdp)
                .size(24.Sdp)
                .clickable(onClick = onClose)
        )

        Column(
            modifier = Modifier
                .padding(bottom = 18.Sdp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextView(
                stringResource(R.string.app_name),
                color = IptvTextPrimary,
                font = R.font.ss_700,
                textSize = 18.Ssp
            )
            TextView(
                "Premium", color = IptvTextPrimary, font = R.font.ss_700, textSize = 16.Ssp,
                modifier = Modifier
                    .clip(RoundedCornerShape(32.Sdp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0873FF),
                                Color(0xFF4AD5F1)
                            )
                        )
                    )
                    .padding(horizontal = 18.Sdp, vertical = 7.Sdp)
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.Sdp)
        ) {
            Feature(Icons.Rounded.RemoveCircleOutline, "No more ads")
            Feature(Icons.Rounded.AllInclusive, "Enjoy unlimited channels")
            Feature(Icons.Rounded.VideoLibrary, "Easily organize your channels")
            SpaceH(20.Sdp)
            PlanCard(
                Plan.YEARLY,
                prices.yearly,
                selectedPlan == Plan.YEARLY,
                "BEST SELLER",
                onSelectPlan
            )
            PlanCard(Plan.MONTHLY, prices.monthly, selectedPlan == Plan.MONTHLY, null, onSelectPlan)
            PlanCard(
                Plan.WEEKLY,
                prices.weekly,
                selectedPlan == Plan.WEEKLY,
                "Best Offer",
                onSelectPlan
            )
            TextView(
                stringResource(R.string.auto_renewable_cancel_anytime),
                color = IptvTextPrimary,
                textSize = 13.Ssp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.Sdp),
                textAlign = TextAlign.Center
            )
            SpaceF()
            TextView(
                stringResource(R.string.subscribe_now),
                color = IptvTextPrimary,
                font = R.font.ss_700,
                textSize = 12.Ssp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.Sdp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0873FF),
                                Color(0xFF4AD5F1)
                            )
                        )
                    )
                    .clickable(onClick = onPurchase)
                    .padding(vertical = 12.Sdp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.Sdp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextView(
                    stringResource(R.string.term_of_service),
                    color = IptvTextSecondary,
                    textSize = 11.Ssp
                )
                SpaceW(44.Sdp)
                TextView(
                    stringResource(R.string.privacy_policy),
                    color = IptvTextSecondary,
                    textSize = 11.Ssp
                )
            }
        }
    }
}

@Composable
private fun Feature(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = IptvTextPrimary,
            modifier = Modifier
                .size(26.Sdp)
                .width(38.Sdp)
        )
        TextView(text, color = IptvTextPrimary, textSize = 12.Ssp)
    }
}

@Composable
private fun PlanCard(
    plan: Plan,
    price: String,
    selected: Boolean,
    badge: String?,
    onSelect: (Plan) -> Unit
) {
    val blue = Color(0xFF096BFA)
    Box(
        Modifier
            .fillMaxWidth()
            .paddingTop(10.Sdp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .paddingTop(12.Sdp)
                .clip(RoundedCornerShape(12.Sdp))
                .background(if (selected) blue else Color(0xFF303136))
                .border(
                    1.Sdp,
                    if (selected) blue else Color(0xFF5A5B61),
                    RoundedCornerShape(12.Sdp)
                )
                .clickable { onSelect(plan) }
                .padding(horizontal = 16.Sdp, vertical = 12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Check,
                null,
                tint = if (selected) blue else Color.Transparent,
                modifier = Modifier
                    .size(20.Sdp)
                    .clip(CircleShape)
                    .background(if (selected) Color.White else Color.Transparent)
                    .border(2.Sdp, if (selected) Color.White else Color(0xFF9699A5), CircleShape)
                    .padding(4.Sdp)
            )
            SpaceW(18.Sdp)
            TextView(plan.label, color = IptvTextPrimary, font = R.font.ss_700, textSize = 14.Ssp)
            Spacer(Modifier.weight(1f))
            TextView(price, color = IptvTextPrimary, font = R.font.ss_700, textSize = 19.Ssp)
        }
        badge?.let {
            TextView(
                it,
                color = IptvTextPrimary,
                font = R.font.ss_700,
                textSize = 12.Ssp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(10.Sdp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFF9A2E),
                                Color(0xFFFF3218)
                            )
                        )
                    )
                    .padding(horizontal = 12.Sdp, vertical = 6.Sdp)
            )
        }
    }
}

@PhonePreview
@Composable
private fun IapScreenPreview() {
    IapScreen(
        prices = PlanPrices(
            weekly = "$4.99",
            monthly = "$14.99",
            yearly = "$29.99",
        ),
        selectedPlan = Plan.YEARLY,
        onSelectPlan = {},
        onPurchase = {},
        onClose = {},
    )
}
