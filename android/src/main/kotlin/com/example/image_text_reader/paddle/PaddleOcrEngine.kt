package com.example.image_text_reader.paddle


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.models.ImageInput
import com.example.image_text_reader.ocr.OcrEngine


class PaddleOcrEngine(
    private val onnxEngine: OnnxEngine
) : OcrEngine {


    override fun extractText(
        image: ImageInput
    ): String {


        return """
        PaddleOCR Engine

        Image:
        ${image.path}

        ONNX backend:
        Active

        Model:
        Not loaded yet
        """.trimIndent()

    }

}