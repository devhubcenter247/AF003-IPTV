package com.dongbv.alf_03_iptv.data.local.entity

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dongbv.alf_03_iptv.R
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Entity representing a playlist in the IPTV app.
 * A playlist can be loaded from URL, local file, or gallery.
 */
@Stable
@Parcelize
@Entity(tableName = "playlists")
data class PlaylistEntity(
      @PrimaryKey(autoGenerate = true)
      val id: Long = 0,

      @ColumnInfo(name = "name")
      val name: String,

      @ColumnInfo(name = "source")
      val source: String, // URL or file path

      @ColumnInfo(name = "source_type")
      val sourceType: Int = SourceType.URL.value,

      @ColumnInfo(name = "icon")
      val icon: String? = null, // Custom icon path or URL

      @ColumnInfo(name = "channel_count")
      val channelCount: Int = 0,

      @ColumnInfo(name = "last_updated")
      val lastUpdated: Long = System.currentTimeMillis(),

      @ColumnInfo(name = "created_at")
      val createdAt: Long = System.currentTimeMillis(),

      @ColumnInfo(name = "is_active")
      val isActive: Boolean = true,
      @ColumnInfo(name = "isFavorite")
      val isFavorite: Boolean = false,
      @ColumnInfo(name = "last_watched")
      val lastWatched: Long? = null
) : Parcelable {
    fun getSourceTypeEnum(): SourceType = SourceType.fromValue(sourceType)

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(createdAt))
    }

    fun getFormattedLastUpdated(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(lastUpdated))
    }

    fun getChannelCountText(context: Context): String {
        return when {
            channelCount == 0 -> context.getString(R.string.no_channel)
            channelCount == 1 -> "1 ${context.getString(R.string.videos).lowercase()}"
            else -> "$channelCount ${context.getString(R.string.videos).lowercase()}"
        }
    }
}
