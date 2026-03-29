package com.dongbv.alf_03_iptv.ui.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.dongbv.alf_03_iptv.R

val headerScreen = TextStyle(
    color = Color.Black,
    fontFamily = FontFamily(Font(R.font.rb_600)),
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
)