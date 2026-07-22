package com.example.image_text_reader.recogniser

import kotlin.math.exp


object Softmax {


    fun apply(
        values: FloatArray
    ): FloatArray {


        val max =
            values.maxOrNull() ?: 0f


        val expValues =
            values.map {
                exp(it - max)
            }


        val sum =
            expValues.sum()


        return expValues.map {
            it / sum
        }.toFloatArray()

    }

}