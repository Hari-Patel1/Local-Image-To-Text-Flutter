package com.example.image_text_reader

import com.example.image_text_reader.ml.OnnxEngine
import com.example.image_text_reader.models.ImageInput
import com.example.image_text_reader.paddle.PaddleOcrEngine
import com.example.image_text_reader.processing.ImageProcessor

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageTextReaderPlugin :
    FlutterPlugin,
    MethodCallHandler {

    private val pluginScope =
        CoroutineScope(
            Dispatchers.Main + SupervisorJob()
        )

    private lateinit var channel: MethodChannel

    private lateinit var onnxEngine: OnnxEngine

    private lateinit var ocrEngine: PaddleOcrEngine

    private val imageProcessor =
        ImageProcessor()

    override fun onAttachedToEngine(
        binding: FlutterPlugin.FlutterPluginBinding
    ) {

        channel = MethodChannel(
            binding.binaryMessenger,
            "image_text_reader"
        )

        onnxEngine =
            OnnxEngine(
                binding.applicationContext
            )

        onnxEngine.loadModel(
            "detector",
            "models/det.onnx"
        )

        onnxEngine.loadModel(
            "recogniser",
            "models/rec.onnx"
        )

        ocrEngine =
            PaddleOcrEngine(
                binding.applicationContext,
                onnxEngine
            )

        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(
        call: MethodCall,
        result: Result
    ) {

        when (call.method) {

            "extractText" -> {

                val imagePath =
                    call.argument<String>("imagePath")

                if (imagePath == null) {

                    result.error(
                        "INVALID_IMAGE",
                        "Image path missing",
                        null
                    )

                    return
                }

                pluginScope.launch {

                    try {

                        val ocrResult =
                            withContext(Dispatchers.IO) {

                                val image =
                                    ImageInput(imagePath)

                                val processedImage =
                                    imageProcessor.process(
                                        image
                                    )

                                ocrEngine.extractText(
                                    processedImage
                                )

                            }

                        println("SENDING RESULT TO FLUTTER")

                        result.success(
                            mapOf(
                                "text" to ocrResult.text,
                                "confidence" to ocrResult.confidence
                            )
                        )

                    } catch (e: Exception) {

                        e.printStackTrace()

                        result.error(
                            "OCR_FAILED",
                            e.stackTraceToString(),
                            null
                        )

                    }

                }

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

    override fun onDetachedFromEngine(
        binding: FlutterPlugin.FlutterPluginBinding
    ) {

        channel.setMethodCallHandler(null)

        pluginScope.cancel()

        // If your OnnxEngine has a cleanup method,
        // call it here:
        //
        // onnxEngine.close()

    }

}