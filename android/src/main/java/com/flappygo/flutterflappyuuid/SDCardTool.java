
package com.flappygo.flutterflappyuuid;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class SDCardTool {


    /******
     * write String to sd card
     * @param dirPath  路径
     * @param fileName 名称
     * @param message  信息
     */
    public static void writeFileSdcard(String dirPath, String fileName, String message) {
        try {
            CreateDirTool.createDir(dirPath);
            File file = new File(dirPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(file);
            byte[] bytes = message.getBytes();
            fOut.write(bytes);
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*********
     * read string sd card
     * @param dirPath  路径
     * @param fileName 名称
     * @return
     */
    public static String readFileSdcard(String dirPath, String fileName) {
        try {
            File file = new File(dirPath + fileName);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fin = new FileInputStream(file);
            byte[] b = new byte[fin.available()];
            fin.read(b);
            fin.close();
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int one = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE);
        int two = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int three = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (one != PackageManager.PERMISSION_GRANTED ||
                two != PackageManager.PERMISSION_GRANTED ||
                three != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


}
