package com.dongbv.alf_03_iptv.ads.model

import com.google.gson.annotations.SerializedName

data class SkipInterSplash(
    @SerializedName("enable")
    val enable: Boolean = true,
    @SerializedName("time_out_ms")
    val timeOutMS : Long = 0L
){
    companion object{
        val defaultData = SkipInterSplash(
            true,
            5000
        )
    }
}

data class OnboardingScreen(
    @SerializedName("screen_1")
    val screen1: Boolean = false,
    @SerializedName("screen_2")
    val screen2: Boolean = false,
    @SerializedName("screen_3")
    val screen3: Boolean = false,
    @SerializedName("screen_4")
    val screen4: Boolean = false,
){
    fun enableCount(): Int = listOf(screen1, screen2, screen3, screen4).count { it }
    companion object{
        val defaultOnboardingScreen = OnboardingScreen(
            screen1 = true,
            screen2 = true,
            screen3 = true,
            screen4 = true
        )
    }
}