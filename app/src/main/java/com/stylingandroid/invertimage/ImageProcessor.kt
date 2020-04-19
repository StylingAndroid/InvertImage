package com.stylingandroid.invertimage

import android.graphics.Bitmap

interface ImageProcessor {
    suspend fun processImage(bitmap: Bitmap): Bitmap
}
