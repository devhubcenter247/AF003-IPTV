package com.dongbv.alf_03_iptv.ui.component

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style._10Sdp
import com.dongbv.alf_03_iptv.style._11Ssp
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._12Ssp
import com.dongbv.alf_03_iptv.style._13Ssp
import com.dongbv.alf_03_iptv.style._14Sdp
import com.dongbv.alf_03_iptv.style._15Ssp
import com.dongbv.alf_03_iptv.style._1Sdp
import com.dongbv.alf_03_iptv.style._24Sdp
import com.dongbv.alf_03_iptv.style._26Sdp
import com.dongbv.alf_03_iptv.style._30Sdp
import com.dongbv.alf_03_iptv.style._3Sdp
import com.dongbv.alf_03_iptv.style._8Sdp
import com.dongbv.alf_03_iptv.style.black
import com.dongbv.alf_03_iptv.style.darkGray
import com.dongbv.alf_03_iptv.style.disabled
import com.dongbv.alf_03_iptv.style.fontFamily
import com.dongbv.alf_03_iptv.style.fontPadding
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.paddingEnd
import com.dongbv.alf_03_iptv.style.paddingStart
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.language.LanguageItem
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.grdStart

@Preview(device = PIXEL_4_XL, showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun PreviewUnit() {
    var value by remember { mutableStateOf("Error dsjksjdksjd") }
    val isError = value.length > 10
    AppViewTheme {
        Column(Modifier.fillMaxWidth()) {
            MonthYearSelect(Modifier.fillMaxWidth()) {}
            SpaceH(16)
            HeaderCreate("Create", R.drawable.ic_app_back)
            ButtonCreate(true)
        }
    }
}

@Composable
fun LanguageItem(
    currentLanguage: LanguageItem? = null,
    item: LanguageItem,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    callback: (LanguageItem) -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .border(
                width = _1Sdp,

                color = if (item != currentLanguage) Color(0xFFF2F2F7).opacity(0.5f) else IptvAccent,
                shape = RoundedCornerShape(_8Sdp)
            )
            .background(white.opacity(0.6f), shape = RoundedCornerShape(_12Sdp))
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(_14Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(item.flagId),
                contentDescription = item.code,
                modifier = Modifier
                    .size(_24Sdp)
                    .border(1.dp, color = gray.opacity(0.2f), shape = CircleShape)
            )
            TextView(
                text = item.name,
                font = R.font.ss_400,
                textSize = _12Ssp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )

            Icon(
                painter = painterResource(if (currentLanguage == item) R.drawable.ic_language_selected else R.drawable.ic_language_unselected),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (currentLanguage == item) IptvAccent else LocalContentColor.current
            )
        }
        Box(
            Modifier
                .matchParentSize()
                .onClick { callback(item) })
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
            .blur(_30Sdp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
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
            .blur(_30Sdp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
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
fun ButtonCreate(
    isEnable: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    TextView(
        text = stringResource(R.string.create),
        modifier = modifier.then(
            Modifier
                .wrapContentHeight()
                .background(
                    if (isEnable) IptvAccent else disabled, CircleShape
                )
                .padding(_12Sdp)
        ),
        textAlign = TextAlign.Center,
        textSize = _12Ssp,
        font = R.font.ss_600,
        color = if (isEnable) IptvAccent else darkGray,
        singleLine = true,
        txtStyle = TextStyle.Default.fontPadding(false)
    ) {
        if (isEnable) {
            onClick.invoke()
        }
    }
}

@Composable
fun HeaderCreate(
    header: String,
    @DrawableRes lefIcon: Int,
    @DrawableRes rightIcon: Int? = null,
    modifier: Modifier = Modifier,
    rightAction: (() -> Unit)? = null,
    leftClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconView(
            item = lefIcon, modifier = Modifier.size(_26Sdp)
        ) {
            leftClick.invoke()
        }

        Text(
            text = header,
            style = headerScreen,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = _15Ssp
        )
        rightIcon?.let {
            IconView(
                item = rightIcon, modifier = Modifier.size(_26Sdp)
            ) {
                rightAction?.invoke()
            }
        } ?: run {
            Box(Modifier.size(_26Sdp))
        }
    }
}

@Composable
fun HeaderCreateResult(
    header: String,
    @DrawableRes lefIcon: Int,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    leftClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconView(
            item = lefIcon, modifier = Modifier.size(_26Sdp)
        ) {
            leftClick.invoke()
        }

        Text(
            text = header,
            style = headerScreen,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = _15Ssp
        )

        IconView(
            item = R.drawable.ic_app_edit, modifier = Modifier
                .paddingEnd(_10Sdp)
                .size(_26Sdp)
        ) {
            onEdit.invoke()
        }

        IconView(
            item = R.drawable.ic_app_delete, modifier = Modifier.size(_26Sdp)
        ) {
            onDelete.invoke()
        }
    }
}

@Composable
fun HeaderScanResult(
    header: String,
    @DrawableRes lefIcon: Int,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier,
    onFavorite: () -> Unit = {},
    onDelete: () -> Unit = {},
    leftClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconView(
            item = lefIcon, modifier = Modifier.size(_26Sdp)
        ) {
            leftClick.invoke()
        }

        Text(
            text = header,
            style = headerScreen,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = _15Ssp
        )

        IconView(
            item = if (isFavorite) R.drawable.ic_app_favorite_minus else R.drawable.ic_app_favorite_add,
            modifier = Modifier
                .paddingEnd(_10Sdp)
                .size(_26Sdp)
        ) {
            onFavorite.invoke()
        }

        IconView(
            item = R.drawable.ic_app_delete, modifier = Modifier.size(_26Sdp)
        ) {
            onDelete.invoke()
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun MonthYearSelect(
    modifier: Modifier = Modifier, isMonth: Boolean = true, isMonthSelect: (Boolean) -> Unit = {}
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(0.45f))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(_8Sdp))
                .padding(vertical = _8Sdp)
                .weight(0.55f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier
                        .paddingEnd(_3Sdp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(if (isMonth) R.drawable.ic_language_selected else R.drawable.ic_language_unselected),
                        contentDescription = null,
                        Modifier
                            .size(24.dp)
                            .onClick {
                                isMonthSelect.invoke(true)
                            })
                    Text(
                        text = stringResource(R.string.month_),
                        fontSize = _11Ssp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = TextStyle.Default.fontFamily(R.font.ss_500),
                        color = black,
                        modifier = Modifier
                            .weight(1f)
                            .paddingStart(_3Sdp),
                    )
                }
                Row(
                    Modifier
                        .paddingStart(_3Sdp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(if (!isMonth) R.drawable.ic_language_selected else R.drawable.ic_language_unselected),
                        contentDescription = null,
                        Modifier
                            .size(24.dp)
                            .onClick {
                                isMonthSelect.invoke(false)
                            })
                    Text(
                        text = stringResource(R.string.year),
                        fontSize = _11Ssp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = TextStyle.Default.fontFamily(R.font.ss_500),
                        color = black,
                        modifier = Modifier
                            .weight(1f)
                            .paddingStart(_3Sdp)
                    )
                }
            }
        }
    }
}