package com.arny.celestiatools.utils;

public class GradMinSec {
    private double grad;
    private double min;
    private double sec;

    public GradMinSec(double grad, double min, double sec) {
        this.grad = grad;
        this.min = min;
        this.sec = sec;
    }

    public GradMinSec(double grad) {
        this.grad = grad;
    }

    public GradMinSec() {
    }

    public double getGrad() {
        return grad;
    }

    public void setGrad(double grad) {
        this.grad = grad;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getSec() {
        return sec;
    }

    public void setSec(double sec) {
        this.sec = sec;
    }
}
