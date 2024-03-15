
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



}
