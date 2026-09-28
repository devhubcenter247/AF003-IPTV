package com.dongbv.alf_03_iptv.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.net.toUri
import com.dongbv.alf_03_iptv.R
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory

object Aso {
    const val POLICY_LINK =
        "https://docs.google.com/document/d/e/2PACX-1vTtE7wku1uWM9XM-FQsxZzYRFH6UIDjFMrE_EWfO0KkWuR6PQWtr4H-ZFnKo7_xMQ/pub"

    const val TEAM_SERVICE =
        "https://docs.google.com/document/d/e/2PACX-1vQfRYn2prW_SefAtJXzzjFkV_EeJI5YE-KM3EHoMJI5kHR-7AvJgrF5QrKzPML_Iw/pub"

    const val MAIL_SUPPORT = "dong2k2.dev@gmail.com"
}

fun Context.openUrl(url: String) {
    runCatching {
        Intent(Intent.ACTION_VIEW, url.toUri()).also {
            this.startActivity(it)
        }
    }
}

fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastTop(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
        show()
    }
}


fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.rateApp(onFinish: () -> Unit) {
    val manager = ReviewManagerFactory.create(this)
    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { task: Task<ReviewInfo> ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            val flow =
                manager.launchReviewFlow(this, reviewInfo)
            flow.addOnCompleteListener {
                Log.d("RateApp", "Rate complete")
                onFinish()
            }
        } else {
            Log.e("RateApp", "error: " + task.exception.toString())
            openStoreRatingPage()
            onFinish()
        }
    }
}

private fun Activity.openStoreRatingPage() {
    val marketIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        "https://play.google.com/store/apps/details?id=$packageName".toUri()
    )
    runCatching { startActivity(marketIntent) }
        .recoverCatching { startActivity(webIntent) }
}


fun Activity.sendFeedBack(rating: Int, reason: String): Boolean {
    val subject = getString(R.string.rating_feedback_subject, getString(R.string.app_name))
    val body = getString(R.string.rating_feedback_body, rating, reason.trim())
    val emailUri = "mailto:${Aso.MAIL_SUPPORT}?subject=${Uri.encode(subject)}&body=${Uri.encode(body)}"
    val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri.toUri()).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(Aso.MAIL_SUPPORT))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    return try {
        startActivity(emailIntent)
        true
    } catch (_: ActivityNotFoundException) {
        toastShort(getString(R.string.rating_no_email_app))
        false
    }
}


fun Context.shareApp() {
    val applicationID = this.packageName
    val appPlayStoreUrl = "https://play.google.com/store/apps/details?id=$applicationID"
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name))
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,
        getString(R.string.check_out_this_amazing_app) + appPlayStoreUrl
    )
    val chooserIntent = Intent.createChooser(shareIntent, getString(R.string.share_via))
    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(chooserIntent)
}

fun Context.shareValue(value: String) {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name))
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,
        value
    )
    val chooserIntent = Intent.createChooser(shareIntent, getString(R.string.share_via))
    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(chooserIntent)
}
