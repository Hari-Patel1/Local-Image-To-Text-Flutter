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

    private val decoder =
        DetectionDecoder()


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


        val output =
            result[0].value as Array<Array<Array<FloatArray>>>


        val boxes =
            decoder.decode(
                output
            )


        Log.d(
            "PADDLE_DETECTOR",
            "Detected boxes: ${boxes.size}"
        )

    }

}