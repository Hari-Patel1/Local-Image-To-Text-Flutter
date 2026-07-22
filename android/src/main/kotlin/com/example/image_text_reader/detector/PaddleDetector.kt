package com.example.image_text_reader.detector


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.TensorImage



class PaddleDetector(
    private val onnxEngine: OnnxEngine
) {


    fun detect(
        tensor: TensorImage
    ) {


        val result =
            onnxEngine.run(
                "detector",
                tensor.buffer,
                tensor.width,
                tensor.height
            )


        result.close()

    }

}