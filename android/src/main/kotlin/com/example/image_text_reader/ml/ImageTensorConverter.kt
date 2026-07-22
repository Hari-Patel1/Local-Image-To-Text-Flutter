package com.example.image_text_reader.ml


import android.graphics.Bitmap
import java.nio.FloatBuffer
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


        val inputSize =
            1 * 3 * 640 * 640


        val buffer =
            ByteBuffer
                .allocateDirect(
                    inputSize * 4
                )
                .order(
                    ByteOrder.nativeOrder()
                )
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


            val r =
                ((pixel shr 16) and 0xFF) / 255.0f


            val g =
                ((pixel shr 8) and 0xFF) / 255.0f


            val b =
                (pixel and 0xFF) / 255.0f



            buffer.put(r)

            buffer.put(g)

            buffer.put(b)

        }



        buffer.rewind()



        return TensorImage(
            buffer,
            640,
            640
        )

    }

}