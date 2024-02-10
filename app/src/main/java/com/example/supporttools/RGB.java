package com.example.supporttools;

import com.example.myapp.R;

public class RGB {
    protected double r;
    protected double g;
    protected double b;

    public RGB(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public RGB() {
    }

    // setters //
    public void setR (double val) {
        this.r = val;
    }
    public void setG (double val) {
        this.g = val;
    }
    public void setB (double val) {
        this.b = val;
    }
    ////
    // getters //
    public double getR () {
        return this.r;
    }
    public double getG () {
        return this.g;
    }
    public double getB () {
        return this.b;
    }
    ////
}
