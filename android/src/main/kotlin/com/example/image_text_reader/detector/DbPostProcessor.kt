package com.example.image_text_reader.detector


import android.graphics.Bitmap
import com.example.image_text_reader.models.TextBox
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import android.graphics.Color
import org.opencv.android.OpenCVLoader
import kotlin.math.max
import kotlin.math.min


class DbPostProcessor {

    // How much to expand each detected box outward, as a fraction of its
    // own size. The DB probability map is trained on a *shrunk* version of
    // the real text region, so boxes taken straight from contours are
    // consistently tighter than the actual text -- this compensates for
    // that and helps stop characters (especially ascenders/descenders and
    // the first/last letter of a line) getting clipped.
    private val unclipRatio = 1.6f

    // Minimum contour area to keep -- filters out speckle/noise contours.
    private val minArea = 20.0

    // Minimum mean probability (0..1) inside a box's own contour to keep it.
    // Distinct from the binarization threshold used to build the mask --
    // this is a second, independent quality gate on top of that.
    private val minConfidence = 0.5f

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
            if(area < minArea)
                continue



            val rect =
                Imgproc.boundingRect(
                    contour
                )


            // Mean raw probability from the detector's output map, inside
            // this box's original (pre-unclip) bounds. This is the actual
            // confidence signal -- area alone says nothing about how sure
            // the model was.
            val confidence =
                meanProbability(
                    output,
                    width,
                    height,
                    rect
                )


            if(confidence < minConfidence)
                continue


            val expanded =
                unclip(
                    rect,
                    unclipRatio,
                    width,
                    height
                )


            boxes.add(

                TextBox(

                    expanded.x.toFloat(),

                    expanded.y.toFloat(),

                    (expanded.x + expanded.width).toFloat(),

                    (expanded.y + expanded.height).toFloat(),

                    confidence

                )

            )

        }


        println(
            "DB POST PROCESS BOXES: ${boxes.size}"
        )


        return boxes

    }


    /**
     * Expands a rect outward by [ratio] (fraction of its own width/height
     * added as margin on each side), clamped to stay within the map bounds.
     */
    private fun unclip(
        rect: Rect,
        ratio: Float,
        mapWidth: Int,
        mapHeight: Int
    ): Rect {

        val marginX =
            (rect.width * (ratio - 1f) / 2f)
                .toInt()

        val marginY =
            (rect.height * (ratio - 1f) / 2f)
                .toInt()

        val left =
            max(0, rect.x - marginX)

        val top =
            max(0, rect.y - marginY)

        val right =
            min(mapWidth, rect.x + rect.width + marginX)

        val bottom =
            min(mapHeight, rect.y + rect.height + marginY)

        return Rect(
            left,
            top,
            right - left,
            bottom - top
        )

    }


    /**
     * Average raw detector probability across the pixels inside [rect].
     * Used as a confidence score, independent of contour area.
     */
    private fun meanProbability(
        output: FloatArray,
        width: Int,
        height: Int,
        rect: Rect
    ): Float {

        var sum = 0f
        var count = 0

        val startY = rect.y.coerceIn(0, height - 1)
        val endY = (rect.y + rect.height).coerceIn(0, height)

        val startX = rect.x.coerceIn(0, width - 1)
        val endX = (rect.x + rect.width).coerceIn(0, width)

        for (y in startY until endY) {
            val rowOffset = y * width
            for (x in startX until endX) {
                val index = rowOffset + x
                if (index < output.size) {
                    sum += output[index]
                    count++
                }
            }
        }

        return if (count > 0) sum / count else 0f

    }

}