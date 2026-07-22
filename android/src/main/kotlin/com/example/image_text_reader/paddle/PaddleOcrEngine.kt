package com.example.image_text_reader.paddle


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.ImageTensorConverter
import com.example.image_text_reader.processing.ProcessedImage
import com.example.image_text_reader.detector.PaddleDetector


class PaddleOcrEngine(
    private val onnxEngine: OnnxEngine
) {


    private val tensorConverter =
        ImageTensorConverter()


    private val detector =
        PaddleDetector(
            onnxEngine
        )



    fun extractText(
        image: ProcessedImage
    ): OcrResult {


        val tensor =
            tensorConverter.convert(
                image.bitmap
            )


        val boxes =
            detector.detect(
                tensor
            )


        return OcrResult(
            text = "Detected boxes: ${boxes.size}",
            confidence = boxes.firstOrNull()?.confidence ?: 0f
        )

    }

}