import 'package:flutter_test/flutter_test.dart';
import 'package:image_text_reader/image_text_reader.dart';
import 'package:image_text_reader/image_text_reader_platform_interface.dart';
import 'package:image_text_reader/image_text_reader_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockImageTextReaderPlatform
    with MockPlatformInterfaceMixin
    implements ImageTextReaderPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ImageTextReaderPlatform initialPlatform = ImageTextReaderPlatform.instance;

  test('$MethodChannelImageTextReader is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelImageTextReader>());
  });

  test('getPlatformVersion', () async {
    ImageTextReader imageTextReaderPlugin = ImageTextReader();
    MockImageTextReaderPlatform fakePlatform = MockImageTextReaderPlatform();
    ImageTextReaderPlatform.instance = fakePlatform;

    expect(await imageTextReaderPlugin.getPlatformVersion(), '42');
  });
}
