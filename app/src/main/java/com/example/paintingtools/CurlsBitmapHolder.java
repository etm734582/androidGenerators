package com.example.paintingtools;

import android.graphics.Bitmap;

public class CurlsBitmapHolder {
    private static Bitmap bitmap;

    public static void setBitmap (Bitmap b) {
        bitmap = b;
    }
    public static Bitmap getBitmap () {
        return bitmap;
    }
}
