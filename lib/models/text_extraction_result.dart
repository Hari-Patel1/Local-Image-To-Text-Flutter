class TextExtractionResult {
  final String text;

  final double confidence;

  const TextExtractionResult({
    required this.text,
    this.confidence = 1.0,
  });

  factory TextExtractionResult.fromMap(
      Map<dynamic, dynamic> map,
      ) {
    return TextExtractionResult(
      text: map["text"] ?? "",
      confidence:
      (map["confidence"] as num?)?.toDouble() ??
          1.0,
    );
  }
}