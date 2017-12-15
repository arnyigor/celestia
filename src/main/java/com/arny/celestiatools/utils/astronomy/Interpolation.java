package com.arny.celestiatools.utils.astronomy;

public class Interpolation {
    public static double interpolate(double n, double y1, double y2, double y3) {
        double a = y2 - y1;
        double b = y3 - y2;
        return ((0.5d * n) * ((a + b) + (n * (b - a)))) + y2;
    }

    public static double interpolateLinear(double xn, double x1, double x2, double y1, double y2) {
        return (((xn - x1) * (y2 - y1)) / (x2 - x1)) + y1;
    }

    public static double interpolateZero(double interval, double x3, double y1, double y2, double y3, double y4, double y5) {
        double b = y3 - y2;
        double c = y4 - y3;
        double f = c - b;
        double h = f - (b - (y2 - y1));
        double j = ((y5 - y4) - c) - f;
        double k = j - h;
        double n1 = 0.0d;
        double n0;
        do {
            n0 = n1;
            n1 = ((((-24.0d * y3) + (Math.pow(n0, 2.0d) * (k - (12.0d * f)))) - ((2.0d * Math.pow(n0, 3.0d)) * (h + j))) - (Math.pow(n0, 4.0d) * k)) / (2.0d * ((((6.0d * b) + (6.0d * c)) - h) - j));
        } while (Math.abs(n0 - n1) > 1.0E-4d);
        return x3 + (n1 * interval);
    }

    public static double interpolateExtremum(double interval, double x3, double y1, double y2, double y3, double y4, double y5) {
        double b = y3 - y2;
        double c = y4 - y3;
        double f = c - b;
        double h = f - (b - (y2 - y1));
        double j = ((y5 - y4) - c) - f;
        double k = j - h;
        double n1 = 0.0d;
        double n0;
        do {
            n0 = n1;
            n1 = ((((((6.0d * b) + (6.0d * c)) - h) - j) + ((3.0d * Math.pow(n0, 2.0d)) * (h + j))) + ((2.0d * Math.pow(n0, 3.0d)) * k)) / (k - (12.0d * f));
        } while (Math.abs(n0 - n1) > 1.0E-4d);
        return x3 + (n1 * interval);
    }

    public static Coordinates3D interpolateExtremum(double interval, double x2, double y1, double y2, double y3) {
        double a = y2 - y1;
        double b = y3 - y2;
        double c = b - a;
        return new Coordinates3D(x2 - (((a + b) * interval) / (2.0d * c)), y2 - (Math.pow(a + b, 2.0d) / (8.0d * c)));
    }

    public static Coordinates3D interpolateZero(double interval, double x2, double y1, double y2, double y3) {
        double a = y2 - y1;
        double b = y3 - y2;
        double c = b - a;
        double n1 = 0.0d;
        double n0;
        do {
            n0 = n1;
            n1 = (-2.0d * y2) / ((a + b) + (c * n0));
        } while (Math.abs(n0 - n1) > 1.0E-4d);
        return new Coordinates3D(x2 + (n1 * interval), 0.0d);
    }
}
