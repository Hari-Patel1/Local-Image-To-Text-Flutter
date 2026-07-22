package com.example.image_text_reader.detector

import android.graphics.Bitmap
import com.example.image_text_reader.models.TextBox
import com.example.image_text_reader.models.TextRegion


class TextCropper {


    fun crop(
        bitmap: Bitmap,
        boxes: List<TextBox>
    ): List<TextRegion> {


        val regions = mutableListOf<TextRegion>()


        val scaleX =
            bitmap.width / 640f


        val scaleY =
            bitmap.height / 640f



        for(box in boxes){


            val left =
                (box.left * scaleX)
                    .toInt()
                    .coerceIn(0, bitmap.width - 1)


            val top =
                (box.top * scaleY)
                    .toInt()
                    .coerceIn(0, bitmap.height - 1)



            val right =
                (box.right * scaleX)
                    .toInt()
                    .coerceIn(left + 1, bitmap.width)



            val bottom =
                (box.bottom * scaleY)
                    .toInt()
                    .coerceIn(top + 1, bitmap.height)



            println(
                "SCALED CROP: $left,$top -> $right,$bottom"
            )



            val crop =
                Bitmap.createBitmap(
                    bitmap,
                    left,
                    top,
                    right-left,
                    bottom-top
                )


            println(
                "CROP SIZE: ${crop.width}x${crop.height}"
            )


            regions.add(
                TextRegion(
                    crop,
                    box.confidence
                )
            )

        }


        return regions
    }
}