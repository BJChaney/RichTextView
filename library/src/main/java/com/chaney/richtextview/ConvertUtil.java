package com.chaney.richtextview;

import android.content.Context;

/**
 * Created by Chaney on 2016/11/6.
 */

public class ConvertUtil {

    public static int dp2px(Context context, float dp){
        final float m = context.getResources().getDisplayMetrics().density;
        return (int) (dp*m+0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
