package com.example.image_text_reader.processing


import android.graphics.BitmapFactory
import com.example.image_text_reader.models.ImageInput



class ImageProcessor {


  fun process(
    image: ImageInput
  ): ProcessedImage {


    if(!image.exists()) {

      throw IllegalArgumentException(
        "Image does not exist"
      )

    }


    val bitmap =
      BitmapFactory.decodeFile(
        image.path
      )


    return ProcessedImage(

      bitmap = bitmap,

      width = bitmap.width,

      height = bitmap.height

    )

  }

}