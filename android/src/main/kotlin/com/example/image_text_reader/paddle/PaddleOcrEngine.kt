package com.example.image_text_reader.paddle


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.processing.ProcessedImage

class PaddleOcrEngine(
    private val onnxEngine: OnnxEngine
) {


    fun extractText(
        image: ProcessedImage
    ): OcrResult {

        /*
            Step 1:
            Run detection model

            TODO


            Step 2:
            Run recognition model

            TODO


            Step 3:
            Decode text

            TODO
        */


        return OcrResult(
            text = "OCR pipeline connected",
            confidence = 1.0f
        )

    }

}