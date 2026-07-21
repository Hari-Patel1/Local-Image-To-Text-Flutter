package com.example.image_text_reader.ocr

import com.example.image_text_reader.models.ImageInput


interface OcrEngine {

    fun extractText(
        image: ImageInput
    ): String

}