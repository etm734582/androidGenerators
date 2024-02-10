package com.example.supporttools;

import com.example.supporttools.RGB;
import com.example.supporttools.Vector2;

public class PixelBrush {
    double angle;
    double angleShift;
    double angleShiftPlus;
    Vector2 position;
    Vector2 vector;
    RGB RGBcolor;
    RGB RGBPlus;

    public PixelBrush(double angle, Vector2 position, Vector2 vector) {
        this.angle = angle;
        this.position = position;
        this.vector = vector;
    }
    public PixelBrush(double angle, Vector2 position, Vector2 vector, double angleShift,
                      RGB color, double angleShiftPlus, RGB RGBPlus) {
        this.angle = angle;
        this.position = position;
        this.vector = vector;
        this.angleShift = angleShift;
        this.RGBcolor = color;
        this.angleShiftPlus = angleShiftPlus;
        this.RGBPlus = RGBPlus;
    }
    public PixelBrush() {
    }

    // functions //
    public static double convertVectorToAngle(Vector2 vector) {
        double angleT = Math.atan(vector.getX() / vector.getY());
        double angle = ((angleT * 180) / Math.PI);
        return angle;
    }
    public static Vector2 convertAngleToVector(double angle) {
        angle = (angle * (Math.PI/180));
        double x = Math.cos(angle);
        double y = Math.sin(angle);
        return new Vector2(x, y);
    }
    public void addVectorToPosition(Vector2 addingVector) {
        this.position = Vector2.vectorSum(position, addingVector);
    }
    public void addRGBtoColor (RGB addingRGB) {
        this.RGBcolor.r += addingRGB.r;
        this.RGBcolor.g += addingRGB.g;
        this.RGBcolor.b += addingRGB.b;
    }
    public void addAngle(double a){
        this.angle += a;
    }
    public void plusToAngleShift(double aplus) {
        this.angleShift += aplus;
    }
    ////

    // getters //
    public double getAngle() {
        return this.angle;
    }
    public Vector2 getPosition() {
        return this.position;}
    public Vector2 getVector() {
        return this.vector;
    }
    public RGB getRGBcolor() {
        return this.RGBcolor;
    }
    public double getAngleShift() {
        return this.angleShift;
    }
    public double getAngleShiftPlus () {
        return this.angleShiftPlus;
    }
    public RGB getRGBPlus () {
        return this.RGBPlus;
    }
    ////

    // setters //
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public void setVector(Vector2 vector) {
        this.vector = vector;
    }
    public void setAngleShift(double as) {
        this.angleShift = as;
    }
    public void setRGBcolor(RGB rgb) {
        this.RGBcolor = rgb;
    }
    public void setAngleShiftPlus(double a) {
        this.angleShiftPlus = a;
    }
    public void setRGBPlus(RGB rgb) {
        this.RGBPlus = rgb;
    }
    ////
}
