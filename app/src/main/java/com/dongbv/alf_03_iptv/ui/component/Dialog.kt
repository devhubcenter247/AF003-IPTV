package com.dongbv.alf_03_iptv.ui.component

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.rateApp
import com.dongbv.alf_03_iptv.app.sendFeedBack
import com.dongbv.alf_03_iptv.base.AppDialog
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.RatingBar
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.gradientContent
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.paddingVertical
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Preview(device = PIXEL_4_XL)
@Composable
fun PreviewDialog() {
    AppViewTheme {
        DialogQuitApp(true, {}) {
        }
    }
}


@Preview(device = PIXEL_4_XL)
@Composable
fun PreviewDialog10() {
    AppViewTheme {
        DialogNoInternet(true) {
        }
    }
}

@Preview(device = PIXEL_4_XL)
@Composable
fun PreviewDialog2() {
    AppViewTheme {
        DialogQuitApp(isShowDialog = true) {
        }
    }
}

@Preview(device = PIXEL_4_XL)
@Composable
fun PreviewRatingDialog() {
    AppViewTheme {
        RatingDialog(isShowRate = true, rateApp = {}, sendEmailFeedback = {})
    }
}

@Composable
fun SleepTimerDialog(
    isShow: Boolean = false,
    isActive: Boolean = false,
    remainingMs: Long = 0L,
    onSetTimer: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    onCancelTimer: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isShow) return
    AppDialog(onDismiss = {
        onDismiss()
    }) {
        Column(
            modifier = Modifier
                .backgroundResource(
                    R.drawable.img_bg_app,
                    contentScale = ContentScale.Crop
                )
                .border(1.dp, IptvDivider, RoundedCornerShape(16.Sdp))
                .padding(20.Sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.sleep_timer),
                style = headline700(fontSize = 16.Ssp, color = IptvTextPrimary)
            )
            Spacer(Modifier.height(18.Sdp))

            if (isActive) {
                val totalSeconds = remainingMs / 1000
                val h = totalSeconds / 3600
                val m = (totalSeconds % 3600) / 60
                val s = totalSeconds % 60
                Text(
                    text = stringResource(R.string.sleep_timer_remaining, h, m, s),
                    style = headline600(fontSize = 20.Ssp, color = IptvAccent),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.Sdp))
                DialogActionButton(

                    text = stringResource(R.string.cancel),
                    modifier = Modifier.fillMaxWidth(),
                    isPrimary = true,
                    onClick = onCancelTimer
                )
            } else {
                val defaultTarget = remember {
                    Calendar.getInstance().apply { add(Calendar.MINUTE, 30) }
                }
                var hour by remember { mutableIntStateOf(defaultTarget.get(Calendar.HOUR_OF_DAY)) }
                var minute by remember {
                    mutableIntStateOf((defaultTarget.get(Calendar.MINUTE) / 5) * 5)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.Sdp)
                ) {
                    TimeStepper(
                        label = stringResource(R.string.sleep_timer_hour_label),
                        value = hour,
                        onIncrement = { hour = (hour + 1) % 24 },
                        onDecrement = { hour = (hour + 23) % 24 }
                    )
                    Text(
                        ":",
                        style = headline700(fontSize = 26.Ssp, color = IptvTextPrimary)
                    )
                    TimeStepper(
                        label = stringResource(R.string.sleep_timer_minute_label),
                        value = minute,
                        onIncrement = { minute = (minute + 5) % 60 },
                        onDecrement = { minute = (minute + 55) % 60 }
                    )
                }

                Spacer(Modifier.height(20.Sdp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.Sdp)
                ) {
                    DialogActionButton(
                        text = stringResource(R.string.cancel),
                        modifier = Modifier.weight(1f),
                        isPrimary = false,
                        onClick = onDismiss
                    )
                    DialogActionButton(
                        text = stringResource(R.string.set),
                        modifier = Modifier.weight(1f),
                        isPrimary = true,
                        onClick = { onSetTimer(hour, minute) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeStepper(
    label: String,
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepperButton("+", onIncrement)
        Spacer(Modifier.height(6.Sdp))
        Text(
            text = "%02d".format(value),
            style = headline700(fontSize = 28.Ssp, color = IptvTextPrimary)
        )
        Spacer(Modifier.height(6.Sdp))
        StepperButton("−", onDecrement)
        Spacer(Modifier.height(4.Sdp))
        Text(label, style = headline400(fontSize = 10.Ssp, color = colorSecondary))
    }
}

@Composable
private fun StepperButton(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.Sdp)
            .clip(CircleShape)
            .background(white.opacity(8))
            .border(1.dp, IptvDivider, CircleShape)
            .onClick(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, style = headline600(fontSize = 16.Ssp, color = IptvTextPrimary))
    }
}

