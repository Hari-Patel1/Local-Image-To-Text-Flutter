package com.example.image_text_reader.detector


import com.example.image_text_reader.models.TextBox


class DbPostProcessor {


    fun process(
        output: FloatArray,
        width: Int,
        height: Int
    ): List<TextBox> {


        val boxes =
            mutableListOf<TextBox>()


        var max =
            0f


        for(value in output){

            if(value > max){
                max = value
            }

        }


        if(max > 0.3f){

            boxes.add(

                TextBox(

                    left = 0f,

                    top = 0f,

                    right = width.toFloat(),

                    bottom = height.toFloat(),

                    confidence = max

                )

            )

        }


        return boxes

    }

}