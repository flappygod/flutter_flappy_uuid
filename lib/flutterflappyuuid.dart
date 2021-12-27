import 'dart:async';

import 'package:flutter/services.dart';

//UUID
class Flutterflappyuuid {

  static const MethodChannel _channel =
      const MethodChannel('flutterflappyuuid');

  //get uuid
  static Future<String?> getUUID() async {
    final String? version = await _channel.invokeMethod('getUUID');
    return version;
  }

}
