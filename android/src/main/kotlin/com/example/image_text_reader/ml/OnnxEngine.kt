package com.example.image_text_reader.ml


import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context


class OnnxEngine(
    private val context: Context
) {


    private val environment =
        OrtEnvironment.getEnvironment()


    private val sessions =
        mutableMapOf<String, OrtSession>()



    fun loadModel(
        name: String,
        path: String
    ): Boolean {


        return try {


            val modelBytes =
                context.assets
                    .open(path)
                    .readBytes()


            val session =
                environment
                    .createSession(
                        modelBytes
                    )


            sessions[name] =
                session


            true


        } catch(e: Exception) {


            false

        }

    }



    fun isLoaded(
        name: String
    ): Boolean {

        return sessions.containsKey(name)

    }


}