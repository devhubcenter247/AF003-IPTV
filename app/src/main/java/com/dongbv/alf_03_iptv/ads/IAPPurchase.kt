package com.dongbv.alf_03_iptv.ads

import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.billing.Entitlement
import com.lib.ads.gma.ads.billing.PurchaseItem

object IAPPurchase {

    val listProductData = mutableListOf(
        PurchaseItem.removeAdsOnly(
            itemId = "remove_ads",
            type = AppPurchase.TYPE_IAP.PURCHASE,
        ),
        PurchaseItem.bundle(
            itemId = "premium_weekly",
            type = AppPurchase.TYPE_IAP.SUBSCRIPTION,
            trialId = "",
            consume = false,
        ),
        PurchaseItem.bundle(
            itemId = "premium_monthly",
            type = AppPurchase.TYPE_IAP.SUBSCRIPTION,
            trialId = "trial-3d",
            consume = false,
        ),
        PurchaseItem.bundle(
            itemId = "premium_yearly",
            type = AppPurchase.TYPE_IAP.SUBSCRIPTION,
            trialId = "trial-3d",
            consume = false,
        )
    )
}
