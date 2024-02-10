package com.example.paintingtools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.supporttools.PixelBrush;
import com.example.supporttools.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Drops {
    public static Bitmap getDropsBackground(
            Bitmap image,
            int dropsQnty,
            int dropRadius,
            int tailRadius,
            int startColor,
            int finishColor,
            int outlineColor,
            int outlineWidth,
            boolean isTextureMode) {
        double posYChange = (image.getHeight() / dropsQnty);
        double dropCentreY = 0;

        double RColorStepChange = (double) (Color.red(startColor) - Color.red(finishColor)) / dropsQnty;
        double GColorStepChange = (double) (Color.green(startColor) - Color.green(finishColor)) / dropsQnty;
        double BColorStepChange = (double) (Color.blue(startColor) - Color.blue(finishColor)) / dropsQnty;
        double Rcolor = (Color.red(startColor));
        double Gcolor = (Color.green(startColor));
        double Bcolor = (Color.blue(startColor));
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(outlineColor);
        paint.setStrokeWidth(outlineWidth);

        for (int i = 0; i < dropsQnty; i++) {
            paint.setStrokeWidth(outlineWidth);
            paint.setColor(outlineColor);
            dropCentreY += posYChange;
            Rcolor -= RColorStepChange;
            Gcolor -= GColorStepChange;
            Bcolor -= BColorStepChange;

            Random rnd = new Random();
            double dropCentreX = rnd.nextInt(image.getWidth());
            Vector2 dropCentre = new Vector2(dropCentreX, dropCentreY);

            double dropRadiusD = (double) dropRadius;
            double tailRadiusD = (double) tailRadius;

            double n = Math.PI / (Math.asin(1 / (2 * dropRadiusD)));
            double anglePlus = 180 - ((n - 2) * 180) / n;

            double radiusDiffFactor = dropRadiusD / (dropRadiusD + tailRadiusD);
            double currY = Math.sqrt(Math.pow(dropRadius, 2) + 2 * dropRadius * tailRadius) * radiusDiffFactor;
            double currX = tailRadius * radiusDiffFactor;
            Vector2 currPos = Vector2.getGlobalPosByRel(dropCentre, new Vector2(currX, -currY));
            Vector2 finish_orb_pos = Vector2.getGlobalPosByRel(dropCentre, new Vector2(-currX, -currY));

            PixelBrush pixelBrush = new PixelBrush();
            pixelBrush.setVector(new Vector2(currX, currY));
            double currAngle = PixelBrush.convertVectorToAngle(pixelBrush.getVector());
            pixelBrush.setAngle(currAngle);
            pixelBrush.setPosition(new Vector2(currPos.getX(), currPos.getY()));

            List<Vector2> drop = new ArrayList<>();
            drop.add(pixelBrush.getPosition());
            Vector2 tail1Start = pixelBrush.getPosition();
            double tail1Angle = currAngle - 180;

            while (1 < Vector2.findDistantion(pixelBrush.getPosition(), finish_orb_pos)) {
                pixelBrush.addAngle(anglePlus);
                pixelBrush.setVector(PixelBrush.convertAngleToVector(pixelBrush.getAngle()));
                pixelBrush.addVectorToPosition(pixelBrush.getVector());
                drop.add(pixelBrush.getPosition());
            }
            pixelBrush.addAngle(anglePlus);
            pixelBrush.setVector(PixelBrush.convertAngleToVector(pixelBrush.getAngle()));
            pixelBrush.addVectorToPosition(pixelBrush.getVector());
            drop.add(pixelBrush.getPosition());


            Vector2 tailFinishPos = Vector2.getGlobalPosByRel(new Vector2(dropCentreX, dropCentreY),
                    new Vector2(0, (-Math.sqrt(Math.pow(dropRadius, 2) + 2 * dropRadius * tailRadius))));
            n = Math.PI / (Math.asin(1 / (2 * tailRadiusD)));
            anglePlus = 180 - (((n - 2) * 180) / n);
            pixelBrush.setAngle(180 - tail1Angle);
            pixelBrush.setVector(PixelBrush.convertAngleToVector(pixelBrush.getAngle()));
            drop.add(pixelBrush.getPosition());
            drop.add(tailFinishPos);

            while (1 < Vector2.findDistantion(pixelBrush.getPosition(), tailFinishPos)) {
                pixelBrush.addAngle(-anglePlus);
                pixelBrush.setVector(PixelBrush.convertAngleToVector(pixelBrush.getAngle()));
                pixelBrush.addVectorToPosition(pixelBrush.getVector());
                drop.add(pixelBrush.getPosition());
            }



            pixelBrush.setPosition(tail1Start);
            drop.add(pixelBrush.getPosition());
            pixelBrush.setAngle(tail1Angle);
            while (1 < Vector2.findDistantion(pixelBrush.getPosition(), tailFinishPos)) {
                drop.add(pixelBrush.getPosition());
                pixelBrush.addAngle(anglePlus);
                pixelBrush.setVector(PixelBrush.convertAngleToVector(pixelBrush.getAngle()));
                pixelBrush.addVectorToPosition(pixelBrush.getVector());
            }


            Set<Vector2> borders = new HashSet<>();
            for (Vector2 j :
                    drop) {
                borders.add(new Vector2(Math.round(j.getX()), Math.round(j.getY())));
            }
            Set<Vector2> generalFilling = new HashSet<>();
            Set<Vector2> filling = new HashSet<>();
            Set<Vector2> newFilling = new HashSet<>();
            generalFilling.add(new Vector2(dropCentreX, dropCentreY));
            newFilling.add(new Vector2(dropCentreX, dropCentreY));

            while (true) {
                filling.clear();
                for (Vector2 j :
                        newFilling) {
                    filling.add(j);
                }
                newFilling.clear();
                boolean isAnythingchanged = false;

                for (Vector2 j :
                        filling) {
                    float x = Math.round(j.getX());
                    float y = Math.round(j.getY());

                    float xplus = x + 1;
                    float yplus = y + 1;
                    float xminus = x - 1;
                    float yminus = y - 1;

                    if ((!borders.contains(new Vector2(xplus, y))) &&
                            (!generalFilling.contains(new Vector2(xplus, y)))) {
                        newFilling.add(new Vector2(xplus, y));
                        generalFilling.add(new Vector2(xplus, y));
                        isAnythingchanged = true;
                    }
                    if (!borders.contains(new Vector2(xminus, y)) &&
                            (!generalFilling.contains(new Vector2(xminus, y)))) {
                        newFilling.add(new Vector2(xminus, y));
                        generalFilling.add(new Vector2(xminus, y));
                        isAnythingchanged = true;
                    }
                    if (!borders.contains(new Vector2(x, yplus)) &&
                            (!generalFilling.contains(new Vector2(x, yplus)))) {
                        newFilling.add(new Vector2(x, yplus));
                        generalFilling.add(new Vector2(x, yplus));
                        isAnythingchanged = true;
                    }
                    if (!borders.contains(new Vector2(x, yminus)) &&
                            (!generalFilling.contains(new Vector2(x, yminus)))) {
                        newFilling.add(new Vector2(x, yminus));
                        generalFilling.add(new Vector2(x, yminus));
                        isAnythingchanged = true;
                }
                    System.out.println(1);
                }
                if (!isAnythingchanged) {
                    break;
                }
            }

            paint.setStrokeWidth(1);
            paint.setColor(Color.rgb((int) Math.round(Rcolor), (int) Math.round(Gcolor), (int) Math.round(Bcolor)));
            for (Vector2 j:
                 generalFilling) {
                int x = (int) Math.round(j.getX());
                int y = (int) Math.round(j.getY());
                if (isTextureMode) {
                    while (x < 0) {
                        x = (int) image.getWidth() + x;
                    }
                    while (y < 0) {
                        y = (int) image.getHeight() + y;
                    }
                    while (x >= image.getWidth()) {
                        x = (int) x - image.getWidth();
                    }
                    while (y >= image.getHeight()) {
                        y = (int) y - image.getHeight();
                    }
                }
                else continue;
                canvas.drawPoint(x, y, paint);
            }

            paint.setStrokeWidth(outlineWidth);
            paint.setColor(outlineColor);
            for (Vector2 j:
                    drop) {
                int x = (int) Math.round(j.getX());
                int y = (int) Math.round(j.getY());
                if (isTextureMode) {
                    while (x < 0) {
                        x = (int) image.getWidth() + x;
                    }
                    while (y < 0) {
                        y = (int) image.getHeight() + y;
                    }
                    while (x >= image.getWidth()) {
                        x = (int) x - image.getWidth();
                    }
                    while (y >= image.getHeight()) {
                        y = (int) y - image.getHeight();
                    }
                } else continue;
                canvas.drawPoint(x, y, paint);
            }
        }
        return image;
    }
}
