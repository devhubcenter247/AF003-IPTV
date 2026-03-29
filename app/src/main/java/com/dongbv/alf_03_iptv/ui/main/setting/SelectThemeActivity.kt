package com.dongbv.alf_03_iptv.ui.main.setting

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.app.toastShort
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.base.InnerPadding
import com.dongbv.alf_03_iptv.style.IconView
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.ViewParentContent
import com.dongbv.alf_03_iptv.style._11Ssp
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._12Ssp
import com.dongbv.alf_03_iptv.style._16Sdp
import com.dongbv.alf_03_iptv.style._16Ssp
import com.dongbv.alf_03_iptv.style._2Sdp
import com.dongbv.alf_03_iptv.style._34Sdp
import com.dongbv.alf_03_iptv.style._37Sdp
import com.dongbv.alf_03_iptv.style._38Sdp
import com.dongbv.alf_03_iptv.style._4Sdp
import com.dongbv.alf_03_iptv.style._8Sdp
import com.dongbv.alf_03_iptv.style.black
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.ColorTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SelectThemeActivity : BaseActivity() {
    override fun initialize()  = Unit

    @Composable
    override fun BoxScope.ContentView(
          innerPadding: InnerPadding,
          context: Context
    ) {
        val colorSelect by PreferenceData.themeApp.flow.collectAsStateWithLifecycle(ColorTheme.BLUE)
        val scope = rememberCoroutineScope()
        ViewParentContent(
            onBack = { finish() },
            modifier = Modifier
                .fillMaxSize()
                .background(colorSelect.bgColor)
                .padding(top = innerPadding.top)
        ) {
            SelectThemeScreen(
                initialTheme = colorSelect,
                onBack = { finish() },
                onSave = { chosen ->
                    PreferenceData.themeApp.value = chosen
                    toastShort(getString(R.string.applying_theme_success))
                    scope.launch(Dispatchers.Main) {
                        delay(1200L)
                        finish()
                    }
                }
            )
        }
    }
}

@Composable
fun SelectThemeScreen(
      initialTheme: ColorTheme,
      onBack: () -> Unit,
      onSave: (ColorTheme) -> Unit,
) {
    var colorSelect by remember(initialTheme) { mutableStateOf(initialTheme) }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(onBack)

        SpaceH(24)

        Column(
            Modifier
                .padding(_16Sdp)
                .fillMaxWidth()
                .background(color = white, shape = RoundedCornerShape(20.dp))
                .padding(_16Sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemeView(colorSelect)
            SpaceH(16)
            DividerThin()
            SpaceH(16)

            TextView(
                R.string.highlight_color,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                font = R.font.rb_500,
                textSize = _12Ssp,
                color = black
            )

            SpaceH(16)

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ColorTheme.entries.forEach { theme ->
                    ThemeOption(
                        theme = theme,
                        selected = (theme == colorSelect),
                        onSelect = { colorSelect = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        SpaceH(24)

        SaveButton(
            colorSelect = colorSelect,
            onClick = { onSave(colorSelect) }
        )
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .paddingHorizontal(_8Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconView(
            item = R.drawable.ic_app_back,
            modifier = Modifier.size(_34Sdp),
            icSize = 28,
            tint = black
        ) { onBack() }

        TextView(
            text = R.string.color_theme,
            font = R.font.rb_700,
            color = black,
            textSize = _16Ssp
        )
    }
}

@Composable
private fun DividerThin() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .height(1.5.dp)
            .background(color = Color(0xFFD9D9D9))
    )
}

@Composable
private fun ThemeOption(
      theme: ColorTheme,
      selected: Boolean,
      onSelect: (ColorTheme) -> Unit,
      modifier: Modifier = Modifier
) {
    val themeName by remember {
        mutableIntStateOf(
            when (theme) {
                ColorTheme.PINK -> R.string.theme_2
                ColorTheme.GREEN -> R.string.theme_3
                ColorTheme.DENIM -> R.string.theme_4
                else -> R.string.theme_1
            }
        )
    }
    Column(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorDot(
            selected = selected,
            color = theme.buttonColor,
            onClick = { onSelect(theme) }
        )
        Spacer(modifier = Modifier.height(_4Sdp))
        if (selected) {
            TextView(
                text = stringResource(themeName),
                font = R.font.rb_500,
                color = theme.buttonColor,
                textSize = _11Ssp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun ColorDot(
      selected: Boolean,
      color: Color,
      onClick: () -> Unit,
      modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(_37Sdp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(color, CircleShape)
            .padding(_2Sdp)
    ) {
        if (selected) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(_2Sdp, white, CircleShape)
                    .clickable(onClick = onClick)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
private fun SaveButton(
      colorSelect: ColorTheme,
      onClick: () -> Unit
) {
    TextView(
        text = stringResource(R.string.save),
        font = Font(R.font.rb_600),
        textAlign = TextAlign.Center,
        color = colorSelect.colorText,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(_38Sdp)
            .clip(RoundedCornerShape(50))
            .background(colorSelect.buttonColor),
        contentAlignment = Alignment.Center
    ) {
        onClick.invoke()
    }
}

@Composable
private fun ThemeView(colorSelect: ColorTheme) {
    val outerShape = remember { RoundedCornerShape(16.dp) }
    val cardShape = remember { RoundedCornerShape(12.dp) }
    val dividerColor = remember { Color(0xFFD9D9D9) }
    val boxGray = remember { Color(0xFFB8B8BE) }

    Box(
        Modifier
            .size(100.dp, 175.dp)
            .padding(2.dp)
            .border(2.5.dp, colorSelect.buttonColor, outerShape)
    ) {
        Column(
            Modifier
                .padding(4.dp)
                .fillMaxSize()
                .background(white, cardShape)
                .border(1.5.dp, dividerColor, cardShape),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color(0xFFF2F2F7),
                        shape = cardShape.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .width(45.dp)
                        .height(10.dp)
                        .background(boxGray, RoundedCornerShape(3.dp))
                )
                SpaceH(5.dp)
                Box(
                    Modifier
                        .width(30.dp)
                        .height(10.dp)
                        .background(boxGray, RoundedCornerShape(5.dp))
                )
            }

            SpaceH(12)

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                repeat(5) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(boxGray, CircleShape)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Box(
                Modifier
                    .width(50.dp)
                    .height(10.dp)
                    .background(colorSelect.buttonColor, RoundedCornerShape(3.dp))
            )

            SpaceH(18)
        }
    }
}

@Preview(showBackground = true, name = "SelectTheme - BLUE")
@Composable
private fun SelectThemePreviewBlue() {
    Box(
        Modifier
            .fillMaxSize()
            .background(ColorTheme.BLUE.bgColor)
            .padding(16.dp)
    ) {
        SelectThemeScreen(
            initialTheme = ColorTheme.BLUE,
            onBack = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "SelectTheme - PINK")
@Composable
private fun SelectThemePreviewPink() {
    Box(
        Modifier
            .fillMaxSize()
            .background(ColorTheme.PINK.bgColor)
    ) {
        SelectThemeScreen(
            initialTheme = ColorTheme.PINK,
            onBack = {},
            onSave = {}
        )
    }
}
