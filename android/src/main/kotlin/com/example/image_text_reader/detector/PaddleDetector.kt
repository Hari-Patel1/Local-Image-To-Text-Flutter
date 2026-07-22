package com.example.image_text_reader.detector


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.TensorImage
import com.example.image_text_reader.models.TextBox

import ai.onnxruntime.OnnxTensor



class PaddleDetector(
    private val onnxEngine: OnnxEngine
) {


    private val postProcessor =
        DbPostProcessor()



    fun detect(
        tensor: TensorImage
    ): List<TextBox> {


        val result =
            onnxEngine.run(
                "detector",
                tensor.buffer,
                tensor.width,
                tensor.height
            )


        val output =
            result[0] as OnnxTensor



        val buffer =
            output.floatBuffer


        val values =
            FloatArray(
                buffer.remaining()
            )


        buffer.get(values)



        val boxes =
            postProcessor.process(
                values,
                tensor.width,
                tensor.height
            )



        println(
            "Detected boxes: ${boxes.size}"
        )



        result.close()


        return boxes

    }


}