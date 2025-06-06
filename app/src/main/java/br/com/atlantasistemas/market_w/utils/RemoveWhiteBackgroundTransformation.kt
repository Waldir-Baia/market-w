package br.com.atlantasistemas.market_w.utils

import android.graphics.Bitmap
import android.graphics.Color
import coil.size.Size
import coil.transform.Transformation

class RemoveWhiteBackgroundTransformation : Transformation {

    override val cacheKey: String
        get() = "RemoveWhiteBackgroundTransformation"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val width = input.width
        val height = input.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = input.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)

                // Remove tons de branco
                if (red > 240 && green > 240 && blue > 240) {
                    result.setPixel(x, y, Color.TRANSPARENT)
                } else {
                    result.setPixel(x, y, pixel)
                }
            }
        }

        return result
    }
}