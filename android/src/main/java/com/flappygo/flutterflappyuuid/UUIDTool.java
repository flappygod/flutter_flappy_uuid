package com.flappygo.flutterflappyuuid;

import java.util.UUID;

public class UUIDTool {


    /******************
     * 获取平板的唯一标识
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }



}
