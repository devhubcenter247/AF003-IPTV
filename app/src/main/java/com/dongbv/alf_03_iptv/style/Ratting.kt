package com.dongbv.alf_03_iptv.style

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dongbv.alf_03_iptv.R

val Icons.Outlined.StarOutline: ImageVector
    get() {
        if (_star != null) {
            return _star!!
        }
        _star = materialIcon(name = "Outlined.Star") {
            materialPath {
                moveTo(22.0f, 9.24f)
                lineToRelative(-7.19f, -0.62f)
                lineTo(12.0f, 2.0f)
                lineTo(9.19f, 8.62f)
                lineTo(2.0f, 9.24f)
                lineToRelative(5.46f, 4.73f)
                lineToRelative(-1.63f, 7.03f)
                lineTo(12.0f, 17.77f)
                lineToRelative(6.17f, 3.23f)
                lineToRelative(-1.64f, -7.03f)
                lineTo(22.0f, 9.24f)
                close()
                moveTo(12.0f, 15.4f)
                lineToRelative(-3.76f, 1.97f)
                lineToRelative(1.0f, -4.28f)
                lineToRelative(-3.32f, -2.88f)
                lineToRelative(4.38f, -0.38f)
                lineTo(12.0f, 6.1f)
                lineToRelative(1.71f, 4.73f)
                lineToRelative(4.38f, 0.38f)
                lineToRelative(-3.32f, 2.88f)
                lineToRelative(1.0f, 4.28f)
                lineTo(12.0f, 15.4f)
                close()
            }
        }
        return _star!!
    }
private var _star: ImageVector? = null
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    stars: Int = 5,
    starSize: Dp = _28Sdp,
    ) {
    val starFill: Int = R.drawable.ic_rate_fill
    val starDefault: Int = R.drawable.ic_rate_def
    val star5NotFill: Int = R.drawable.ic_rate_5
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..stars) {
            val iconRes = when {
                i <= rating -> starFill                        // Filled up to rating
                i == stars -> star5NotFill                    // Show gold 5th star if not filled
                else -> starDefault                           // Default outlined star
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(starSize)
                    .clickable { onRatingChanged(i) }
            )
            SpaceW(_4Sdp)
        }
    }
}
