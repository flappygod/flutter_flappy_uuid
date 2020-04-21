package com.flappygo.flutterflappyuuid;

import android.text.TextUtils;

import java.io.File;

/**
 * 创建文件夹的工具 Created by lijunlin on 2014/10/20.
 */
public class CreateDirTool {

    /************************
     * 判断路径是否存在，不存在就创建 默认创建.nomedia 防止被系统识别
     *
     * @throws Exception
     ************************/
    public static void createDir(String DIRPATH) throws Exception {

        File file = new File(DIRPATH);
        synchronized (CreateDirTool.class) {
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Exception e = new Exception(
                            "createDir failed, no dir or dirpath wrong");
                    throw e;
                }
            }
        }
        File nomidia = new File(DIRPATH + ".nomedia");
        if (!nomidia.exists()) {
            nomidia.createNewFile();
        }
    }

    /************************
     * 删除文件文件夹，注意此方法不会删除文件夹， 原因是删除文件夹在部分手机上会导致文件夹无法创建
     *
     * @param file 文件
     ***********************/
    public static boolean deleteFile(File file) {
        boolean b = true;
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return true;
            }
            for (int i = 0; i < childFiles.length; i++) {
                boolean men = deleteFile(childFiles[i]);
                // 如果它的子中有一个没有删除，那么也设置为false
                if (!men) {
                    b = false;
                }
            }
        } else {
            // 如果某一个没有删除,那么b就是false
            if (!file.delete()) {
                b = false;
            }
            file = null;
        }
        return b;
    }


    /****************
     * 判断sd卡是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else return !TextUtils.isEmpty(android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath());
    }
}
