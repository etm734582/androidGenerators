package com.example.paintingtools;

import android.graphics.Bitmap;

import java.util.concurrent.Callable;

public class CurlsGeneratorCallable implements Callable<Bitmap> {
    int stepQnty;
    int branchStartFromBranch;
    int startColor;
    int finishColor;
    int branchLenght;
    int angleShiftLim;
    int angleShiftPlus;
    int lineWidth;
    int backCol;
    int width;
    int height;
    boolean isTextureMode;
    int treesQnty;

    public CurlsGeneratorCallable (
            int stepQnty,
            int branchStartFromBranch,
            int startColor,
            int finishColor,
            int branchLenght,
            int angleShiftLim,
            int angleShiftPlus,
            int lineWidth,
            boolean isTextureMode,
            int backCol,
            int width,
            int height,
            int treesQnty
    ) {
        this.stepQnty = stepQnty;
        this.branchStartFromBranch = branchStartFromBranch;
        this.startColor = startColor;
        this.finishColor = finishColor;
        this.branchLenght = branchLenght;
        this.angleShiftLim = angleShiftLim;
        this.angleShiftPlus = angleShiftPlus;
        this.lineWidth = lineWidth;
        this.isTextureMode = isTextureMode;
        this.backCol = backCol;
        this.width = width;
        this.height = height;
        this.treesQnty = treesQnty;
    }

    @Override
    public Bitmap call() throws Exception {
        Bitmap simple = SimpleBackground.getSimpleBackground(width, height, backCol);
        return Curls.getCurlsBackground(
                simple,
                stepQnty,
                branchStartFromBranch,
                startColor,
                finishColor,
                branchLenght,
                angleShiftLim,
                angleShiftPlus,
                lineWidth,
                true,
                treesQnty
        );
    }
}
