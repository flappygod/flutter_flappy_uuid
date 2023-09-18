package com.flappygo.flutterflappyuuid;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.content.Context;
import android.os.Environment;

import static android.os.Build.FINGERPRINT;

/**
 * @author xc
 * @date 2018/11/16
 * @desc
 */
public class DeviceIdUtil {


    // preference
    public final static String PREFERENCES = "com.chuangyou.flutter_chuangyou_lib.UUID";

    // uuid key
    public final static String UUID_KEY = "com.chuangyou.flutter_chuangyou_lib.UUIDKEY";


    //get device unique id 
    public static String getUniqueID(Context context) {
        //get uuid saved
        String UUID = getUUID(context);
        //if uuid is null
        if (UUID == null) {
            //get android id
            String androidID = getAndroidID(context);
            //if android id is null,generate one 
            UUID = (androidID == null ? UUIDTool.getUUID() : androidID);
            //save the uuid 
            saveUUID(context, UUID);
        }
        //return 
        return UUID;
    }


    //get android ID
    public static String getAndroidID(Context context) {
        try {
            @SuppressLint("HardwareIds")
            String ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String strOne = ANDROID_ID.toUpperCase();
            String strTwo = MD5.MD5Encode(FINGERPRINT).substring(8, 24).toUpperCase();
            String total = strOne + strTwo;
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


    //get saved uuid 
    private static String getUUID(Context context) {
        try {
            //get SharedPreferences first
            SharedPreferences mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            //if SharedPreferences is null
            String UUID = mSharedPreferences.getString(UUID_KEY, null);
            //get it from file
            if (UUID == null) {
                //file path
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath() + "/";
                //get from sdcard
                UUID = SDcardTool.readFileSdcard(path, "FlappyUI_D.sec");
                //if not null save
                if (UUID != null) {
                    saveUUID(context, UUID);
                }
            }
            return UUID;
        } catch (Exception exception) {
            return null;
        }
    }

    //save uuid
    private static void saveUUID(Context context, String uuid) {
        //create
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        //editor
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //set uuid
        editor.putString(UUID_KEY, uuid);
        //commit
        editor.apply();
        //create  path
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath() + "/";
        //save to file
        SDcardTool.writeFileSdcard(path, "FlappyUI_D.sec", uuid);
    }
}