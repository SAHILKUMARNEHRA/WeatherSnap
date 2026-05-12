package com.trackzio.weathersnap.ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

data class CompressionResult(
    val compressedFile: File,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long,
)

fun compressJpegToFile(
    originalFile: File,
    outputFile: File,
    maxDimensionPx: Int = 1280,
    quality: Int = 75,
): CompressionResult {
    val originalSize = originalFile.length()
    val decoded = decodeScaledBitmap(originalFile, maxDimensionPx) ?: error("Failed to decode image")

    FileOutputStream(outputFile).use { out ->
        decoded.compress(Bitmap.CompressFormat.JPEG, quality, out)
        out.flush()
    }

    val compressedSize = outputFile.length()
    return CompressionResult(
        compressedFile = outputFile,
        originalSizeBytes = originalSize,
        compressedSizeBytes = compressedSize,
    )
}

private fun decodeScaledBitmap(file: File, maxDim: Int): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, bounds)

    val srcW = bounds.outWidth
    val srcH = bounds.outHeight
    if (srcW <= 0 || srcH <= 0) return null

    val largest = max(srcW, srcH)
    val sample = computeInSampleSize(largest, maxDim)

    val opts = BitmapFactory.Options().apply {
        inSampleSize = sample
        inPreferredConfig = Bitmap.Config.ARGB_8888
    }
    return BitmapFactory.decodeFile(file.absolutePath, opts)
}

private fun computeInSampleSize(largestDim: Int, maxDim: Int): Int {
    var inSampleSize = 1
    while (largestDim / inSampleSize > maxDim) {
        inSampleSize *= 2
    }
    return inSampleSize
}

