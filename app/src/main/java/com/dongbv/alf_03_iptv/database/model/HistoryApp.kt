package com.dongbv.alf_03_iptv.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "HistoryApp")
data class HistoryApp(
    @PrimaryKey(autoGenerate = true)
    var hisId: Int = 0,
    var scanTime: Long = System.currentTimeMillis(),
    var scanType: ScanType = ScanType.QRCODE,
    var content: String = "",
    var scanImage: String = "",

    var qrName: String = "",
    var createType: CreateType = CreateType.CREATE,
    var isFavorite: Boolean = false,
    var isDelete: Boolean = false,
) : Parcelable {
}
