package com.example.image_text_reader.models

import android.graphics.Bitmap

data class TextRegion(
    val bitmap: Bitmap,
    val confidence: Float
)