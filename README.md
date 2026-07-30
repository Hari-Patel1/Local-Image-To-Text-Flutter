# Flutter Local OCR

A fast, fully offline OCR plugin for Flutter powered by PaddleOCR v5 and ONNX Runtime.

Extract text from images entirely on-device with no internet connection required.

---

## Features

- Fully offline
- Native Android implementation
- Powered by PaddleOCR v5
    - Detection OCRv6
    - Recognition OCRv5
- ONNX Runtime inference
- Fast image processing
- No cloud APIs
- No usage limits
- Easy Flutter API

---

## Installation

```yaml
dependencies:
  image_text_reader: ^1.0.0
```

---

## Android

No additional setup required.

---

## Usage

```dart
final result = await FlutterLocalOcr.extractText(imagePath);

print(result.text);
```

---

## Example

```dart
File image = File(path);

final result =
    await FlutterLocalOcr.extractText(image.path);

print(result.text);
```

---

## Output

```text
The quick brown fox
Jumped over the lazy dog
```

---

## Performance

Runs entirely on-device.

No internet required.

Typical processing time:

| Image | Time |
|--------|------|
| Receipt | ~150 ms |
| Recipe | ~250 ms |
| A4 document | ~400 ms |

Performance varies by device.

---

## Platform Support

| Platform | Status |
|-----------|--------|
| Android | ✅ |
| iOS | 🚧 Planned |
| Windows | 🚧 Planned |
| macOS | 🚧 Planned |
| Linux | 🚧 Planned |

---

## Roadmap

- Improved preprocessing
- Multi-language recognition
- Confidence scores
- Bounding boxes
- PDF OCR
- Batch processing

---

## Contributing

Pull requests and feature suggestions are welcome :).
Edit and use in good health and most importantly: Have fun with it!

---

## License

MIT License.
