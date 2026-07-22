package com.example.image_text_reader.ml


import android.content.Context


class ModelManager(
    private val context: Context
) {


    fun modelExists(
        path: String
    ): Boolean {


        return try {

            context.assets.open(path)

            true

        } catch(e: Exception) {

            false

        }

    }


}