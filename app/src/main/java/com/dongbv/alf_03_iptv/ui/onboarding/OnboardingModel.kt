package com.dongbv.alf_03_iptv.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dongbv.alf_03_iptv.R

data class IntroModel(@DrawableRes var imageRes: Int = 0,
                      @StringRes var title: Int,
                      @StringRes var content: Int)

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
