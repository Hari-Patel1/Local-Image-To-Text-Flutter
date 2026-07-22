package com.example.image_text_reader.paddle


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.ImageTensorConverter
import com.example.image_text_reader.processing.ProcessedImage


class PaddleOcrEngine(
    private val onnxEngine: OnnxEngine
) {


    private val tensorConverter =
        ImageTensorConverter()



    fun extractText(
        image: ProcessedImage
    ): OcrResult {


        val tensor =
            tensorConverter.convert(
                image.bitmap
            )


        return OcrResult(
            text = "OCR tensor created",
            confidence = 1.0f
        )

    }

}