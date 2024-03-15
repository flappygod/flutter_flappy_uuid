package com.flappygo.flutterflappyuuid;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodCall;

/**
 * FlutterFlappyUuidPlugin
 */
public class FlutterflappyuuidPlugin implements FlutterPlugin,
        MethodCallHandler,
        ActivityAware,
        PluginRegistry.ActivityResultListener,
        PluginRegistry.RequestPermissionsResultListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    //activity
    private Activity activity;

    //binding
    private ActivityPluginBinding pluginBinding;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_flappy_uuid");
        channel.setMethodCallHandler(this);
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        activity = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        pluginBinding = binding;
        activity = binding.getActivity();
        pluginBinding.addActivityResultListener(this);
        pluginBinding.addRequestPermissionsResultListener(this);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        pluginBinding = binding;
        activity = binding.getActivity();
        pluginBinding.addActivityResultListener(this);
        pluginBinding.addRequestPermissionsResultListener(this);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        pluginBinding.removeActivityResultListener(this);
        pluginBinding.removeRequestPermissionsResultListener(this);
    }

    @Override
    public void onDetachedFromActivity() {
        pluginBinding.removeActivityResultListener(this);
        pluginBinding.removeRequestPermissionsResultListener(this);
        pluginBinding = null;
        activity = null;
    }


    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        FlutterflappyuuidPlugin plugin = new FlutterflappyuuidPlugin();
        //set channel
        plugin.channel = new MethodChannel(registrar.messenger(), "flutterflappyuuid");
        //set handler
        plugin.channel.setMethodCallHandler(plugin);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getUUID")) {
            result.success(DeviceIdUtil.getUniqueID(activity));
        } else {
            result.notImplemented();
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == DeviceIdUtil.REQUEST_EXTERNAL_STORAGE) {
            DeviceIdUtil.refreshUUIDToDirectories(activity);
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        return false;
    }
}
