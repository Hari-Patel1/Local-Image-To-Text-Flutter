package com.example.image_text_reader.ml

import android.graphics.Bitmap
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import java.nio.FloatBuffer


object TensorUtils {


    fun bitmapToTensor(
        environment: OrtEnvironment,
        bitmap: Bitmap,
        width: Int,
        height: Int
    ): OnnxTensor {


        val resized =
            Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                true
            )


        val input =
            FloatArray(
                3 * width * height
            )


        var indexR = 0
        var indexG = width * height
        var indexB = 2 * width * height



        for (y in 0 until height) {

            for (x in 0 until width) {


                val pixel =
                    resized.getPixel(
                        x,
                        y
                    )


                val r =
                    ((pixel shr 16) and 0xFF) / 255.0f


                val g =
                    ((pixel shr 8) and 0xFF) / 255.0f


                val b =
                    (pixel and 0xFF) / 255.0f



                input[indexR++] = r
                input[indexG++] = g
                input[indexB++] = b

            }
        }


        val shape =
            longArrayOf(
                1,
                3,
                height.toLong(),
                width.toLong()
            )


        val buffer =
            FloatBuffer.wrap(
                input
            )


        return OnnxTensor.createTensor(
            environment,
            buffer,
            shape
        )

    }

}