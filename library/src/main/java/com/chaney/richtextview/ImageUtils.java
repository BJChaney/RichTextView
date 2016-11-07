package com.chaney.richtextview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Chaney on 2016/11/7.
 */

public class ImageUtils {

    public static Bitmap resizeImage(Bitmap originalBitmap, int newWidth) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        if(width < newWidth){
            return originalBitmap;
        }
        float scaleWidth = ((float) newWidth) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        return Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);
    }

}
