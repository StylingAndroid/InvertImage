package com.stylingandroid.invertimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stylingandroid.invertimage.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val nativeImageProcessor = NativeImageProcessor()

            binding.nativeInvert.setImageBitmap(processImage(nativeImageProcessor))
        }

        lifecycleScope.launch {
            val rsImageProcessor = RenderscriptImageProcessor(this@MainActivity, this@MainActivity)

            binding.renderscriptInvert.setImageBitmap(processImage(rsImageProcessor))
        }
    }

    private suspend fun processImage(imageProcessor: ImageProcessor): Bitmap {
        return withContext(Dispatchers.IO) {
            imageProcessor.processImage(
                BitmapFactory.decodeResource(resources, R.drawable.beach_huts)
            )
        }
    }
}
