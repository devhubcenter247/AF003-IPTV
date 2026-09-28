package com.dongbv.alf_03_iptv.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.model.OnboardingScreen
import com.dongbv.alf_03_iptv.app.remote.remoteConfig

data class IntroModel(@DrawableRes var imageRes: Int = 0,
                      @StringRes var title: Int,
                      @StringRes var content: Int)


sealed class OnboardingPage {
    data class Slide(
        val index: Int,
        val image: Int,
        val title: Int,
        val content: Int,
        val canShowAd : Boolean = true
    ) : OnboardingPage()

    data object FullAd : OnboardingPage()
}

fun setupOnboardingPage(
    isShowNativeFull1: Boolean = false,
    onboardingScreen: OnboardingScreen = OnboardingScreen.defaultOnboardingScreen
): List<OnboardingPage> = buildList {
    add(
        OnboardingPage.Slide(
            index = 0,
            image = R.drawable.img_onboarding_1,
            title = R.string.intro_title_1,
            content = R.string.intro_content_1,
            canShowAd = onboardingScreen.screen1
        )
    )
    add(
        OnboardingPage.Slide(
            index = 1,
            image = R.drawable.img_onboarding_2,
            title = R.string.intro_title_2,
            content = R.string.intro_content_2,
            canShowAd = onboardingScreen.screen2
        )
    )

    if (isShowNativeFull1) {
        add(OnboardingPage.FullAd)
    }

    add(
        OnboardingPage.Slide(
            index = 2,
            image = R.drawable.img_onboarding_3,
            title = R.string.intro_title_3,
            content = R.string.intro_content_3,
            canShowAd = onboardingScreen.screen3
        )
    )
    add(
        OnboardingPage.Slide(
            index = 2,
            image = R.drawable.img_onboarding_4,
            title = R.string.intro_title_4,
            content = R.string.intro_content_4,
            canShowAd = onboardingScreen.screen4
        )
    )
}
val listIntroModel: List<IntroModel>
    get() = listOf(
        IntroModel(
            imageRes = R.drawable.img_onboarding_1,
            title = R.string.intro_title_1,
            content = R.string.intro_content_1
        ),
        IntroModel(
            imageRes = R.drawable.img_onboarding_2,
            title = R.string.intro_title_2,
            content = R.string.intro_content_2
        ),
        IntroModel(
            imageRes = R.drawable.img_onboarding_3,
            title = R.string.intro_title_3,
            content = R.string.intro_content_3
        ),
        IntroModel(
            imageRes = R.drawable.img_onboarding_4,
            title = R.string.intro_title_4,
            content = R.string.intro_content_4
        ),
    )
