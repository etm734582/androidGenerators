package com.example.paintingtools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Tiling {
    public static Bitmap getTilingBackground
            (
            Bitmap image,
            int xPlatesQnty,
            int yPlatesQnty,
            int outlineColor,
            int outlineWidth,
            int tilingBrightness
            ) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int plateSizeX = (int) imageWidth/xPlatesQnty;
        int plateSizeY = (int) imageHeight/yPlatesQnty;

        float tilingBrightness_Float = tilingBrightness / 100f;

        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStrokeWidth(2);
        final Random random = new Random();

        for (int y = 0; y < yPlatesQnty; y += 1) {
            for (int x = 0; x < xPlatesQnty; x += 1) {
                float startColR = (float) random.nextInt(255);
                float startColG = (float) random.nextInt(255);
                float startColB = (float) random.nextInt(255);
                float finishColR = (float) random.nextInt(255);
                float finishColG = (float) random.nextInt(255);
                float finishColB = (float) random.nextInt(255);

                if (tilingBrightness_Float <= 1f) {
                    startColR = startColR * tilingBrightness_Float;
                    startColG = startColG * tilingBrightness_Float;
                    startColB = startColB * tilingBrightness_Float;
                    finishColR = finishColR * tilingBrightness_Float;
                    finishColG = finishColG * tilingBrightness_Float;
                    finishColB = finishColB * tilingBrightness_Float;
                }
                else {
                    float diffStartR = 255 - startColR;
                    float diffStartG = 255 - startColG;
                    float diffStartB = 255 - startColB;
                    float diffFinishR = 255 - finishColR;
                    float diffFinishG = 255 - finishColG;
                    float diffFinishB = 255 - finishColB;

                    diffStartR = diffStartR / tilingBrightness_Float;
                    diffStartG = diffStartG / tilingBrightness_Float;
                    diffStartB = diffStartB / tilingBrightness_Float;
                    diffFinishR = diffFinishR / tilingBrightness_Float;
                    diffFinishG = diffFinishG / tilingBrightness_Float;
                    diffFinishB = diffFinishB / tilingBrightness_Float;
                    startColR = 255 - diffStartR;
                    startColG = 255 - diffStartG;
                    startColB = 255 - diffStartB;
                    finishColR = 255 - diffFinishR;
                    finishColG = 255 - diffFinishG;
                    finishColB = 255 - diffFinishB;
                }




                float rShift = ((finishColR-startColR) / plateSizeY);
                float gShift = ((finishColG-startColG) / plateSizeY);
                float bShift = ((finishColB-startColB) / plateSizeY);
                float currentR = startColR;
                float currentG = startColG;
                float currentB = startColB;
                paint.setColor(Color.rgb((int) currentR, (int) currentG, (int) currentB));

                for (int yPlate = 0; yPlate < plateSizeY; yPlate += 1) {
                    for (int xPlate = 0; xPlate < plateSizeX; xPlate += 1) {
                        canvas.drawPoint(x*plateSizeX + xPlate, y*plateSizeY + yPlate, paint);
                    }
                    currentR += rShift;
                    currentG += gShift;
                    currentB += bShift;

                    paint.setColor(Color.rgb((int) currentR, (int) currentG, (int) currentB));
                }
            }
        }
        paint.setColor(outlineColor);
        paint.setStrokeWidth(outlineWidth);

        // vertical outline

        for (int y = 0; y < yPlatesQnty; y += 1) {
            for (int x = 0; x < xPlatesQnty; x += 1) {
                for (int yPlate = 0; yPlate < plateSizeY; yPlate += 1) {
                    canvas.drawPoint(x*plateSizeX, y*plateSizeY + yPlate, paint);
                }
            }
        }

        // horisontal outline

        for (int y = 0; y < yPlatesQnty; y += 1) {
            for (int x = 0; x < xPlatesQnty; x += 1) {
                for (int xPlate = 0; xPlate < plateSizeX; xPlate += 1) {
                    canvas.drawPoint(x*plateSizeX + xPlate, y*plateSizeY, paint);
                }
            }
        }



        return image;
    }
}
