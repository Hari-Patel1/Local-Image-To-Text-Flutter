package com.example.image_text_reader.ml

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class DetectorTensor(
    val tensorImage: TensorImage,
    val scale: Float,
    val padX: Int,
    val padY: Int
)

class ImageTensorConverter {

    fun convert(bitmap: Bitmap): DetectorTensor {

        val targetSize = 640

        val scale = minOf(
            targetSize.toFloat() / bitmap.width,
            targetSize.toFloat() / bitmap.height
        )

        val scaledWidth = (bitmap.width * scale).toInt()
        val scaledHeight = (bitmap.height * scale).toInt()

        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)

        val padX = (targetSize - scaledWidth) / 2
        val padY = (targetSize - scaledHeight) / 2

        val padded = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888)
        Canvas(padded).apply {
            drawColor(Color.BLACK)
            drawBitmap(scaledBitmap, padX.toFloat(), padY.toFloat(), null)
        }

        println("DETECTOR CONVERTER: scaled=${scaledWidth}x${scaledHeight} pad=($padX,$padY)")

        val buffer =
            ByteBuffer
                .allocateDirect(1 * 3 * targetSize * targetSize * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()

        val pixels = IntArray(targetSize * targetSize)

        padded.getPixels(pixels, 0, targetSize, 0, 0, targetSize, targetSize)

        // RED CHANNEL
        for (pixel in pixels) {
            buffer.put(((pixel shr 16) and 0xFF) / 255f)
        }
        // GREEN CHANNEL
        for (pixel in pixels) {
            buffer.put(((pixel shr 8) and 0xFF) / 255f)
        }
        // BLUE CHANNEL
        for (pixel in pixels) {
            buffer.put((pixel and 0xFF) / 255f)
        }

        buffer.rewind()

        return DetectorTensor(
            TensorImage(buffer, targetSize, targetSize),
            scale,
            padX,
            padY
        )
    }
}