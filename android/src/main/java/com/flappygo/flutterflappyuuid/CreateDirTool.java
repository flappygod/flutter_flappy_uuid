package com.flappygo.flutterflappyuuid;

import android.text.TextUtils;

import java.io.File;

public class CreateDirTool {

    //create dir
    public static void createDir(String dirPath) throws Exception {
        File file = new File(dirPath);
        synchronized (CreateDirTool.class) {
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new Exception("createDir failed, no dir or dirPath wrong");
                }
            }
        }
        File noMedia = new File(dirPath + ".nomedia");
        if (!noMedia.exists()) {
            noMedia.createNewFile();
        }
    }

    //delete file
    public static boolean deleteFile(File file) {
        boolean b = true;
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return true;
            }
            for (File childFile : childFiles) {
                boolean men = deleteFile(childFile);
                if (!men) {
                    b = false;
                }
            }
        } else {
            if (!file.delete()) {
                b = false;
            }
        }
        return b;
    }


    //is sdcard exist
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else return !TextUtils.isEmpty(android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath());
    }
}
