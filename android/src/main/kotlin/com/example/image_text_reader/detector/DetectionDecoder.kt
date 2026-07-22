package com.example.image_text_reader.detector


import com.example.image_text_reader.models.BoundingBox


class DetectionDecoder {


    fun decode(
        output: Array<Array<Array<FloatArray>>>,
        threshold: Float = 0.3f
    ): List<BoundingBox> {


        val boxes =
            mutableListOf<BoundingBox>()


        val map =
            output[0][0]


        val height =
            map.size


        val width =
            map[0].size


        var minX = width
        var minY = height

        var maxX = 0
        var maxY = 0


        var found =
            false


        for (y in 0 until height) {

            for (x in 0 until width) {


                if(map[y][x] > threshold) {


                    found = true


                    if(x < minX)
                        minX = x


                    if(x > maxX)
                        maxX = x


                    if(y < minY)
                        minY = y


                    if(y > maxY)
                        maxY = y

                }
            }
        }


        if(found) {

            boxes.add(

                BoundingBox(

                    minX.toFloat(),

                    minY.toFloat(),

                    maxX.toFloat(),

                    maxY.toFloat(),

                    1.0f

                )

            )

        }


        return boxes

    }

}