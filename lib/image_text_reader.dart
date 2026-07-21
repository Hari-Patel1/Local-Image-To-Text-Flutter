import 'package:flutter/services.dart';

import 'models/text_extraction_result.dart';

class ImageTextReader {
  static const MethodChannel _channel =
  MethodChannel('image_text_reader');

  static Future<TextExtractionResult> extractText(
      String imagePath,
      ) async {
    final Map<dynamic, dynamic>? result =
    await _channel.invokeMethod<Map<dynamic, dynamic>>(
      'extractText',
      {
        'imagePath': imagePath,
      },
    );

    return TextExtractionResult.fromMap(result!);
  }
}