package com.example.image_text_reader.ml


import java.nio.FloatBuffer


data class TensorImage(

    val buffer: FloatBuffer,

    val width: Int,

    val height: Int

)