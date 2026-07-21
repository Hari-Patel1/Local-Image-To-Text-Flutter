package com.example.image_text_reader.ml


import ai.onnxruntime.OrtEnvironment


class OnnxEngine {


    private val environment =
        OrtEnvironment.getEnvironment()


    fun initialise(): Boolean {


        return environment != null

    }

}