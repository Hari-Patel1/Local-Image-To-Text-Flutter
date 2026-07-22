package com.example.image_text_reader.recogniser

class CtcDecoder(
    private val dictionary: List<String>
) {

    init {

        println("DICTIONARY FIRST 20:")
        dictionary.take(20).forEachIndexed { i, c ->
            println("$i -> $c")
        }

        println("DICTIONARY LAST 10:")
        dictionary.takeLast(10).forEachIndexed { i, c ->
            println("${dictionary.size - 10 + i} -> $c")
        }

        println("Dictionary size = ${dictionary.size}")
    }


    fun decode(
        probabilities: Array<FloatArray>
    ): String {

        val result = StringBuilder()

        var lastIndex = -1

        val blankIndex = 0


        for(timeStep in probabilities) {

            var maxIndex = 0
            var maxValue = Float.NEGATIVE_INFINITY


            for(i in timeStep.indices) {

                if(timeStep[i] > maxValue) {

                    maxValue = timeStep[i]
                    maxIndex = i

                }

            }



            if(maxValue > 0.8f) {
                println(
                    "CTC: index=$maxIndex confidence=$maxValue"
                )
            }

            if (maxIndex >= dictionary.size - 5) {
                println("HIGH INDEX: $maxIndex")
            }


            // CTC blank token
            if(maxIndex == blankIndex) {

                lastIndex = -1
                continue

            }

            println(maxIndex)


            if(maxIndex != lastIndex) {

                if(maxIndex < dictionary.size) {

                    result.append(
                        dictionary[maxIndex]
                    )

                } else {

                    println(
                        "INVALID INDEX $maxIndex"
                    )

                }

            }


            lastIndex = maxIndex

        }


        return result.toString()

    }
}