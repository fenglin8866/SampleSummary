package com.xxh.lang.java.base;

public class Utils {
    private static String TAG = "[BaseDemo]";

    public static void log(String msg) {
        System.out.println(TAG + msg);
    }

    public static void intervalTime(long startTime) {
        log("time= "+(System.currentTimeMillis()-startTime));
    }

    public static void intervalTime(String tag,long startTime) {
        log(tag+" time= "+(System.currentTimeMillis()-startTime));
    }
}
