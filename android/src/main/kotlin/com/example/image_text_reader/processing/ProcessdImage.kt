package com.example.image_text_reader.processing


import android.graphics.Bitmap


data class ProcessedImage(

  val bitmap: Bitmap,

  val width: Int,

  val height: Int

)