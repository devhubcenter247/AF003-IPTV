package com.dongbv.alf_03_iptv.ui.main.play

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.AppDialog
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.PhonePreview
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvError
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.delay

/**
 * Reports a playback failure briefly, then skips the failed channel.
 *
 * [errorEventId] must change for every new player error. This lets a new error replace an old
 * dialog immediately and prevents a timeout racing with the user's tap from advancing twice.
 */
@Composable
internal fun PlaybackErrorDialog(
    errorEventId: Long?,
    onPlayNext: (errorEventId: Long) -> Unit,
) {
    if (errorEventId == null) return

    var handled by remember(errorEventId) { mutableStateOf(false) }
    val latestOnPlayNext by rememberUpdatedState(onPlayNext)
    val advanceOnce = {
        if (!handled) {
            handled = true
            latestOnPlayNext(errorEventId)
        }
    }

    LaunchedEffect(errorEventId) {
        delay(PLAYBACK_ERROR_AUTO_ADVANCE_MILLIS)
        advanceOnce()
    }

    AppDialog(
        onDismiss = advanceOnce,
        dismissBack = true,
        dismissOutsize = true,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(IptvBgSecondary)
                .border(1.dp, IptvDivider, RoundedCornerShape(16.Sdp))
                .padding(20.Sdp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(48.Sdp)
                    .background(IptvError.copy(alpha = 0.14f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_info),
                    contentDescription = null,
                    tint = IptvError,
                    modifier = Modifier.size(24.Sdp),
                )
            }

            Spacer(Modifier.height(12.Sdp))
            Text(
                text = stringResource(R.string.playback_error_title),
                style = headline700(fontSize = 16.Ssp, color = IptvTextPrimary),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.Sdp))
            Text(
                text = stringResource(R.string.playback_error_message),
                style = headline400(fontSize = 12.Ssp, color = colorSecondary),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(18.Sdp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(IptvAccent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = advanceOnce,
                    )
                    .padding(vertical = 12.Sdp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.next_channel),
                    style = headline600(fontSize = 13.Ssp, color = IptvBgSecondary),
                )
            }
        }
    }
}

internal const val PLAYBACK_ERROR_AUTO_ADVANCE_MILLIS = 2_000L

@PhonePreview
@Composable
private fun PlaybackErrorDialogPreview() {
    AppViewTheme {
        PlaybackErrorDialog(
            errorEventId = 1L,
            onPlayNext = {}
        )
    }
}
