package com.example.paintingtools;

import android.credentials.GetCredentialRequest;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.BitSet;
import java.util.concurrent.Callable;

public class TilingGeneratorCallable implements Callable<Bitmap> {
    int width;
    int height;
    int xPlatesQnty;
    int yPlatesQnty;
    int outlineColor;
    int outlineWidth;
    int tilingBrightness;

    public TilingGeneratorCallable (
            int xPlatesQnty,
            int yPlatesQnty,
            int outlineColor,
            int outlineWidth,
            int width,
            int height,
            int tilingBrightness
    ) {
        this.xPlatesQnty = xPlatesQnty;
        this.yPlatesQnty = yPlatesQnty;
        this.outlineColor = outlineColor;
        this.outlineWidth = outlineWidth;
        this.width = width;
        this.height = height;
        this.tilingBrightness = tilingBrightness;
    }
    @Override
    public Bitmap call() throws Exception {
        Bitmap simple = SimpleBackground.getSimpleBackground(width, height, Color.rgb(0, 0, 0));
        return Tiling.getTilingBackground(simple,
                xPlatesQnty,
                yPlatesQnty,
                outlineColor,
                outlineWidth,
                tilingBrightness);
    }
}
