package com.dongbv.alf_03_iptv.style

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.sdp.ssp.android.Sdp

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    stars: Int = 5,
    starSize: Dp = 32.dp,
    ) {
    val starFill: Int = R.drawable.ic_rate_5
    val starDefault: Int = R.drawable.ic_rate_def
    val star5NotFill: Int = R.drawable.ic_rate_def
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..stars) {
            val iconRes = when {
                i <= rating -> starFill
                i == stars -> star5NotFill
                else -> starDefault
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(starSize)
                    .clickable { onRatingChanged(i) }
            )
            SpaceW(8.Sdp)
        }
    }
}
