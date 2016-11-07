package com.chaney.richtextview;

/**
 * Created by Chaney on 2016/11/7.
 */

public class OnClickUtil {
    private static long lastClickTime;

    public static boolean isFastDoubleClick(long mills) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < mills) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
