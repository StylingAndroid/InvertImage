package com.stylingandroid.invertimage

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class RenderscriptImageProcessor(
    context: Context,
    lifecycleOwner: LifecycleOwner
) : LifecycleObserver, ImageProcessor {

    private val renderScript = RenderScript.create(context)
    private val script: ScriptC_Invert = ScriptC_Invert(renderScript)
    private lateinit var input: Allocation
    private lateinit var output: Allocation

    /**
     * Re-using outputBitmap may not be appropriate for all use cases. Please check the
     * explanation at https://blog.stylingandroid.com/introduction-to-renderscript for further
     * details.
     */
    private lateinit var outputBitmap: Bitmap

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override suspend fun processImage(bitmap: Bitmap): Bitmap {
        val start = System.currentTimeMillis()
        if (!::input.isInitialized || input.bytesSize != bitmap.byteCount) {
            destroyAllocations()
            input = Allocation.createFromBitmap(renderScript, bitmap)
            output = Allocation.createFromBitmap(renderScript, bitmap)
            outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        }
        input.copyFrom(bitmap)
        script.forEach_root(input, output)
        output.copyTo(outputBitmap)
        println("Renderscript Inversion Time: ${outputBitmap.width} x ${outputBitmap.height} " +
                "image in ${System.currentTimeMillis() - start}ms")
        return outputBitmap
    }

    private fun destroyAllocations() {
        if (::input.isInitialized) input.destroy()
        if (::output.isInitialized) output.destroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        destroyAllocations()
        script.destroy()
        renderScript.destroy()
    }
}
