package com.arny.celestiatools.utils;

public class AstroAngle {
    private double grad;
    private double min;
    private double sec;
    private int sign = 1;

    public AstroAngle() {
    }

    public AstroAngle(double grad) {
        this.grad = grad;
        sign = grad < 0 ? -1 : 1;
    }

    public AstroAngle(double grad, double min, double sec) {
        this.grad = grad;
        this.min = min;
        this.sec = sec;
        sign = grad < 0 ? -1 : 1;
    }

    private void normalize360() {
        while (grad >= 360) {
            grad -= 360;
        }
    }

    public void expand() {
        normalize360();
        int intact = MathUtils.intact(grad);
        min = MathUtils.intact(((grad / 60) - intact) * 60);
        sec = (((((grad / 60) - intact) * 60) - min) * 60);
        grad = intact;
    }

    public void collaps() {
        normalize360();
        grad = grad + (min * 60) + (sec * 3600);
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

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
