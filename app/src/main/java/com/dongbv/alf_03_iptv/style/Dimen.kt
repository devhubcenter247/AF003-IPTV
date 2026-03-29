@file:Suppress("unused")

package com.dongbv.alf_03_iptv.style

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import kotlin.math.min

val Int.sdp: Dp
    @Composable
    get() = this.sdpGetSize()
@Composable
fun Int.sdpGetSize(): Dp {
    val minValue = min(getScreenHeight(), getScreenWidth())
    val ratio = minValue / 300.0
    return (this * ratio).dp
}

val _1Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._1sdp).toSp()
    }
val _2Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._2sdp).toSp()
    }
val _3Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._3sdp).toSp()
    }
val _4Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._4sdp).toSp()
    }
val _5Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._5sdp).toSp()
    }
val _6Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._6sdp).toSp()
    }
val _7Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._7sdp).toSp()
    }
val _8Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._8sdp).toSp()
    }
val _9Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._9sdp).toSp()
    }
val _10Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._10sdp).toSp()
    }
val _11Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._11sdp).toSp()
    }
val _12Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._12sdp).toSp()
    }
val _13Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._13sdp).toSp()
    }
val _14Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._14sdp).toSp()
    }
val _15Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._15sdp).toSp()
    }
val _16Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._16sdp).toSp()
    }
val _17Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._17sdp).toSp()
    }
val _18Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._18sdp).toSp()
    }
val _19Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._19sdp).toSp()
    }
val _20Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._20sdp).toSp()
    }
val _21Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._21sdp).toSp()
    }
val _22Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._22sdp).toSp()
    }
val _23Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._23sdp).toSp()
    }
val _24Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._24sdp).toSp()
    }
val _25Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._25sdp).toSp()
    }
val _26Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._26sdp).toSp()
    }
val _27Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._27sdp).toSp()
    }
val _28Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._28sdp).toSp()
    }
val _29Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._29sdp).toSp()
    }
val _30Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._30sdp).toSp()
    }
val _31Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._31sdp).toSp()
    }
val _32Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._32sdp).toSp()
    }
val _33Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._33sdp).toSp()
    }
val _34Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._34sdp).toSp()
    }
val _35Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._35sdp).toSp()
    }
val _36Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._36sdp).toSp()
    }
val _37Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._37sdp).toSp()
    }
val _38Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._38sdp).toSp()
    }
val _39Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._39sdp).toSp()
    }
val _40Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._40sdp).toSp()
    }
val _41Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._41sdp).toSp()
    }
val _42Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._42sdp).toSp()
    }
val _43Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._43sdp).toSp()
    }
val _44Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._44sdp).toSp()
    }
val _45Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._45sdp).toSp()
    }
val _46Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._46sdp).toSp()
    }
val _47Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._47sdp).toSp()
    }
val _48Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._48sdp).toSp()
    }
val _49Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._49sdp).toSp()
    }
val _50Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._50sdp).toSp()
    }
val _51Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._51sdp).toSp()
    }
val _52Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._52sdp).toSp()
    }
val _53Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._53sdp).toSp()
    }
val _54Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._54sdp).toSp()
    }
val _55Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._55sdp).toSp()
    }
val _56Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._56sdp).toSp()
    }
val _57Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._57sdp).toSp()
    }
val _58Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._58sdp).toSp()
    }
val _59Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._59sdp).toSp()
    }
val _60Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._60sdp).toSp()
    }
val _61Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._61sdp).toSp()
    }
val _62Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._62sdp).toSp()
    }
val _63Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._63sdp).toSp()
    }
val _64Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._64sdp).toSp()
    }
val _65Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._65sdp).toSp()
    }
val _66Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._66sdp).toSp()
    }
val _67Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._67sdp).toSp()
    }
val _68Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._68sdp).toSp()
    }
val _69Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._69sdp).toSp()
    }
val _70Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._70sdp).toSp()
    }
val _71Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._71sdp).toSp()
    }
val _72Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._72sdp).toSp()
    }
val _73Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._73sdp).toSp()
    }
val _74Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._74sdp).toSp()
    }
val _75Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._75sdp).toSp()
    }
val _76Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._76sdp).toSp()
    }
val _77Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._77sdp).toSp()
    }
val _78Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._78sdp).toSp()
    }
val _79Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._79sdp).toSp()
    }
val _80Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._80sdp).toSp()
    }
val _81Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._81sdp).toSp()
    }
val _82Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._82sdp).toSp()
    }
val _83Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._83sdp).toSp()
    }
val _84Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._84sdp).toSp()
    }
val _85Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._85sdp).toSp()
    }
val _86Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._86sdp).toSp()
    }
