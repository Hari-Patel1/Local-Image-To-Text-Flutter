package com.example.image_text_reader

import com.example.image_text_reader.ocr.SimpleOcrEngine
import com.example.image_text_reader.models.ImageInput
import com.example.image_text_reader.processing.ImageProcessor
import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.paddle.PaddleOcrEngine

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** ImageTextReaderPlugin */
class ImageTextReaderPlugin :
    FlutterPlugin,
    MethodCallHandler {
    // The MethodChannel that will the communication between Flutter and native Android
    //
    // This local reference serves to register the plugin with the Flutter Engine and unregister it
    // when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    private val onnxEngine =
        OnnxEngine()

    private val ocrEngine =
        PaddleOcrEngine(
            onnxEngine
        )

    private val imageProcessor =
        ImageProcessor()



    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "image_text_reader")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(
        call: MethodCall,
        result: Result
    ) {

        when(call.method) {

            "extractText" -> {


                val imagePath =
                    call.argument<String>(
                        "imagePath"
                    )


                if(imagePath == null){

                    result.error(
                        "INVALID_IMAGE",
                        "Image path missing",
                        null
                    )

                    return

                }


                val image =
                    ImageInput(
                        imagePath
                    )


                val processedImage =
                    imageProcessor.process(
                        image
                    )


                val onnxReady =
                    onnxEngine.initialise()


                val text =
                    if(onnxReady){

                        "ONNX Runtime ready"

                    } else {

                        "ONNX Runtime failed"

                    }


                result.success(
                    mapOf(
                        "text" to text,
                        "confidence" to 1.0
                    )
                )
            }


            "getPlatformVersion" -> {

                result.success(
                    "Android ${android.os.Build.VERSION.RELEASE}"
                )

            }


            else -> {

                result.notImplemented()

            }

        }

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
