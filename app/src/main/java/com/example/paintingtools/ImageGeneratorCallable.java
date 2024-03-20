package com.example.paintingtools;

import android.graphics.Bitmap;

import java.util.BitSet;
import java.util.concurrent.Callable;

public class ImageGeneratorCallable implements Callable<Bitmap> {
    int dropsQnty;
    int dropRadius;
    int tailRadius;
    int startColor;
    int finishColor;
    int outlineColor;
    int outlineWidth;
    boolean isTextureMode;
    int width;
    int height;
    int backCol;

    public ImageGeneratorCallable(
            int dropsQnty,
            int dropRadius,
            int tailRadius,
            int startColor,
            int finishColor,
            int outlineColor,
            int outlineWidth,
            boolean isTextureMode,
            int width,
            int height,
            int backCol
    ) {
        this.dropsQnty = dropsQnty;
        this.dropRadius = dropRadius;
        this.tailRadius = tailRadius;
        this.startColor = startColor;
        this.finishColor = finishColor;
        this.outlineColor = outlineColor;
        this.outlineWidth = outlineWidth;
        this.isTextureMode = isTextureMode;
        this.width = width;
        this.height = height;
        this.backCol = backCol;
    }
    @Override
    public Bitmap call() throws Exception {
        Bitmap simple = SimpleBackground.getSimpleBackground(width, height, backCol);
        return Drops.getDropsBackground(
                simple,
                dropsQnty,
                dropRadius,
                tailRadius,
                startColor,
                finishColor,
                outlineColor,
                outlineWidth,
                true);
    }
}
