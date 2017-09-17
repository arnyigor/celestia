package com.arny.celestiatools.utils.astronomy;

public class Ecliptic {
    private static double lastJD = -999.0d;
    private static double obliquity = 0.0d;

    public static double getObliquity(double jd) {
        if (jd == lastJD) {
            return obliquity;
        }
        double T = Ephemeris.getCenturiesSince1900(jd);
        obliquity = Math.toRadians((((23.452294d - (0.0130125d * T)) - ((1.64E-6d * T) * T)) + (((5.03E-7d * T) * T) * T)));
        lastJD = jd;
        return obliquity;
    }
}
