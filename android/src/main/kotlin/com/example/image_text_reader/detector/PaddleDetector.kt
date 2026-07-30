package com.example.image_text_reader.detector

import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.ml.DetectorTensor
import com.example.image_text_reader.models.TextBox

import ai.onnxruntime.OnnxTensor

class PaddleDetector(
    private val onnxEngine: OnnxEngine
) {

    private val postProcessor = DbPostProcessor()

    fun detect(
        detectorTensor: DetectorTensor,
        originalWidth: Int,
        originalHeight: Int
    ): List<TextBox> {

        val tensor = detectorTensor.tensorImage

        println("Detector tensor: width=${tensor.width}, height=${tensor.height}")

        val result =
            onnxEngine.run(
                "detector",
                tensor.buffer,
                longArrayOf(1, 3, tensor.height.toLong(), tensor.width.toLong())
            )

        val output = result[0] as OnnxTensor
        val buffer = output.floatBuffer
        val values = FloatArray(buffer.remaining())
        buffer.get(values)

        val rawBoxes =
            postProcessor.process(values, tensor.width, tensor.height)

        // Undo letterbox padding + scale to map back to original image coords
        val boxes = rawBoxes.map { box ->
            TextBox(
                left = (box.left - detectorTensor.padX) / detectorTensor.scale,
                top = (box.top - detectorTensor.padY) / detectorTensor.scale,
                right = (box.right - detectorTensor.padX) / detectorTensor.scale,
                bottom = (box.bottom - detectorTensor.padY) / detectorTensor.scale,
                confidence = box.confidence
            )
        }

        println("Detected boxes: ${boxes.size}")

        result.close()

        return boxes
    }
}