@Composable
private fun DialogActionButton(
    text: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .backgroundState(enabled = isPrimary)
            .border(1.dp, if (isPrimary) IptvAccent else IptvDivider, RoundedCornerShape(50))
            .onClick(onClick = onClick)
            .padding(vertical = 12.Sdp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = headline600(fontSize = 13.Ssp, color = if (isPrimary) white else colorSecondary)
        )
    }
}

@Preview(device = PIXEL_4_XL)
@Composable
private fun SleepTimerDialogSetPreview() {
    AppViewTheme {
        SleepTimerDialog(
            isShow = true,
            isActive = false,
            remainingMs = 0,
            onSetTimer = { _, _ -> },
            onCancelTimer = {},
            onDismiss = {}
        )
    }
}

@Preview(device = PIXEL_4_XL)
@Composable
private fun SleepTimerDialogActivePreview() {
    AppViewTheme {
        SleepTimerDialog(
            isShow = true,
            isActive = true,
            remainingMs = 45 * 60 * 1000L + 30_000L,
            onSetTimer = { _, _ -> },
            onCancelTimer = {},
            onDismiss = {}
        )
    }
}


@Composable
fun DialogNoInternet(
    isShowDialog: Boolean,
    onDismiss: () -> Unit = {},
    tryAgain: () -> Unit = {}
) {
    if (isShowDialog) {
        AppDialog(onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.Sdp))
                    .backgroundResource(R.drawable.img_bg_app)
                    .padding(12.Sdp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    R.drawable.ic_no_wifi_connect,
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.Sdp)
                        .gradientContent(listOf(grdStart, grdEnd))
                )
                TextView(
                    stringResource(R.string.no_wifi_connection),
                    font = R.font.ss_600,
                    textAlign = TextAlign.Center,
                    textSize = 14.Ssp,
                    color = IptvTextPrimary,
                    modifier = Modifier
                        .paddingTop(6.Sdp)
                        .fillMaxWidth()
                )
                TextView(
                    stringResource(R.string.please_check_your_wifi_connection),
                    font = R.font.ss_400,
                    textAlign = TextAlign.Center,
                    textSize = 11.Ssp,
                    color = colorSecondary,
                    modifier = Modifier
                        .paddingVertical(10.dp)
                        .fillMaxWidth()
                )
                DialogActionButton(
                    text = stringResource(R.string.try_again),
                    modifier = Modifier.fillMaxWidth(),
                    isPrimary = true,
                    onClick = tryAgain
                )
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun RatingDialog(
    isShowRate: Boolean = false,
    onDismiss: () -> Unit = {},
    rateApp: () -> Unit,
    sendEmailFeedback: () -> Unit
) {
    val activity = LocalActivity.current
    val isPreview = LocalInspectionMode.current
    var rating by remember(isShowRate) { mutableIntStateOf(0) }
    var reason by remember(isShowRate) { mutableStateOf("") }
    var reasonError by remember(isShowRate) { mutableStateOf(false) }
    var isSubmitting by remember(isShowRate) { mutableStateOf(false) }
    val titleRate by remember(rating) {
        mutableIntStateOf(
            when (rating) {
                1, 2 -> R.string.rating_title_1_2
                3 -> R.string.rating_title_3
                4 -> R.string.rating_title_4
                5 -> R.string.rating_title_5
                else -> R.string.rating_title_0
            }
        )
    }
    val contentRate by remember(rating) {
        mutableIntStateOf(
            when (rating) {
                1, 2 -> R.string.rating_description_1_2
                3 -> R.string.rating_description_3
                4 -> R.string.rating_description_4
                5 -> R.string.rating_description_5
                else -> R.string.rating_description_0
            }
        )
    }
    if (isShowRate) {
        val content = @Composable {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .backgroundResource(R.drawable.img_bg_app)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(titleRate),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.ss_500)),
                        fontSize = 14.Ssp,
                        color = IptvTextPrimary
                    )
                    SpaceH(18.Sdp)
                    Text(
                        text = stringResource(contentRate),
                        fontSize = 12.Ssp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.ss_400)),
                        color = colorSecondary,
                        minLines = 2
                    )

                    Spacer(Modifier.height(16.dp))

                    RatingBar(
                        rating = rating,
                        onRatingChanged = {
                            if (!isSubmitting) {
                                rating = it
                                reasonError = false
                            }
                        }
                    )
                    if (rating in 1..3) {
                        SpaceH(12.Sdp)
                        OutlinedTextField(
                            value = reason,
                            onValueChange = {
                                reason = it
                                reasonError = false
                            },
                            label = { Text(stringResource(R.string.rating_feedback_reason)) },
                            placeholder = { Text(stringResource(R.string.rating_feedback_hint)) },
                            isError = reasonError,
                            supportingText = if (reasonError) {
                                { Text(stringResource(R.string.rating_feedback_reason_required)) }
                            } else null,
                            minLines = 2,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = IptvTextPrimary,
                                unfocusedTextColor = IptvTextPrimary,
                                focusedBorderColor = IptvAccent,
                                unfocusedBorderColor = colorSecondary,
                                focusedLabelColor = IptvAccent,
                                unfocusedLabelColor = colorSecondary,
                                cursorColor = IptvAccent,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Spacer(Modifier.height(18.Sdp))
                    DialogActionButton(
                        text = stringResource(when (rating) {
                            4, 5 -> R.string.rate_on_google_play
                            1, 2, 3 -> R.string.rating_send_feedback
                            else -> R.string.rate_us
                        }),
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = true
                    ) {
                        if (isPreview || isSubmitting) return@DialogActionButton
                        when {
                            rating == 0 -> {
                                Toast.makeText(
                                    activity,
                                    activity?.getString(R.string.rating_select_stars),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            rating >= 4 -> {
                                val owner = activity ?: return@DialogActionButton
                                isSubmitting = true
                                AppPreferences.isRatingApp.value = true
                                owner.rateApp {
                                    rateApp()
                                }
                                onDismiss()
                            }

                            else -> {
                                if (reason.isBlank()) {
                                    reasonError = true
                                } else {
                                    val owner = activity ?: return@DialogActionButton
                                    AppPreferences.isRatingApp.value = true
                                    if (owner.sendFeedBack(rating, reason)) {
                                        isSubmitting = true
                                        sendEmailFeedback()
                                        onDismiss()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        } else {
            Dialog(
                onDismissRequest = { onDismiss() },
                properties = DialogProperties(
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false,
                    dismissOnBackPress = true
                )
            ) {
                content()
            }
        }
    }
}

@Composable
fun DialogQuitApp(isShowDialog: Boolean, onDismiss: () -> Unit = {}, onQuit: () -> Unit = {}) {
    if (isShowDialog) {
        AppDialog(onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.Sdp))
                    .backgroundResource(R.drawable.img_bg_app)
                    .padding(12.Sdp)
            ) {
                TextView(
                    stringResource(R.string.quit),
                    font = R.font.ss_600,
                    textSize = 14.Ssp,
                    color = IptvTextPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                SpaceH(12.Sdp)
                TextView(
                    stringResource(R.string.are_you_sure_want_to_quit_app),
                    font = R.font.ss_500,
                    textSize = 12.Ssp,
                    color = colorSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                SpaceH(12.Sdp)

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.Sdp)
                ) {
                    DialogActionButton(
                        text = stringResource(R.string.cancel),
                        modifier = Modifier.weight(1f),
                        isPrimary = true,
                        onClick = onDismiss
                    )
                    DialogActionButton(
                        text = stringResource(R.string.ok),
                        modifier = Modifier.weight(1f),
                        isPrimary = false,
                        onClick = onQuit
                    )
                }
            }
        }
    }
}


@Composable
fun CopyrightNoticeDialog(onAccept: () -> Unit, onCancel: () -> Unit) {
    AppDialog(onDismiss = onCancel, dismissOutsize = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.Sdp))
                .backgroundResource(R.drawable.img_bg_app)
                .border(1.dp, IptvDivider, RoundedCornerShape(12.Sdp))
                .padding(20.Sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.ic_app_info,
                contentDescription = null,
                modifier = Modifier
                    .size(40.Sdp)
                    .gradientContent(listOf(grdStart, grdEnd))
            )
            SpaceH(12.Sdp)
            Text(
                text = stringResource(R.string.copyright_notice_title),
                style = headline700(fontSize = 16.Ssp, color = IptvTextPrimary),
                textAlign = TextAlign.Center
            )
            SpaceH(12.Sdp)
            Text(
                text = stringResource(R.string.copyright_notice_message),
                style = headline400(fontSize = 12.Ssp, color = colorSecondary),
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
            SpaceH(20.Sdp)
            DialogActionButton(
                text = stringResource(R.string.copyright_notice_accept),
                isPrimary = true,
                modifier = Modifier.fillMaxWidth(),
                onClick = onAccept
            )
            SpaceH(10.Sdp)
            DialogActionButton(
                text = stringResource(R.string.cancel),
                isPrimary = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = onCancel
            )
        }
    }
}

@Preview(device = PIXEL_4_XL)
@Composable
private fun CopyrightNoticeDialogPreview() {
    AppViewTheme {
        CopyrightNoticeDialog(onAccept = {}, onCancel = {})
    }
}
