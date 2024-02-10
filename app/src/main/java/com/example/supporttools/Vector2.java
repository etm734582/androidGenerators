package com.example.supporttools;

import java.util.Objects;

public class Vector2 {
    protected double x;
    protected double y;

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2() {

    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2 vector2 = (Vector2) obj;
        return Double.compare(vector2.x, x) == 0 &&
                Double.compare(vector2.y, y) == 0;
    }
    public int hashCode () {
        return Objects.hash(x, y);
    }


    // functions //
    public static Vector2 vectorSum(Vector2 vector1, Vector2 vector2) {
        double x1 = vector1.getX();
        double y1 = vector1.getY();
        double x2 = vector2.getX();
        double y2 = vector2.getY();

        double sumX = x1 + x2;
        double sumY = y1 + y2;

        return new Vector2(sumX, sumY);
    }
    public static double findDistantion(Vector2 position1, Vector2 position2) {
        double x1 = position1.getX();
        double y1 = position1.getY();
        double x2 = position2.getX();
        double y2 = position2.getY();

        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
    public static Vector2 getGlobalPosByRel(Vector2 ref, Vector2 rel) {
        return new Vector2(ref.getX() + rel.getX(), ref.getY() + rel.getY());
    }
    ////

    // getters //
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    ////
    // setters //
    public void setX (double x) {
        this.x = x;
    }
    public void setY (double y) {
        this.y = y;
    }
    ////
    
}
