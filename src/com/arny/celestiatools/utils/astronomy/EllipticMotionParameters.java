package com.arny.celestiatools.utils.astronomy;


import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class EllipticMotionParameters {
    public static final byte ORBIT_NON_PERIODIC = (byte) 1;
    public static final byte ORBIT_PERIODIC = (byte) 0;
    private double M0;
    private double Omega;
    private double e;
    private double epoch;
    private double i;
    private double n;
    private double omega;
    private byte orbitType = (byte) 0;
    private double q;
    private double SMA;

    public EllipticMotionParameters(double SMA, double e, double i, double omega, double omega2, double n, double epoch, double m0) {
        initialize(SMA, e, i, omega, omega2, n, (double) epoch, m0);
    }

    public void initialize(double sa, double e, double i, double omega, double omega2, double n, double epoch, double m0) {
        this.SMA = sa;
        this.e = e;
        this.i = i;
        this.omega = omega;
        this.Omega = omega2;
        this.n = n;
        this.epoch = epoch;
        this.M0 = m0;
        this.orbitType = (byte) 0;
        this.q = (1.0f - e) * sa;
    }

    public EllipticMotionParameters(String orbitType, int year, int month, double day, double q, double e, double i, double omega, double omega2, double m0) {
        Calendar mdt = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        try {
            mdt.set(year, month, (int) day);
        } catch (Exception e2) {
            mdt.add(Calendar.HOUR_OF_DAY,1);
            mdt.set(year, month, (int) day);
        }
        if (orbitType.contains("P")) {
            this.orbitType = (byte) 0;
        } else {
            this.orbitType = ORBIT_NON_PERIODIC;
        }
        this.epoch = JulianDate.getJD0UT(mdt.getTimeInMillis()) + ((double) (day - ((double) ((int) day))));
        this.q = q;
        this.SMA = q / (1.0f - e);
        this.e = e;
        this.i = i;
        this.omega = omega;
        this.Omega = omega2;
        this.n = Math.toRadians(0.9856076686d) / (((double) this.SMA) * Math.sqrt((double) this.SMA));
        this.M0 = m0;
    }

    protected EllipticMotionParameters(EllipticMotionParameters original) {
        this.SMA = original.SMA;
        this.e = original.e;
        this.i = original.i;
        this.omega = original.omega;
        this.Omega = original.Omega;
        this.n = original.n;
        this.M0 = original.M0;
        this.epoch = original.epoch;
        this.orbitType = original.orbitType;
        this.q = original.q;
    }

    public EllipticMotionParameters copy() {
        return new EllipticMotionParameters(this);
    }

    public double getSMA() {
        return this.SMA;
    }

    public double getQ() {
        return this.q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public double getE() {
        return this.e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getI() {
        return this.i;
    }

    public double getomega() {
        return this.omega;
    }

    public double getOmega() {
        return this.Omega;
    }

    public double getN() {
        return this.n;
    }

    public double getM0() {
        return this.M0;
    }

    public double getEpoch() {
        return this.epoch;
    }

    public byte getOrbitType() {
        return this.orbitType;
    }
}
