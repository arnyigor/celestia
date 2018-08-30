package com.arny.celestiatools.utils.astronomy;

public class Airmass {
    public static double getAirmass(double altitude) {
        if (altitude <= 0.0d) {
            return 0.0d;
        }
        double secz = 1.0d / Math.cos(Ephemeris.PID2 - altitude);
        if (secz < 20.0d) {
            return secz - (0.0018167d * (secz - 1.0d));
        }
        return 19.96548d;
    }

    public static float getExtinction(double altitude, double magnitude, double extinctionCoeff) {
        return (float) (magnitude - (getAirmass(altitude) * extinctionCoeff));
    }
}
