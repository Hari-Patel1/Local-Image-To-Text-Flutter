package com.example.image_text_reader.detector


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.TensorUtils
import com.example.image_text_reader.processing.ProcessedImage
import ai.onnxruntime.OrtEnvironment
import android.util.Log



class PaddleDetector(
    private val onnxEngine: OnnxEngine,
    private val environment: OrtEnvironment
) {


    fun detect(
        image: ProcessedImage
    ) {


        val tensor =
            TensorUtils.bitmapToTensor(
                environment,
                image.bitmap,
                640,
                640
            )


        val result =
            onnxEngine.run(
                "detector",
                tensor
            )


        for (i in 0 until result.size()) {

            val output =
                result[i]

            Log.d(
                "PADDLE_DETECTOR",
                "Detector output [$i]: ${output.value}"
            )

        }

    }

}