package com.example.image_text_reader.models


data class BoundingBox(

    val left: Float,

    val top: Float,

    val right: Float,

    val bottom: Float,

    val confidence: Float

)