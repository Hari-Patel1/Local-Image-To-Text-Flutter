package com.example.image_text_reader.ml


import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context

import ai.onnxruntime.OnnxTensor


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
    fun run(
        name: String,
        input: OnnxTensor
    ): OrtSession.Result {


        val session =
            sessions[name]
                ?: throw IllegalStateException(
                    "Model not loaded: $name"
                )


        val inputs =
            mapOf(
                session.inputNames.first() to input
            )


        return session.run(
            inputs
        )

    }

}