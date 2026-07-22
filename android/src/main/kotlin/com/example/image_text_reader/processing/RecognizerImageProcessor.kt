package com.example.image_text_reader.processing

import android.graphics.Bitmap
import android.graphics.BitmapFactory


class RecognizerImageProcessor {


    fun resize(
        bitmap: Bitmap
    ): Bitmap {


        val targetHeight = 48


        val ratio =
            bitmap.width.toFloat() /
                    bitmap.height.toFloat()


        val targetWidth =
            (targetHeight * ratio)
                .toInt()


        return Bitmap.createScaledBitmap(
            bitmap,
            targetWidth,
            targetHeight,
            true
        )

    }

}