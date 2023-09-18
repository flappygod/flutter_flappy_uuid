import 'package:flutter/services.dart';
import 'dart:async';

//UUID
class FlutterFlappyUuid {
  static const MethodChannel _channel = const MethodChannel('flutterflappyuuid');

  //get uuid
  static Future<String?> getUUID() async {
    final String? version = await _channel.invokeMethod('getUUID');
    return version;
  }
}