val _87Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._87sdp).toSp()
    }
val _88Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._88sdp).toSp()
    }
val _89Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._89sdp).toSp()
    }
val _90Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._90sdp).toSp()
    }
val _91Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._91sdp).toSp()
    }
val _92Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._92sdp).toSp()
    }
val _93Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._93sdp).toSp()
    }
val _94Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._94sdp).toSp()
    }
val _95Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._95sdp).toSp()
    }
val _96Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._96sdp).toSp()
    }
val _97Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._97sdp).toSp()
    }
val _98Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._98sdp).toSp()
    }
val _99Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._99sdp).toSp()
    }
val _100Ssp: TextUnit
    @Composable get() = with(LocalDensity.current) {
        dimensionResource(id = com.intuit.sdp.R.dimen._100sdp).toSp()
    }
// sdp
val _1Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._1sdp)
val _2Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp)
val _3Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._3sdp)
val _4Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._4sdp)
val _5Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
val _6Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._6sdp)
val _7Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._7sdp)
val _8Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)
val _9Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._9sdp)
val _10Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._10sdp)
val _11Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._11sdp)
val _12Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)
val _13Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._13sdp)
val _14Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._14sdp)
val _15Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._15sdp)
val _16Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)
val _17Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._17sdp)
val _18Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._18sdp)
val _19Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._19sdp)
val _20Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)
val _21Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._21sdp)
val _22Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._22sdp)
val _23Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._23sdp)
val _24Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._24sdp)
val _25Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._25sdp)
val _26Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._26sdp)
val _27Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._27sdp)
val _28Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._28sdp)
val _29Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._29sdp)
val _30Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._30sdp)
val _31Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._31sdp)
val _32Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._32sdp)
val _33Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._33sdp)
val _34Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._34sdp)
val _35Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._35sdp)
val _36Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._36sdp)
val _37Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._37sdp)
val _38Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._38sdp)
val _39Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._39sdp)
val _40Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._40sdp)
val _41Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._41sdp)
val _42Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._42sdp)
val _43Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._43sdp)
val _44Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._44sdp)
val _45Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._45sdp)
val _46Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._46sdp)
val _47Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._47sdp)
val _48Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._48sdp)
val _49Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._49sdp)
val _50Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._50sdp)
val _51Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._51sdp)
val _52Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._52sdp)
val _53Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._53sdp)
val _54Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._54sdp)
val _55Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._55sdp)
val _56Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._56sdp)
val _57Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._57sdp)
val _58Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._58sdp)
val _59Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._59sdp)
val _60Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._60sdp)
val _61Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._61sdp)
val _62Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._62sdp)
val _63Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._63sdp)
val _64Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._64sdp)
val _65Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._65sdp)
val _66Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._66sdp)
val _67Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._67sdp)
val _68Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._68sdp)
val _69Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._69sdp)
val _70Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._70sdp)
val _71Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._71sdp)
val _72Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._72sdp)
val _73Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._73sdp)
val _74Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._74sdp)
val _75Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._75sdp)
val _76Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._76sdp)
val _77Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._77sdp)
val _78Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._78sdp)
val _79Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._79sdp)
val _80Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._80sdp)
val _81Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._81sdp)
val _82Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._82sdp)
val _83Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._83sdp)
val _84Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._84sdp)
val _85Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._85sdp)
val _86Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._86sdp)
val _87Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._87sdp)
val _88Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._88sdp)
val _89Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._89sdp)
val _90Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._90sdp)
val _91Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._91sdp)
val _92Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._92sdp)
val _93Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._93sdp)
val _94Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._94sdp)
val _95Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._95sdp)
val _96Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._96sdp)
val _97Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._97sdp)
val _98Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._98sdp)
val _99Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._99sdp)
val _100Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._100sdp)
val _101Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._101sdp)
val _102Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._102sdp)
val _103Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._103sdp)
val _104Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._104sdp)
val _105Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._105sdp)
val _106Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._106sdp)
val _107Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._107sdp)
val _108Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._108sdp)
val _109Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._109sdp)
val _110Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._110sdp)
val _111Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._111sdp)
val _112Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._112sdp)
val _113Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._113sdp)
val _114Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._114sdp)
val _115Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._115sdp)
val _116Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._116sdp)
val _117Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._117sdp)
val _118Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._118sdp)
val _119Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._119sdp)
val _120Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._120sdp)
val _121Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._121sdp)
val _122Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._122sdp)
val _123Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._123sdp)
val _124Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._124sdp)
val _125Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._125sdp)
val _126Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._126sdp)
val _127Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._127sdp)
val _128Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._128sdp)
val _129Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._129sdp)
val _130Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._130sdp)
val _131Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._131sdp)
val _132Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._132sdp)
val _133Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._133sdp)
val _134Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._134sdp)
val _135Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._135sdp)
val _136Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._136sdp)
val _137Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._137sdp)
val _138Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._138sdp)
val _139Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._139sdp)
val _140Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._140sdp)
val _141Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._141sdp)
val _142Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._142sdp)
val _143Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._143sdp)
val _144Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._144sdp)
val _145Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._145sdp)
val _146Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._146sdp)
val _147Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._147sdp)
val _148Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._148sdp)
val _149Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._149sdp)
val _150Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._150sdp)
val _151Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._151sdp)
val _152Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._152sdp)
val _153Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._153sdp)
val _154Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._154sdp)
val _155Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._155sdp)
val _156Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._156sdp)
val _157Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._157sdp)
val _158Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._158sdp)
val _159Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._159sdp)
val _160Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._160sdp)
val _161Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._161sdp)
val _162Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._162sdp)
val _163Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._163sdp)
val _164Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._164sdp)
val _165Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._165sdp)
val _166Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._166sdp)
val _167Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._167sdp)
val _168Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._168sdp)
val _169Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._169sdp)
val _170Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._170sdp)
val _171Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._171sdp)
val _172Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._172sdp)
val _173Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._173sdp)
val _174Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._174sdp)
val _175Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._175sdp)
val _176Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._176sdp)
val _177Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._177sdp)
val _178Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._178sdp)
val _179Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._179sdp)
val _180Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._180sdp)
val _181Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._181sdp)
val _182Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._182sdp)
val _183Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._183sdp)
val _184Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._184sdp)
val _185Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._185sdp)
val _186Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._186sdp)
val _187Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._187sdp)
val _188Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._188sdp)
val _189Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._189sdp)
val _190Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._190sdp)
val _191Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._191sdp)
val _192Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._192sdp)
val _193Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._193sdp)
val _194Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._194sdp)
val _195Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._195sdp)
val _196Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._196sdp)
val _197Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._197sdp)
val _198Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._198sdp)
val _199Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._199sdp)
val _200Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._200sdp)
val _201Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._201sdp)
val _202Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._202sdp)
val _203Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._203sdp)
val _204Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._204sdp)
val _205Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._205sdp)
val _206Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._206sdp)
val _207Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._207sdp)
val _208Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._208sdp)
val _209Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._209sdp)
val _210Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._210sdp)
val _211Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._211sdp)
val _212Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._212sdp)
val _213Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._213sdp)
val _214Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._214sdp)
val _215Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._215sdp)
val _216Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._216sdp)
val _217Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._217sdp)
val _218Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._218sdp)
val _219Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._219sdp)
val _220Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._220sdp)
val _221Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._221sdp)
val _222Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._222sdp)
val _223Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._223sdp)
val _224Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._224sdp)
val _225Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._225sdp)
val _226Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._226sdp)
val _227Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._227sdp)
val _228Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._228sdp)
val _229Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._229sdp)
val _230Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._230sdp)
val _231Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._231sdp)
val _232Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._232sdp)
val _233Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._233sdp)
val _234Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._234sdp)
val _235Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._235sdp)
val _236Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._236sdp)
val _237Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._237sdp)
val _238Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._238sdp)
val _239Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._239sdp)
val _240Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._240sdp)
val _241Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._241sdp)
val _242Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._242sdp)
val _243Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._243sdp)
val _244Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._244sdp)
val _245Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._245sdp)
val _246Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._246sdp)
val _247Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._247sdp)
val _248Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._248sdp)
val _249Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._249sdp)
val _250Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._250sdp)
val _251Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._251sdp)
val _252Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._252sdp)
val _253Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._253sdp)
val _254Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._254sdp)
val _255Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._255sdp)
val _256Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._256sdp)
val _257Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._257sdp)
val _258Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._258sdp)
val _259Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._259sdp)
val _260Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._260sdp)
val _261Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._261sdp)
val _262Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._262sdp)
val _263Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._263sdp)
val _264Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._264sdp)
val _265Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._265sdp)
val _266Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._266sdp)
val _267Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._267sdp)
val _268Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._268sdp)
val _269Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._269sdp)
val _270Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._270sdp)
val _271Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._271sdp)
val _272Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._272sdp)
val _273Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._273sdp)
val _274Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._274sdp)
val _275Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._275sdp)
val _276Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._276sdp)
val _277Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._277sdp)
val _278Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._278sdp)
val _279Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._279sdp)
val _280Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._280sdp)
val _281Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._281sdp)
val _282Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._282sdp)
val _283Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._283sdp)
val _284Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._284sdp)
val _285Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._285sdp)
val _286Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._286sdp)
val _287Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._287sdp)
val _288Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._288sdp)
val _289Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._289sdp)
val _290Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._290sdp)
val _291Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._291sdp)
val _292Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._292sdp)
val _293Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._293sdp)
val _294Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._294sdp)
val _295Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._295sdp)
val _296Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._296sdp)
val _297Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._297sdp)
val _298Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._298sdp)
val _299Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._299sdp)
val _300Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._300sdp)
val _301Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._301sdp)
val _302Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._302sdp)
val _303Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._303sdp)
val _304Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._304sdp)
val _305Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._305sdp)
val _306Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._306sdp)
val _307Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._307sdp)
val _308Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._308sdp)
val _309Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._309sdp)
val _310Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._310sdp)
val _311Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._311sdp)
val _312Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._312sdp)
val _313Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._313sdp)
val _314Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._314sdp)
val _315Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._315sdp)
val _316Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._316sdp)
val _317Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._317sdp)
val _318Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._318sdp)
val _319Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._319sdp)
val _320Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._320sdp)
val _321Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._321sdp)
val _322Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._322sdp)
val _323Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._323sdp)
val _324Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._324sdp)
val _325Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._325sdp)
val _326Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._326sdp)
val _327Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._327sdp)
val _328Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._328sdp)
val _329Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._329sdp)
val _330Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._330sdp)
val _331Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._331sdp)
val _332Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._332sdp)
val _333Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._333sdp)
val _334Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._334sdp)
val _335Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._335sdp)
val _336Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._336sdp)
val _337Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._337sdp)
val _338Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._338sdp)
val _339Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._339sdp)
val _340Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._340sdp)
val _341Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._341sdp)
val _342Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._342sdp)
val _343Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._343sdp)
val _344Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._344sdp)
val _345Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._345sdp)
val _346Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._346sdp)
val _347Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._347sdp)
val _348Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._348sdp)
val _349Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._349sdp)
val _350Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._350sdp)
val _351Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._351sdp)
val _352Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._352sdp)
val _353Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._353sdp)
val _354Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._354sdp)
val _355Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._355sdp)
val _356Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._356sdp)
val _357Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._357sdp)
val _358Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._358sdp)
val _359Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._359sdp)
val _360Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._360sdp)
val _361Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._361sdp)
val _362Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._362sdp)
val _363Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._363sdp)
val _364Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._364sdp)
val _365Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._365sdp)
val _366Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._366sdp)
val _367Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._367sdp)
val _368Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._368sdp)
val _369Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._369sdp)
val _370Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._370sdp)
val _371Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._371sdp)
val _372Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._372sdp)
val _373Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._373sdp)
val _374Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._374sdp)
val _375Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._375sdp)
val _376Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._376sdp)
val _377Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._377sdp)
val _378Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._378sdp)
val _379Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._379sdp)
val _380Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._380sdp)
val _381Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._381sdp)
val _382Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._382sdp)
val _383Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._383sdp)
val _384Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._384sdp)
val _385Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._385sdp)
val _386Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._386sdp)
val _387Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._387sdp)
val _388Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._388sdp)
val _389Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._389sdp)
val _390Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._390sdp)
val _391Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._391sdp)
val _392Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._392sdp)
val _393Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._393sdp)
val _394Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._394sdp)
val _395Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._395sdp)
val _396Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._396sdp)
val _397Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._397sdp)
val _398Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._398sdp)
val _399Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._399sdp)
val _400Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._400sdp)
val _401Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._401sdp)
val _402Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._402sdp)
val _403Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._403sdp)
val _404Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._404sdp)
val _405Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._405sdp)
val _406Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._406sdp)
val _407Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._407sdp)
val _408Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._408sdp)
val _409Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._409sdp)
val _410Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._410sdp)
val _411Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._411sdp)
val _412Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._412sdp)
val _413Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._413sdp)
val _414Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._414sdp)
val _415Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._415sdp)
val _416Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._416sdp)
val _417Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._417sdp)
val _418Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._418sdp)
val _419Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._419sdp)
val _420Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._420sdp)
val _421Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._421sdp)
val _422Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._422sdp)
val _423Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._423sdp)
val _424Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._424sdp)
val _425Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._425sdp)
val _426Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._426sdp)
val _427Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._427sdp)
val _428Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._428sdp)
val _429Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._429sdp)
val _430Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._430sdp)
val _431Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._431sdp)
val _432Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._432sdp)
val _433Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._433sdp)
val _434Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._434sdp)
val _435Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._435sdp)
val _436Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._436sdp)
val _437Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._437sdp)
val _438Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._438sdp)
val _439Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._439sdp)
val _440Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._440sdp)
val _441Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._441sdp)
val _442Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._442sdp)
val _443Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._443sdp)
val _444Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._444sdp)
val _445Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._445sdp)
val _446Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._446sdp)
val _447Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._447sdp)
val _448Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._448sdp)
val _449Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._449sdp)
val _450Sdp: Dp @Composable get() = dimensionResource(id = com.intuit.sdp.R.dimen._450sdp)