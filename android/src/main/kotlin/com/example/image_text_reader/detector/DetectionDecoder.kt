package com.example.image_text_reader.detector

import com.example.image_text_reader.models.TextBox


class DetectionDecoder {


    fun decode(
        output: Array<Array<Array<FloatArray>>>,
        originalWidth: Int,
        originalHeight: Int,
        inputWidth: Int,
        inputHeight: Int,
        threshold: Float = 0.3f
    ): List<TextBox> {


        val boxes =
            mutableListOf<TextBox>()


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


        var found = false


        for(y in 0 until height){

            for(x in 0 until width){


                if(map[y][x] > threshold){


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


        if(found){


            // Convert detector coordinates back
            // to original image coordinates

            val scaleX =
                originalWidth.toFloat() /
                        inputWidth.toFloat()


            val scaleY =
                originalHeight.toFloat() /
                        inputHeight.toFloat()



            boxes.add(

                TextBox(

                    minX * scaleX,

                    minY * scaleY,

                    maxX * scaleX,

                    maxY * scaleY,

                    1.0f
                )

            )

        }


        return boxes

    }

}