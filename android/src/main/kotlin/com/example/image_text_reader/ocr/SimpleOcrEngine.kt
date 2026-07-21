package com.example.image_text_reader.ocr


import com.example.image_text_reader.models.ImageInput


class SimpleOcrEngine : OcrEngine {


    override fun extractText(
        image: ImageInput
    ): String {


        if(!image.exists()) {

            return "Image does not exist"

        }


        return """
        OCR ENGINE ACTIVE
        
        Image found:
        ${image.path}
        
        Image size:
        ${image.file().length()} bytes
        
        Ready for OCR processing.
        """.trimIndent()

    }


}