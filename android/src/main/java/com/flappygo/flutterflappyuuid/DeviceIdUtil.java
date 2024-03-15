package com.flappygo.flutterflappyuuid;

import static android.os.Build.FINGERPRINT;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.content.Context;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.util.UUID;

/*******
 * device id tool
 */
public class DeviceIdUtil {

    // preference
    public final static String PREFERENCES = "com.chuangyou.flutter_chuangyou_lib.UUID";

    // uuid key
    public final static String UUID_KEY = "com.chuangyou.flutter_chuangyou_lib.UUIDKEY";

    //get device unique id 
    public static String getUniqueID(Activity context) {
        //get android id
        String androidID = getAndroidID(context);
        //return 
        return (androidID == null ? getUUID(context) : androidID);
    }

    //generate uuid
    public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }


    //get android ID
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        try {
            String ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String FINGERPRINT_MD5 = MD5.MD5Encode(FINGERPRINT);
            String FINGERPRINT_ID = FINGERPRINT_MD5.substring(8).toUpperCase();
            String total = ANDROID_ID.toUpperCase() + FINGERPRINT_ID;
            if (total.length() >= 32) {
                total = total.substring(0, 32);
            }
            if (total.length() == 32) {
                String one = total.substring(0, 8);
                String two = total.substring(8, 12);
                String three = total.substring(12, 16);
                String four = total.substring(16, 20);
                String five = total.substring(20, 32);
                return one + "-" + two + "-" + three + "-" + four + "-" + five;
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }


    ///get UUID from SharedPreferences
    private static String getUUIDFromSharedPreferences(Activity activity) {
        SharedPreferences mSharedPreferences = activity.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(UUID_KEY, null);
    }

    //get UUID from External Directory
    private static String getUUIDFromDirectories(Activity activity) {
        String path;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/";
        } else {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath() + "/";
        }
        return SDCardTool.readFileSdcard(path, "FlappyUI_D.sec");
    }

    //save uuid
    private static void saveUUIDSharedPreferences(Context context, String uuid) {
        //create
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        //editor
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //set uuid
        editor.putString(UUID_KEY, uuid);
        //commit
        editor.apply();
    }


    //save uuid
    private static void saveUUIDExternalStorage(Context context, String uuid) {
        //create
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        //editor
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //set uuid
        editor.putString(UUID_KEY, uuid);
        //commit
        editor.apply();
    }


    //save uuid
    public static void refreshUUIDToDirectories(Activity context) {
        String path;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/";
        } else {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath() + "/";
        }
        //save to file
        SDCardTool.writeFileSdcard(path, "FlappyUI_D.sec", getUUIDFromSharedPreferences(context));
    }


    //get saved uuid 
    private static String getUUID(Activity activity) {
        try {
            //get from SharedPreferences
            String oneStep = getUUIDFromSharedPreferences(activity);
            if (oneStep != null) {
                return oneStep;
            }
            //get from External Directory
            String stepTwo = getUUIDFromDirectories(activity);
            if (stepTwo != null) {
                saveUUIDSharedPreferences(activity, stepTwo);
                return stepTwo;
            }
            String stepThree = generateUUID();
            saveUUIDSharedPreferences(activity, stepThree);
            saveUUIDExternalStorage(activity, stepThree);
            verifyStoragePermissions(activity);
            return stepThree;
        } catch (Exception exception) {
            return null;
        }
    }


    public static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int two = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int three = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (two != PackageManager.PERMISSION_GRANTED ||
                three != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}