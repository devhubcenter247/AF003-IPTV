package com.dongbv.alf_03_iptv.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

@Composable
fun LanguageItem(
    currentLanguage: LanguageItem? = null,
    item: LanguageItem,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    callback: (LanguageItem) -> Unit
) {
        Row(
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.Sdp,
                    color = if (item != currentLanguage) Color(0xFFF2F2F7).copy(alpha = 0.05f) else IptvAccent,
                    shape = RoundedCornerShape(16.Sdp)
                ).onClick {
                    callback.invoke(item)
                }
                .background(white.copy(alpha = 0.05f), shape = RoundedCornerShape(16.Sdp))
                .padding(14.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(item.flagId),
                contentDescription = item.code,
                modifier = Modifier
                    .size(24.Sdp)
                    .border(1.dp, color = gray.opacity(0.2f), shape = CircleShape)
            )
            TextView(
                text = item.name,
                font = R.font.ss_400,
                textSize = 12.Ssp,
                color = white,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(if (currentLanguage == item) R.drawable.ic_language_selected else R.drawable.ic_language_unselected),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if(item.code == currentLanguage?.code) IptvAccent else IptvTextSecondary
            )
    }
}

@Composable
fun BoxScope.BlurView(
    color: Color = Color.White.opacity(50),
    shape: Shape = RoundedCornerShape(50)
) {
    Box(
        Modifier
            .matchParentSize()
            .blur(30.Sdp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(
                    color = color, shape = shape
                )
        )
    }
}

@Composable
fun BoxScope.BlurView(
    brush: Brush = Brush.linearGradient(),
    shape: Shape = RoundedCornerShape(50)
) {
    Box(
        Modifier
            .matchParentSize()
            .blur(30.Sdp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(
                    brush = brush, shape = shape
                )
        )
    }
}

