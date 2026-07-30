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

            println("MAXINDEX: $maxIndex")


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

//package com.example.image_text_reader.recogniser
//
//class CtcDecoder(
//    private val dictionary: List<String>
//) {
//
//    // TUNE THIS once you've looked at the "BLANK RUN" logs for a few real
//    // lines. Start here, then adjust: raise it if you're getting spurious
//    // spaces inside words, lower it if real word-gaps aren't producing one.
//    private val spaceBlankThreshold = 4
//
//    init {
//
//        println("DICTIONARY FIRST 20:")
//        dictionary.take(20).forEachIndexed { i, c ->
//            println("$i -> $c")
//        }
//
//        println("DICTIONARY LAST 10:")
//        dictionary.takeLast(10).forEachIndexed { i, c ->
//            println("${dictionary.size - 10 + i} -> $c")
//        }
//
//        println("Dictionary size = ${dictionary.size}")
//    }
//
//
//    fun decode(
//        probabilities: Array<FloatArray>
//    ): String {
//
//        val result = StringBuilder()
//
//        var lastIndex = -1
//
//        val blankIndex = 0
//
//        // Tracks how many consecutive timesteps have argmax'd to blank.
//        // A long run at a real word gap tends to be longer than the short
//        // runs that separate two different letters within the same word,
//        // even when the model never actually predicts the space class.
//        var blankRunLength = 0
//
//
//        for(timeStep in probabilities) {
//
//            var maxIndex = 0
//            var maxValue = Float.NEGATIVE_INFINITY
//
//
//            for(i in timeStep.indices) {
//
//                if(timeStep[i] > maxValue) {
//
//                    maxValue = timeStep[i]
//                    maxIndex = i
//
//                }
//
//            }
//
//
//
//            if(maxValue > 0.8f) {
//                println(
//                    "CTC: index=$maxIndex confidence=$maxValue"
//                )
//            }
//
//            if (maxIndex >= dictionary.size - 5) {
//                println("HIGH INDEX: $maxIndex")
//            }
//
//
//            // CTC blank token
//            if(maxIndex == blankIndex) {
//
//                blankRunLength++
//
//                lastIndex = -1
//                continue
//
//            }
//
//
//            // We just left a run of blanks (possibly zero-length, e.g. two
//            // different letters back to back with no blank between them).
//            // Log the run length so you can calibrate spaceBlankThreshold
//            // against real data -- remove this println once you're happy
//            // with the threshold value.
//            if (blankRunLength > 0) {
//                println("BLANK RUN: $blankRunLength")
//            }
//
//            // A sufficiently long blank run just ended -> treat it as a
//            // word gap and insert a space, provided we've already emitted
//            // something and the last character isn't already a space.
//            if (blankRunLength >= spaceBlankThreshold &&
//                result.isNotEmpty() &&
//                result.last() != ' '
//            ) {
//                result.append(' ')
//            }
//
//            blankRunLength = 0
//
//
//            println("MAXINDEX: $maxIndex")
//
//
//            if(maxIndex != lastIndex) {
//
//                if(maxIndex < dictionary.size) {
//
//                    result.append(
//                        dictionary[maxIndex]
//                    )
//
//                } else {
//
//                    println(
//                        "INVALID INDEX $maxIndex"
//                    )
//
//                }
//
//            }
//
//
//            lastIndex = maxIndex
//
//        }
//
//
//        return result.toString()
//
//    }
//}