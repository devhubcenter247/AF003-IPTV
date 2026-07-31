package com.dongbv.alf_03_iptv.ui.component

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
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
import com.dongbv.alf_03_iptv.app.rateApp
import com.dongbv.alf_03_iptv.base.AppDialog
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.RatingBar
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.SpaceW
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.black
import com.dongbv.alf_03_iptv.style.gray
import com.dongbv.alf_03_iptv.style.lightGrey
import com.dongbv.alf_03_iptv.style.paddingEnd
import com.dongbv.alf_03_iptv.style.paddingStart
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.paddingVertical
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Preview(device = PIXEL_4_XL)
@Composable
fun PreviewDialog() {
    AppViewTheme {
        Column {
            DialogQuitApp(true,{}) {
            }
//            SpaceH(_18Sdp)
//            DatePickerDialog(
//                onDismiss = { },
//                onDateSelected = {
//                   
//                }
//            )
        }
    }
}



@Preview(device = PIXEL_4_XL)
@Composable
fun PreviewDialog10() {
    AppViewTheme {
        Column {
            DialogNoInternet(true) {
            }
        }
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
                    .background(white)
                    .padding(12.Sdp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    R.drawable.ic_no_wifi_connect,
                    contentDescription = null,
                    modifier = Modifier.size(
                        42.Sdp
                    )
                )
                TextView(
                    stringResource(R.string.no_wifi_connection),
                    font = R.font.ss_600,
                    textAlign = TextAlign.Center,
                    textSize = 14.Ssp,
                    color = black,
                    modifier = Modifier
                        .paddingTop(6.Sdp)
                        .fillMaxWidth()
                )
                TextView(
                    stringResource(R.string.please_check_your_wifi_connection),
                    font = R.font.ss_400,
                    textAlign = TextAlign.Center,
                    textSize = 11.Ssp,
                    color = Color(0xFF6B7785),
                    modifier = Modifier
                        .paddingVertical(10.dp)
                        .fillMaxWidth()
                )
                TextView(
                    stringResource(R.string.try_again),
                    color = white,
                    font = R.font.ss_500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = white, CircleShape
                        )
                        .fillMaxWidth(1f)
                        .padding(12.Sdp)
                ) {
                    tryAgain.invoke()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerButton(
      initial: Instant? = null,
      onPicked: (Instant) -> Unit,
      label: String = "Select date & time",
      use24Hour: Boolean = true,
      minDate: LocalDate? = null,
      maxDate: LocalDate? = null
) {
    val zone = ZoneId.systemDefault()

    val initZdt = remember(initial) {
        initial?.atZone(zone) ?: ZonedDateTime.now(zone)
    }

    var showDate by remember { mutableStateOf(false) }
    var showTime by remember { mutableStateOf(false) }

   
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = initZdt.toInstant().toEpochMilli(),
        yearRange = IntRange(
            minDate?.year ?: 1900,
            maxDate?.year ?: 2100
        )
    )

   
    val timeState = rememberTimePickerState(
        initialHour = initZdt.hour,
        initialMinute = initZdt.minute,
        is24Hour = use24Hour
    )

   
    val previewText = remember(dateState.selectedDateMillis, timeState.hour, timeState.minute) {
        if (dateState.selectedDateMillis != null) {
            val selectedDate = Instant.ofEpochMilli(dateState.selectedDateMillis!!)
                .atZone(zone).toLocalDate()
            val time = LocalTime.of(timeState.hour, timeState.minute)
            DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                .format(LocalDateTime.of(selectedDate, time))
        } else {
            label
        }
    }

   
    Button(
        onClick = { showDate = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = previewText)
    }

   
    if (showDate) {
        DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (dateState.selectedDateMillis != null) {
                            showDate = false
                            showTime = true
                        }
                    }
                ) {
                    Text(stringResource(R.string.next))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDate = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

   
    if (showTime) {
       
        TimePicker(
            state = timeState
        )
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
    var rating by remember { mutableIntStateOf(0) }
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
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(titleRate),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.ss_500)),
                        fontSize = 14.Ssp,
                        color = Color.Black
                    )
                    SpaceH(18.Sdp)
                    Text(
                        text = stringResource(contentRate),
                        fontSize = 12.Ssp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.ss_400)),
                        color = Color.Gray,
                        minLines = 2
                    )

                    Spacer(Modifier.height(16.dp))

                    RatingBar(
                        rating = rating,
                        onRatingChanged = { rating = it }
                    )
                    Spacer(Modifier.height(18.Sdp))
                    TextView(
                        text = if (rating >= 4) stringResource(R.string.rate_on_google_play) else stringResource(
                            R.string.rate_us
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = white, CircleShape
                            )
                            .padding(12.Sdp),
                        color = black,
                    ) {
                        when {
                            rating == 0 -> {
                                Toast.makeText(
                                    activity,
                                    activity?.getString(R.string.please_rate_us),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            rating >= 4 -> {
                                activity?.rateApp {
                                    rateApp()
                                }
                                onDismiss()
                            }
                            else -> {
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

@Composable
fun DialogQuitApp(isShowDialog: Boolean, onDismiss: () -> Unit, onQuit: () -> Unit) {
    if (isShowDialog) {
        AppDialog(onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.Sdp))
                    .background(white)
                    .padding(12.Sdp)
            ) {
                TextView(
                    stringResource(R.string.quit),
                    font = R.font.ss_600,
                    textSize = 14.Ssp,
                    color = black,
                    modifier = Modifier.fillMaxWidth()
                )
                SpaceH(12.Sdp)
                TextView(
                    stringResource(R.string.are_you_sure_want_to_quit_app),
                    font = R.font.ss_500,
                    textSize = 12.Ssp,
                    color = gray,
                    modifier = Modifier.fillMaxWidth()
                )
                SpaceH(12.Sdp)

                Row(Modifier.fillMaxWidth()) {
                    TextView(
                        stringResource(R.string.cancel),
                        color = lightGrey,
                        font = R.font.ss_500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .border(
                                1.dp, lightGrey,
                                CircleShape
                            )
                            .fillMaxWidth()
                            .paddingEnd(8.Sdp)
                            .weight(1f)
                            .padding(12.Sdp)
                    ) {
                        onDismiss.invoke()
                    }
                    SpaceW(16.Sdp)
                    TextView(
                        stringResource(R.string.ok),
                        color = IptvTextPrimary,
                        font = R.font.ss_500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(IptvTextPrimary, CircleShape)
                            .paddingStart(8.Sdp)
                            .weight(1f)
                            .padding(12.Sdp)
                    ) {
                        onQuit.invoke()
                    }
                }
            }
        }
    }
}
