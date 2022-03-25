import 'dart:async';

import 'package:flutter/services.dart';

class GsmInfo {
  static const MethodChannel _channel = MethodChannel('gsm_info');

  static Future<int> get gsmSignalDbM async {
    final int version = await _channel.invokeMethod('getGSMInfo');
    return version;
  }
}
