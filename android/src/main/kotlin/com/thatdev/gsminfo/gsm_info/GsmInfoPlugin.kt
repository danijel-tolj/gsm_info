package com.thatdev.gsminfo.gsm_info

import androidx.annotation.NonNull
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.app.Activity
import android.app.Application
import android.content.Context
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall

import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import io.flutter.embedding.engine.plugins.activity.*


/** GsmInfoPlugin */
class GsmInfoPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "gsm_info")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getGSMInfo") {
            getGSMInfo(result)
        } else {
            result.notImplemented()
        }
    }

    private fun getGSMInfo(@NonNull result: Result) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            result.error("no_location_permission", "ACCESS_FINE_LOCATION permission not granted",null)
        } else {
            val telephonyManager =
               activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            if (telephonyManager.allCellInfo.isEmpty()) {
                result.error("no_cell_info", "Cannot retrieve Cell Info",null)
            }
            
            val info = telephonyManager.getAllCellInfo()[0]

                if (info is CellInfoGsm) {
                    val gsm = info.cellSignalStrength
                    result.success(gsm.dbm)
                } else if (info is CellInfoCdma) {
                    val cdma = info.cellSignalStrength
                    result.success(cdma.dbm)
                } else if (info is CellInfoLte) {
                    val lte = info as CellInfoLte
                    result.success(lte.cellSignalStrength.dbm)
                } else {
                    result.error("unknown_signal_type","Unknown type of cell signal",null)
                }
            
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
