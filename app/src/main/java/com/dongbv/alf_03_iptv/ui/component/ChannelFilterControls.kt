package com.dongbv.alf_03_iptv.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.IptvTextTertiary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

@Composable
fun ChannelSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val active = query.isNotBlank()
    Row(
        modifier = modifier
            .height(42.Sdp)
            .background(white.opacity(7), RoundedCornerShape(10.dp))
            .border(
                1.dp,
                if (active) IptvAccent.copy(alpha = 0.5f) else IptvDivider,
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_app_search),
            contentDescription = null,
            tint = if (active) IptvAccent else IptvTextTertiary,
            modifier = Modifier.size(16.Sdp)
        )
        Spacer(Modifier.width(8.Sdp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            cursorBrush = SolidColor(IptvAccent),
            singleLine = true,
            textStyle = headline400(fontSize = 13.Ssp, color = IptvTextPrimary),
            decorationBox = { inner ->
                Box {
                    if (query.isEmpty()) Text(
                        "Search channels...",
                        style = headline400(fontSize = 13.Ssp, color = IptvTextTertiary)
                    )
                    inner()
                }
            }
        )
        if (active) {
            Spacer(Modifier.width(8.Sdp))
            Box(
                modifier = Modifier
                    .size(22.Sdp)
                    .background(white.opacity(10), CircleShape)
                    .onClick { onQueryChanged("") }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_delete),
                    contentDescription = null,
                    tint = IptvTextSecondary,
                    modifier = Modifier.size(12.Sdp)
                )
            }
        }
    }
}

@Composable
fun LayoutToggle(isGridView: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .background(white.opacity(8), RoundedCornerShape(8.dp))
            .border(1.dp, IptvDivider, RoundedCornerShape(8.dp))
            .padding(2.Sdp)
            .onClick(onClick = onToggle)
    ) {
        Row {
            ToggleIconBox(selected = isGridView) {
                Icon(Icons.Rounded.GridView, null, modifier = Modifier.size(18.Sdp))
            }
            ToggleIconBox(selected = !isGridView) {
                Icon(Icons.AutoMirrored.Rounded.ViewList, null, modifier = Modifier.size(18.Sdp))
            }
        }
    }
}

@Composable
private fun ToggleIconBox(selected: Boolean, icon: @Composable () -> Unit) {
    val bg by animateColorAsState(
        if (selected) IptvAccent else Color.Transparent,
        tween(200),
        label = "toggleBg"
    )
    val tint by animateColorAsState(
        if (selected) white else IptvTextSecondary,
        tween(200),
        label = "toggleTint"
    )
    Box(
        modifier = Modifier
            .size(28.Sdp)
            .background(bg, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides tint) { icon() }
    }
}
