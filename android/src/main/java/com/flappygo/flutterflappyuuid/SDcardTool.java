/*
 * Copyright 2013 The JA-SIG Collaborative. All rights reserved.
 * distributed with this file and available online at
 * http://www.etong.com/
 */
package com.flappygo.flutterflappyuuid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.storage.StorageManager;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public class SDcardTool {



    public static void writeFileSdcard(String dirPath, String fileName, String message) {

        try {
            CreateDirTool.createDir(dirPath);
            File file = new File(dirPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(file);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
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
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


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
            String ret = new String(b);
            return ret;
        } catch (Exception e) {
            return null;
        }
    }


    public static class MyVolumeInfo {
        public String id;
        public String path;
        public String internalPath;
        public int type;
        public int state;

    }

    public static List<MyVolumeInfo> getMotedSdcard(Context context) {
        List<MyVolumeInfo> rets = new ArrayList<MyVolumeInfo>();

        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            //manager
            Class clsStorageManager = Class.forName("android.os.storage.StorageManager");
            Method method = clsStorageManager.getDeclaredMethod("getVolumes");
            //volume info
            Class clsVolumeInfo = Class.forName("android.os.storage.VolumeInfo");
            Method methodGetDisk = clsVolumeInfo.getDeclaredMethod("getDisk");
            //DiskInfo
            Class clsDiskInfo = Class.forName("android.os.storage.DiskInfo");
            Method methodIsSd = clsDiskInfo.getDeclaredMethod("isSd");
            //getVolumes
            Object array = method.invoke(mStorageManager);
            Object sdcardVolume = null;
            List listOb = (List) array;
            for (int s = 0; s < listOb.size(); s++) {
                Object volumeInfoObject = listOb.get(s);
                Object diskInfoObject = methodGetDisk.invoke(volumeInfoObject);
                /*Field[] fs = clsVolumeInfo.getDeclaredFields();
                for(int i = 0 ; i < fs.length; i++){
                    Field f = fs[i];
                    if(f.getName().equals("disk")) {
                        f.setAccessible(true); //设置些属性是可以访问的
                        Object val = f.get(volumeInfoObject);//得到此属性的值
                        sdcardVolume = val;
                    }
                }*/
                MyVolumeInfo info = new MyVolumeInfo();
                Field[] fs = clsVolumeInfo.getDeclaredFields();
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    if (f.getName().equals("id")) {
                        f.setAccessible(true); 
                        Object val = f.get(volumeInfoObject);
                        info.id = (String) val;
                    } else if (f.getName().equals("path")) {
                        f.setAccessible(true);
                        Object val = f.get(volumeInfoObject);
                        info.path = (String) val;
                    } else if (f.getName().equals("internalPath")) {
                        f.setAccessible(true); 
                        Object val = f.get(volumeInfoObject);
                        info.internalPath = (String) val;
                    } else if (f.getName().equals("type")) {
                        f.setAccessible(true); 
                        Object val = f.get(volumeInfoObject);
                        info.type = (int) val;
                    } else if (f.getName().equals("state")) {
                        f.setAccessible(true); 
                        Object val = f.get(volumeInfoObject);
                        info.state = (int) val;
                    }

                }
                rets.add(info);
            }
            return rets;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
