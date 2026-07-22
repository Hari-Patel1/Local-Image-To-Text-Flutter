class TextExtractionResult {

  final String text;
  final double confidence;


  TextExtractionResult({
    required this.text,
    required this.confidence,
  });


  factory TextExtractionResult.fromMap(
      Map<dynamic, dynamic> map
      ) {

    return TextExtractionResult(
      text: map['text'] ?? '',
      confidence: (map['confidence'] ?? 0).toDouble(),
    );

  }
}