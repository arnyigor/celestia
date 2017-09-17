package com.arny.celestiatools.utils.astronomy;

public class RiseSetEvent {
    public static final int ALWAYS_ABOVE_HORIZON = 1;
    public static final int ALWAYS_BELOW_HORIZON = 2;
    public static final int NA = 99;
    public static final int NORMAL = 0;
    public static final int RISE = 0;
    public static final int SET = 2;
    public static final float TIME_ALWAYS_ABOVE_HORIZON = -99.0f;
    public static final float TIME_ALWAYS_BELOW_HORIZON = -100.0f;
    public static final int TRANSIT = 1;
    double altitude;
    double azimuth;
    int riseSetType;
    double time;
    int type;

    public RiseSetEvent(double time, double azimuth, double altitude, int type, int riseSetType) {
        this.time = time;
        this.azimuth = azimuth;
        this.altitude = altitude;
        this.type = type;
        this.riseSetType = riseSetType;
    }

    public RiseSetEvent copy() {
        return new RiseSetEvent(this.time, this.azimuth, this.altitude, this.type, this.riseSetType);
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void addTime(double dTime) {
        this.time += dTime;
    }

    public double getAzimuth() {
        return this.azimuth;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public int getType() {
        return this.type;
    }

    public boolean isAlwaysAboveHorizon() {
        return this.riseSetType == TRANSIT;
    }

    public boolean isAlwaysBelowHorizon() {
        return this.riseSetType == SET;
    }

    public boolean notVisible() {
        return this.time == Ephemeris.notVisible;
    }

    public boolean isVisible() {
        return this.time != Ephemeris.notVisible;
    }

    public int getRiseSetType() {
        return this.riseSetType;
    }
}
