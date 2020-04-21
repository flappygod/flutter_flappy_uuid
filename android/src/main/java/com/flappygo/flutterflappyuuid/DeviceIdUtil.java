package com.flappygo.flutterflappyuuid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.Settings;

import static android.os.Build.FINGERPRINT;

/**
 * @author xc
 * @date 2018/11/16
 * @desc
 */
public class DeviceIdUtil {


    // 首选项名称
    public final static String PREFERENCENAME = "com.chuangyou.flutter_chuangyou_lib.UUID";

    // 当前保存的店铺ID
    public final static String UUID_KEY = "com.chuangyou.flutter_chuangyou_lib.UUIDKEY";


    public static String getDeviceUUID(Context context) {
        try {
            //首先获取androidID
            String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String fingerprint = FINGERPRINT;
            //获取到十六位的androidID
            if (ANDROID_ID != null && ANDROID_ID.length() == 16 && fingerprint != null) {
                String strOne = ANDROID_ID.toUpperCase();
                String strTwo = MD5.MD5Encode(fingerprint).substring(8, 24).toUpperCase();
                String total = strOne + strTwo;
                if (total.length() == 32) {
                    String one = total.substring(0, 8);
                    String two = total.substring(8, 12);
                    String three = total.substring(12, 16);
                    String four = total.substring(16, 20);
                    String five = total.substring(20, 32);
                    String ret = one + "-" + two + "-" + three + "-" + four + "-" + five;
                    return ret;
                }
            }
        } catch (Exception ex) {

        }
        return getUnicID(context);
    }


    /*********
     * 获取唯一ID
     * @return
     */
    private static String getUnicID(Context context) {
        //获取UUID
        String UUID = getUUID(context);
        //假如保存的UUID不存在
        if (UUID == null) {
            //创建新的UUID
            UUID = UUIDTool.getUUID();
            //保存UUID
            saveUUID(context, UUID);
        }
        //平板ID
        return UUID;
    }

    /******************
     * 获取平板的唯一标识码
     * @return
     * @param context
     */
    private static String getUUID(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        //获取到设置信息
        String UUID = mSharedPreferences.getString(UUID_KEY, null);
        //获取文件中的
        if (UUID == null) {
            //外部地址
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath() + "/";
            //读取外部地址
            UUID = SDcardTool.readFileSdcard(path, "UUID.txt");
            //如果不为空则保存
            if (UUID != null) {
                saveUUID(context, UUID);
            }
        }
        return UUID;
    }

    /*****************
     * 保存平板的唯一标识码
     * @param uuid  uuid
     * @return
     */
    private static void saveUUID(Context context, String uuid) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath() + "/";
        //向文件夹写入文件数据
        SDcardTool.writeFileSdcard(path, "UUID.txt", uuid);
        //创建首选项
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        //创建editor
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //设置字符串
        editor.putString(UUID_KEY, uuid);
        //提交修改
        editor.commit();
    }
}