package com.example.image_text_reader.models


import java.io.File


data class ImageInput(

    val path: String

) {

    fun exists(): Boolean {
        return File(path).exists()
    }


    fun file(): File {
        return File(path)
    }

}