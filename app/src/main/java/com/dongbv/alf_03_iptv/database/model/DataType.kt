package com.dongbv.alf_03_iptv.database.model

enum class CreateType {
    CREATE, SCAN
}

enum class ScanType {
    QRCODE, BARCODE, OCR
}

object QrSaveType {
    const val QR_SCAN = 0
    const val QR_CREATE = 1
}
