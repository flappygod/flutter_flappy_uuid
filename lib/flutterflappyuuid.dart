import 'package:flutter/services.dart';
import 'dart:async';

/// Get UUID for this App, IOS use keychain and android use ANDROID_ID and FINGERPRINT .
class FlutterFlappyUuid {
  ///channel
  static const MethodChannel _channel =
      const MethodChannel('flutterflappyuuid');

  /// Get UUID for this App, IOS use keychain and android use ANDROID_ID and FINGERPRINT .
  static Future<String?> getUUID() async {
    final String? version = await _channel.invokeMethod('getUUID');
    return version;
  }
}
