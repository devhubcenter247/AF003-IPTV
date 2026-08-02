package com.dongbv.alf_03_iptv.data.local.entity;

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MediaModel(
      val id: Long,
      val name: String,
      val duration: Long,
      val path: String,
      val size: Long
):  Parcelable
