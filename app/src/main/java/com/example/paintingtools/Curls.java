package com.example.paintingtools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.supporttools.PixelBrush;
import com.example.supporttools.RGB;
import com.example.supporttools.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Curls {
    public static Bitmap getCurlsBackground (
            Bitmap image,
            int stepQnty,
            int branchStartFromBranch,
            int startColor,
            int finishColor,
            int branchLenght,
            int angleShiftLim,
            int angleShiftPlus,
            int lineWidth,
            boolean isTextureMode,
            int treesQnty
    ) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        Set<PixelBrush> startPoints = new HashSet<>();
        Set<PixelBrush> newStartPoints = new HashSet<>();
        double asp;

        final Random random = new Random();

        for (int tree = 0; tree < treesQnty; tree++) {
            PixelBrush pixelBrush = new PixelBrush();

            int x = random.nextInt(imageWidth);
            int y = random.nextInt(imageHeight);
            int angle = random.nextInt(360);

            int Rcolor = Color.red(startColor);
            int Gcolor = Color.green(startColor);
            int Bcolor = Color.blue(startColor);
            int RFcolor = Color.red(finishColor);
            int GFcolor = Color.green(finishColor);
            int BFcolor = Color.blue(finishColor);
            double Rplus = (RFcolor - Rcolor);
            double Gplus = (GFcolor - Gcolor);
            double Bplus = (BFcolor - Bcolor);
            Rplus /= (stepQnty*branchLenght);
            Gplus /= (stepQnty*branchLenght);
            Bplus /= (stepQnty*branchLenght);

            RGB rgbPlus = new RGB(Rplus, Gplus, Bplus);

            Vector2 position = new Vector2(x, y);
            Vector2 vector = PixelBrush.convertAngleToVector(angle);

            double angleShift;
            if (angleShiftLim < 1) {
                angleShift = 0;
            }
            else {
                angleShift = random.nextInt(angleShiftLim);
                angleShift = angleShift / 100;
                int rd = random.nextInt(2);
                if (rd == 1) {
                    angleShift = -angleShift;
                }
            }

            if (angleShiftPlus >= 1) {
                if (angleShiftLim != 0) {
                    asp = (angleShift / angleShiftLim) * (angleShiftPlus / 50d);
                    int rd = random.nextInt(2);
                    if (rd == 1) {
                        asp = -asp;
                    }
                }
                else {
                    asp = 0;
                }
            }
            else {
                asp = 0d;
            }

            pixelBrush.setVector(vector);
            pixelBrush.setAngle(angle);
            pixelBrush.setPosition(position);
            pixelBrush.setRGBcolor(new RGB(Rcolor, Gcolor, Bcolor));
            pixelBrush.setAngleShift(angleShift);
            pixelBrush.setAngleShiftPlus(asp);
            pixelBrush.setRGBPlus(rgbPlus);

            newStartPoints.add(pixelBrush);
        }

        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStrokeWidth(lineWidth);


        for(int step = 0; step < stepQnty; step++){
            startPoints.clear();
            for (PixelBrush pb:
                 newStartPoints) {
                startPoints.add(new PixelBrush(pb.getAngle(),
                        pb.getPosition(),
                        pb.getVector(),
                        pb.getAngleShift(),
                        pb.getRGBcolor(),
                        pb.getAngleShiftPlus(),
                        pb.getRGBPlus()));
            }
            newStartPoints.clear();
            for (PixelBrush startPoint:
                 startPoints) {
                List<PixelBrush> currentBranch = new ArrayList<>();
                PixelBrush point = new PixelBrush();
                point.setVector(startPoint.getVector());
                point.setAngle(startPoint.getAngle());
                point.setPosition(startPoint.getPosition());
                point.setRGBcolor(startPoint.getRGBcolor());
                point.setAngleShift(startPoint.getAngleShift());
                point.setAngleShiftPlus(startPoint.getAngleShiftPlus());
                point.setRGBPlus(startPoint.getRGBPlus());

                for (int cntr = 0; cntr < branchLenght; cntr++) {
                    point.plusToAngleShift(point.getAngleShiftPlus());
                    point.addVectorToPosition(point.getVector());
                    point.addRGBtoColor(point.getRGBPlus());
                    point.addAngle(point.getAngleShift());
                    point.setVector(PixelBrush.convertAngleToVector(point.getAngle()));



                    currentBranch.add(new PixelBrush(point.getAngle(),
                            new Vector2(point.getPosition().getX(), point.getPosition().getY()),
                            new Vector2(point.getVector().getX(), point.getVector().getY()),
                            point.getAngleShift(),
                            new RGB(point.getRGBcolor().getR(), point.getRGBcolor().getG(), point.getRGBcolor().getB()),
                            point.getAngleShiftPlus(),
                            new RGB(point.getRGBPlus().getR(), point.getRGBPlus().getG(), point.getRGBPlus().getB())));

//                    int rm = random.nextInt(branchLenght/branchStartFromBranch);
//                    if (rm == 1) {
//                    }
                }

                for (PixelBrush pb:
                     currentBranch) {
                    RGB col = pb.getRGBcolor();
                    int r = (int) (Math.round(col.getR()));
                    int g = (int) (Math.round(col.getG()));
                    int b = (int) (Math.round(col.getB()));
                    paint.setColor(Color.rgb(r, g, b));
                    int x_cord = (int) Math.round(pb.getPosition().getX());
                    int y_cord = (int) Math.round(pb.getPosition().getY());
                    if (isTextureMode) {
                        while (x_cord < 0) {
                            x_cord = (int) image.getWidth() + x_cord;
                        }
                        while (y_cord < 0) {
                            y_cord = (int) image.getHeight() + y_cord;
                        }
                        while (x_cord >= image.getWidth()) {
                            x_cord = (int) x_cord - image.getWidth();
                        }
                        while (y_cord >= image.getHeight()) {
                            y_cord = (int) y_cord - image.getHeight();
                        }
                    }
                    else continue;
                    canvas.drawPoint(x_cord, y_cord, paint);
                }

                for (int i = 0; i < branchStartFromBranch; i++) {
                    int idx = random.nextInt(branchLenght);
                    PixelBrush pointToAdd = currentBranch.get(idx);

                    PixelBrush addingStartPoint = new PixelBrush();
                    double ashift;
                    int rn;
                    if (angleShiftLim > 0) {
                        ashift = random.nextInt(angleShiftLim);
                        ashift = ashift / 100;
                        rn = random.nextInt(2);
                        if (rn == 1) {
                            ashift = -ashift;
                        }
                    }
                    else {
                        ashift = 0;
                    }
                    if (angleShiftLim > 0) {
                        asp = (ashift / angleShiftLim) * (angleShiftPlus / 50d);
                        rn = random.nextInt(2);
                        if (rn == 1) {
                            asp = -asp;
                        }
                    }
                    else {
                        asp = 0;
                    }

                    addingStartPoint.setVector(pointToAdd.getVector());
                    addingStartPoint.setAngle(pointToAdd.getAngle());
                    addingStartPoint.setPosition(pointToAdd.getPosition());
                    addingStartPoint.setRGBcolor(pointToAdd.getRGBcolor());
                    addingStartPoint.setAngleShift(ashift);
                    addingStartPoint.setAngleShiftPlus(asp);
                    addingStartPoint.setRGBPlus(pointToAdd.getRGBPlus());

                    newStartPoints.add(addingStartPoint);
                }
            }

        }



        return image;
    }
}
