package com.example.paintingtools;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class SimpleBackground {
    public static Bitmap getSimpleBackground(int width, int height, int color){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);


        return bitmap;
    }
}
