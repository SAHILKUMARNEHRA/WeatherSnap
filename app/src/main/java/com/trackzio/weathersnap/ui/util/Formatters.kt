package com.trackzio.weathersnap.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun formatTemperature(valueC: Double): String = "${valueC.roundToInt()}°C"

fun formatSizeKb(bytes: Long): String {
    val kb = bytes.toDouble() / 1024.0
    return "${kb.roundToInt()} KB"
}

fun formatTimestamp(epochMs: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault())
    return sdf.format(Date(epochMs))
}

