package com.example.image_text_reader.detector

import android.graphics.Bitmap
import com.example.image_text_reader.models.TextBox
import com.example.image_text_reader.models.TextRegion

class TextCropper {

    fun crop(bitmap: Bitmap, boxes: List<TextBox>): List<TextRegion> {

        val regions = mutableListOf<TextRegion>()

        for (box in boxes) {

            val left = box.left.toInt().coerceIn(0, bitmap.width - 1)
            val top = box.top.toInt().coerceIn(0, bitmap.height - 1)
            val right = box.right.toInt().coerceIn(left + 1, bitmap.width)
            val bottom = box.bottom.toInt().coerceIn(top + 1, bitmap.height)

            println("CROP: $left,$top -> $right,$bottom")

            val crop = Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)

            println("CROP SIZE: ${crop.width}x${crop.height}")

            regions.add(TextRegion(crop, box.confidence))
        }

        return regions
    }
}