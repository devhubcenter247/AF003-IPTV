package com.dongbv.alf_03_iptv.base

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dongbv.alf_03_iptv.style.widthPercentDp
import com.dongbv.alf_03_iptv.style._12Sdp
import com.dongbv.alf_03_iptv.style._8Sdp

@Composable
fun AppDialog(
    onDismiss: () -> Unit, dialog: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(_12Sdp),
            color = Color.White,
            tonalElevation = _8Sdp,
            modifier = Modifier.padding(horizontal = widthPercentDp(5f))
        ) {
            dialog.invoke()
        }
    }
}
