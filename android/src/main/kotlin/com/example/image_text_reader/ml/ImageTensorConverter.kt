package com.example.image_text_reader.ml

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageTensorConverter {

    fun convert(
        bitmap: Bitmap
    ): TensorImage {

        val resized =
            Bitmap.createScaledBitmap(
                bitmap,
                640,
                640,
                true
            )

        println(
            "DETECTOR CONVERTER: ${resized.width} x ${resized.height}"
        )

        val buffer =
            ByteBuffer
                .allocateDirect(
                    1 * 3 * 640 * 640 * 4
                )
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()


        val pixels =
            IntArray(
                640 * 640
            )

        resized.getPixels(
            pixels,
            0,
            640,
            0,
            0,
            640,
            640
        )


        for(pixel in pixels){

            buffer.put(
                ((pixel shr 16) and 0xFF) / 255f
            )

            buffer.put(
                ((pixel shr 8) and 0xFF) / 255f
            )

            buffer.put(
                (pixel and 0xFF) / 255f
            )
        }


        buffer.rewind()


        return TensorImage(
            buffer,
            640,
            640
        )
    }
}