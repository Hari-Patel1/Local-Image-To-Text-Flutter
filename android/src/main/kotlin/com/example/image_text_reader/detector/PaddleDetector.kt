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
        tensor: TensorImage,
        originalWidth: Int,
        originalHeight: Int
    ): List<TextBox> {


        println(
            "Detector tensor: width=${tensor.width}, height=${tensor.height}"
        )


        val result =
            onnxEngine.run(
                "detector",
                tensor.buffer,
                longArrayOf(
                    1,
                    3,
                    tensor.height.toLong(),
                    tensor.width.toLong()
                )
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



        /*
         Detector output is:
         [1,1,height,width]

         We pass the probability map
         into DB post processing.
        */


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