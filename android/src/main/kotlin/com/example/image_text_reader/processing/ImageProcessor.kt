package com.example.image_text_reader.processing

import android.graphics.BitmapFactory
import com.example.image_text_reader.models.ImageInput

class ImageProcessor {

  fun process(
    image: ImageInput
  ): ProcessedImage {

    val bitmap =
      BitmapFactory.decodeFile(
        image.path
      ) ?: throw IllegalArgumentException(
        "Could not load image: ${image.path}"
      )

    println(
      "IMAGE LOADED: ${bitmap.width} x ${bitmap.height}"
    )

    return ProcessedImage(
      bitmap = bitmap,
      width = bitmap.width,
      height = bitmap.height
    )
  }
}