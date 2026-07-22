package com.example.image_text_reader.paddle


import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.ImageTensorConverter
import com.example.image_text_reader.processing.ProcessedImage
import com.example.image_text_reader.detector.PaddleDetector
import com.example.image_text_reader.recogniser.PaddleRecognizer
import com.example.image_text_reader.detector.TextCropper

import android.content.Context

class PaddleOcrEngine(
    private val context: Context,
    private val onnxEngine: OnnxEngine

) {


    private val tensorConverter =
        ImageTensorConverter()


    private val detector =
        PaddleDetector(
            onnxEngine
        )

    private val recognizer =
        PaddleRecognizer(
            context,
            onnxEngine
        )

    private val cropper =
        TextCropper()


    fun extractText(
        image: ProcessedImage
    ): OcrResult {


        val startTime =
            System.currentTimeMillis()


        val tensor =
            tensorConverter.convert(
                image.bitmap
            )


        val boxes =
            detector.detect(
                tensor,
                image.bitmap.width,
                image.bitmap.height
            )


        val duration =
            System.currentTimeMillis() - startTime


        println(
            "OCR detection took ${duration}ms"
        )

        val regions =
            cropper.crop(
                image.bitmap,
                boxes
            )


        val recognisedText =
            mutableListOf<String>()

        for(region in regions){

            val text =
                recognizer.recognise(
                    region
                )

            recognisedText.add(text)

            println(
                "RECOGNIZED: $text"
            )

        }



        println("OCR PIPELINE COMPLETE")

        return OcrResult(
            text = "ANDROID OCR SUCCESS",
            confidence = 1.0f
        )

    }

}