package com.stylingandroid.invertimage

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

class NativeImageProcessor : ImageProcessor {

    override suspend fun processImage(bitmap: Bitmap): Bitmap {
        val startTime = System.currentTimeMillis()
        return Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config).let { output ->
            for (x in 0 until bitmap.width) {
                for (y in 0 until bitmap.height) {
                    output.setPixel(x, y, bitmap.getPixel(x, y).invert())
                }
            }
            output
        }.also {
            println("Native Inversion time: ${it.width} x ${it.height} " +
                    "image in ${System.currentTimeMillis() - startTime}ms")
        }
    }

    private fun Int.invert(): Int =
        Color.rgb(MAX_UCHAR - red, MAX_UCHAR - green, MAX_UCHAR - blue)

    companion object {
        private const val MAX_UCHAR = 0xff
    }
}
