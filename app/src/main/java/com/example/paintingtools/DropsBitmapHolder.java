package com.example.paintingtools;

import android.graphics.Bitmap;
import android.os.CpuUsageInfo;

public class DropsBitmapHolder {
    private static Bitmap bitmap;

    public static void setBitmap (Bitmap b) {
        bitmap = b;
    }
    public static Bitmap getBitmap () {
        return bitmap;
    }
}
