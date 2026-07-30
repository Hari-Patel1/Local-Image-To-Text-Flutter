package com.example.image_text_reader.detector


import android.graphics.Bitmap
import com.example.image_text_reader.models.TextBox
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import android.graphics.Color
import org.opencv.android.OpenCVLoader



class DbPostProcessor {
    init {
        if (!OpenCVLoader.initDebug()) {
            println("OpenCV FAILED TO LOAD")
        } else {
            println("OpenCV LOADED")
        }
    }



        fun process(
        output: FloatArray,
        width: Int,
        height: Int
    ): List<TextBox> {


        val boxes =
            mutableListOf<TextBox>()


        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )


        val pixels =
            IntArray(width * height)



        val limit =
            minOf(
                output.size,
                pixels.size
            )


        for(i in 0 until limit){

            val value =
                if(output[i] > 0.3f)
                    255
                else
                    0


            pixels[i] =
                Color.rgb(
                    value,
                    value,
                    value
                )

        }


        bitmap.setPixels(
            pixels,
            0,
            width,
            0,
            0,
            width,
            height
        )


        val mat =
            Mat()


        Utils.bitmapToMat(
            bitmap,
            mat
        )

        Imgproc.cvtColor(
            mat,
            mat,
            Imgproc.COLOR_RGBA2GRAY
        )


        val contours =
            mutableListOf<MatOfPoint>()



        Imgproc.findContours(
            mat,
            contours,
            Mat(),
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )


        for(contour in contours){


            val area =
                Imgproc.contourArea(contour)


            // ignore tiny noise
            if(area < 20)
                continue



            val rect =
                Imgproc.boundingRect(
                    contour
                )


            boxes.add(

                TextBox(

                    rect.x.toFloat(),

                    rect.y.toFloat(),

                    (rect.x + rect.width).toFloat(),

                    (rect.y + rect.height).toFloat(),

                    area.toFloat()

                )

            )

        }


        println(
            "DB POST PROCESS BOXES: ${boxes.size}"
        )


        return boxes

    }

}