package com.example.image_text_reader.ml

import ai.onnxruntime.*
import android.content.Context
import java.nio.FloatBuffer


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
                environment.createSession(
                    modelBytes
                )


            println(
                "$name INPUT INFO: ${session.inputInfo}"
            )


            println(
                "$name OUTPUT INFO: ${session.outputInfo}"
            )


            sessions[name] =
                session


            true


        } catch(e: Exception) {

            e.printStackTrace()

            false

        }

    }



    fun isLoaded(
        name: String
    ): Boolean {

        return sessions.containsKey(name)

    }



    fun run(
        modelName: String,
        input: FloatBuffer,
        shape: LongArray
    ): OrtSession.Result {


        val session =
            sessions[modelName]
                ?: throw Exception(
                    "Model not loaded: $modelName"
                )

        println(
            "MODEL $modelName INPUTS: ${session.inputInfo}"
        )

        println(
            "MODEL $modelName OUTPUTS: ${session.outputInfo}"
        )


        println(
            "ONNX SHAPE: ${shape.contentToString()}"
        )


        val tensor =
            OnnxTensor.createTensor(
                environment,
                input,
                shape
            )


        val inputs =
            mapOf(
                session.inputNames.first() to tensor
            )


        return session.run(
            inputs
        )

    }

}