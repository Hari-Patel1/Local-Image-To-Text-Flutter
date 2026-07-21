import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'image_text_reader_method_channel.dart';

abstract class ImageTextReaderPlatform extends PlatformInterface {
  /// Constructs a ImageTextReaderPlatform.
  ImageTextReaderPlatform() : super(token: _token);

  static final Object _token = Object();

  static ImageTextReaderPlatform _instance = MethodChannelImageTextReader();

  /// The default instance of [ImageTextReaderPlatform] to use.
  ///
  /// Defaults to [MethodChannelImageTextReader].
  static ImageTextReaderPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ImageTextReaderPlatform] when
  /// they register themselves.
  static set instance(ImageTextReaderPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
