package com.example.image_text_reader.recogniser

import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.models.TextRegion
import com.example.image_text_reader.processing.RecognizerImageProcessor
import com.example.image_text_reader.ml.RecognizerTensorConverter

import ai.onnxruntime.OnnxTensor
import android.content.Context
import java.io.File
import java.io.FileOutputStream
import android.os.Environment


class PaddleRecognizer(
    private val context: Context,
    private val onnxEngine: OnnxEngine
) {

    private val dictionary =
        context.assets
            .open("models/ppocr_keys.txt")
            .bufferedReader()
            .readLines()
            .toMutableList()
            .apply {
                if (lastOrNull() != " ") {
                    add(" ")
                }
                add(0, "blank")
            }

    private val decoder =
        CtcDecoder(
            dictionary
        )


    private val processor =
        RecognizerImageProcessor()


    private val tensorConverter =
        RecognizerTensorConverter()



    fun recognise(
        region: TextRegion
    ): String {


        println(
            "REGION SIZE: ${region.bitmap.width} x ${region.bitmap.height}"
        )


        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ),
            "ocr_crop.png"
        )

        val output =
            java.io.FileOutputStream(file)

        region.bitmap.compress(
            android.graphics.Bitmap.CompressFormat.PNG,
            100,
            output
        )

        output.close()

        println(
            "SAVED CROP: ${file.absolutePath}"
        )



        val resized =
            processor.resize(
                region.bitmap
            )

        val sampleX = resized.width / 2
        val sampleY = resized.height / 2

        println(
            "RECOGNIZER PIXELS: ${
                resized.getPixel(sampleX, sampleY)
            }"
        )
        println("Dictionary size = ${dictionary.size}")

        println(
            "RESIZED SIZE: ${resized.width} x ${resized.height}"
        )


        val tensor =
            tensorConverter.convert(
                resized
            )


        println(
            "PASSING TO ONNX width=${tensor.width} height=${tensor.height}"
        )


        val result =
            onnxEngine.run(
                "recogniser",
                tensor.buffer,
                longArrayOf(
                    1,
                    3,
                    tensor.height.toLong(),
                    tensor.width.toLong()
                )
            )


        println(
            "Recognizer outputs: ${result.count()}"
        )


        val outputTensor =
            result[0] as OnnxTensor

        val raw =
            outputTensor.value as Array<Array<FloatArray>>

        val probabilities =
            raw[0]

        val decoded =
            decoder.decode(probabilities)


        println(
            "OCR TEXT: $decoded"
        )


        println(
            outputTensor.info.shape.contentToString()
        )

        println(
            outputTensor.value!!::class.java.name
        )

        println(
            "REC OUTPUT SHAPE: ${
                outputTensor.info.shape.contentToString()
            }"
        )


        result.close()


        return decoded

    }

}