package com.example.image_text_reader.ml

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RecognizerTensorConverter {

    fun convert(bitmap: Bitmap): TensorImage {

        val width = bitmap.width
        val height = bitmap.height

        println(
            "RECOGNIZER CONVERTER: ${width} x ${height}"
        )

        val buffer =
            ByteBuffer
                .allocateDirect(
                    1 * 3 * width * height * 4
                )
                .order(
                    ByteOrder.nativeOrder()
                )
                .asFloatBuffer()


        val pixels =
            IntArray(width * height)


        bitmap.getPixels(
            pixels,
            0,
            width,
            0,
            0,
            width,
            height
        )


        // RED CHANNEL
        for(pixel in pixels) {

            val r =
                ((pixel shr 16) and 0xFF) / 255f

            buffer.put(
                (r - 0.5f) / 0.5f
            )
        }


        // GREEN CHANNEL
        for(pixel in pixels) {

            val g =
                ((pixel shr 8) and 0xFF) / 255f

            buffer.put(
                (g - 0.5f) / 0.5f
            )
        }


        // BLUE CHANNEL
        for(pixel in pixels) {

            val b =
                (pixel and 0xFF) / 255f

            buffer.put(
                (b - 0.5f) / 0.5f
            )
        }


        buffer.rewind()


        return TensorImage(
            buffer,
            width,
            height
        )
    }
}