package com.thrashed.flibuster.base.imageView

import android.graphics.*
import android.graphics.Bitmap.createBitmap
import androidx.annotation.ColorInt
import coil.size.Size
import coil.transform.Transformation

class ColorFilterTransformation(
    @ColorInt private val color: Int
) : Transformation {

    override val cacheKey: String = "${ColorFilterTransformation::class.java.name}-$color"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val output = createBitmap(input.width, input.height, input.config)

        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        canvas.drawBitmap(input, 0f, 0f, paint)

        return output
    }
}