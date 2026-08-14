package com.dongbv.alf_03_iptv.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.transparent
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.ui.language.getLanguageArray
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextTertiary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

@Composable
fun LanguageViewItem(
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
                brush = Brush.horizontalGradient(
                    if (item != currentLanguage) listOf(
                        white.opacity(0.05f),
                        white.opacity(0.05f)
                    ) else listOf(
                        grdStart, grdEnd
                    )
                ),
                shape = RoundedCornerShape(16.Sdp)
            )
            .onClick {
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
            modifier = Modifier.size(20.Sdp),
            tint = Color.Unspecified
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

@Composable
fun ChannelSearchBar(
    query: String,
    isTablet: Boolean = false,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val active = query.isNotBlank()
    Row(
        modifier = modifier
            .height(if (isTablet) 34.Sdp else 42.Sdp)
            .background(white.opacity(7), RoundedCornerShape(12.Sdp))
            .border(
                1.dp,
                if (active) IptvAccent.copy(alpha = 0.5f) else IptvDivider,
                RoundedCornerShape(12.Sdp)
            )
            .padding(horizontal =if(isTablet) 8.Sdp else 12.Sdp,),
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
            maxLines = 1,
            textStyle = headline400(fontSize = 12.Ssp, color = IptvTextPrimary),
            decorationBox = { inner ->
                Box {
                    if (query.isEmpty()) Text(
                        stringResource(R.string.search_channels),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = headline400(fontSize = 12.Ssp, color = IptvTextTertiary)
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
                    painter = painterResource(R.drawable.ic_app_clear),
                    contentDescription = null,
                    tint = colorSecondary,
                    modifier = Modifier.size(12.Sdp)
                )
            }
        }
    }
}

@Composable
fun LayoutToggle(isGridView: Boolean, modifier: Modifier = Modifier, onToggle: () -> Unit) {
    Box(
        modifier = modifier
            .background(white.opacity(8), CircleShape)
            .border(1.dp, IptvDivider, CircleShape)
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
    val tint by animateColorAsState(
        if (selected) white else colorSecondary,
        tween(200),
        label = "toggleTint"
    )
    Box(
        modifier = Modifier
            .size(28.Sdp)
            .backgroundState(enabled = selected, disableColors = listOf(transparent, transparent)),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides tint) { icon() }
    }
}

@Composable
fun ChannelListItem(
    channel: ChannelEntity,
    isTablet: Boolean = false,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .paddingHorizontal(12.Sdp)
            .paddingTop(8.Sdp)
            .onClick(onClick = onClick)
            .fillMaxWidth()
            .background(
                if (isSelected) IptvAccent.copy(alpha = 0.12f) else white.opacity(5),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (isSelected) IptvAccent.copy(alpha = 0.3f) else IptvDivider,
                RoundedCornerShape(12.dp)
            )
            .padding(8.Sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.Sdp)
    ) {

        Box(
            modifier = Modifier
                .size(if (isTablet) 37.Sdp else 48.Sdp)
                .background(white.opacity(10), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            var isLogoLoadError by remember(channel.logo) { mutableStateOf(false) }
            if (!channel.logo.isNullOrBlank() && !isLogoLoadError) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    onState = { state -> isLogoLoadError = state is AsyncImagePainter.State.Error },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_channel),
                    contentDescription = null,
                    tint = colorSecondary,
                    modifier = Modifier.size(24.Sdp)
                )
            }
        }

        Column(Modifier.weight(1f)) {
            Text(
                text = channel.name,
                style = headline600(
                    fontSize = 12.Ssp,
                    color = if (isSelected) IptvAccent else IptvTextPrimary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!channel.groupName.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(IptvAccent.copy(alpha = 0.15f), CircleShape)
                        .padding(horizontal = 4.Sdp, vertical = 2.Sdp)
                ) {
                    Text(
                        text = channel.groupName,
                        style = headline400(fontSize = 9.Ssp, color = IptvAccent),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (isSelected) {
            Box(
                Modifier
                    .size(8.Sdp)
                    .background(IptvAccent, CircleShape)
            )
        }
    }
}

@ResponsivePreview
@Composable
private fun LanguageViewItemPreview() {
    val languages = getLanguageArray()
    AppViewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageViewItem(
                currentLanguage = languages[0],
                item = languages[0],
                callback = {}
            )
            LanguageViewItem(
                currentLanguage = languages[0],
                item = languages[1],
                callback = {}
            )
        }
    }
}

@ResponsivePreview
@Composable
private fun ChannelSearchBarPreview() {
    AppViewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChannelSearchBar(query = "", onQueryChanged = {})
            ChannelSearchBar(query = "BBC News", onQueryChanged = {})
        }
    }
}

@ResponsivePreview
@Composable
private fun LayoutTogglePreview() {
    AppViewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LayoutToggle(isGridView = true, onToggle = {})
            LayoutToggle(isGridView = false, onToggle = {})
        }
    }
}

@ResponsivePreview
@Composable
private fun ChannelListItemPreview() {
    val channels = ChannelEntity.fakeList()
    AppViewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChannelListItem(channel = channels[0], isSelected = false, onClick = {})
            ChannelListItem(channel = channels[0], isSelected = true, onClick = {})
            ChannelListItem(
                channel = channels[0],
                isTablet = true,
                isSelected = false,
                onClick = {})
        }
    }
}
