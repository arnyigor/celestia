package com.arny.celestiatools.utils.astronomy;

import com.arny.celestiatools.utils.MathUtils;

import java.util.Calendar;

public class DatePosition {
    private double cosLat;
    private GeoLocation geoLocation;
    private boolean is24Hour = true;
    private double jd;
    private long dateTime;
    private double sinLat;

    public DatePosition() {
        this.sinLat = 0.0d;
        this.cosLat = 0.0d;
        this.geoLocation = new GeoLocation("geo",0.0f, 0.0f);
        this.is24Hour = true;
        computeSinCosLat();
    }

    public DatePosition copy() {
        return new DatePosition(this);
    }

    public DatePosition(long dateTime) {
        this.sinLat = 0.0d;
        this.cosLat = 0.0d;
        this.dateTime = dateTime;
        this.geoLocation = new GeoLocation("geo",0.0f, 0.0f);
        this.is24Hour = true;
        computeSinCosLat();
    }

    public DatePosition(DatePosition datePosition) {
        this.sinLat = datePosition.getSinLat();
        this.cosLat = datePosition.getCosLat();
        this.dateTime = datePosition.getDateTime();
        this.geoLocation = datePosition.getGeoLocation();
        this.is24Hour = true;
    }

    public DatePosition(long datetime, GeoLocation geoLocation) {
        this.sinLat = 0.0d;
        this.cosLat = 0.0d;
        this.dateTime = datetime;
        this.geoLocation = geoLocation;
        computeSinCosLat();
    }

    public DatePosition(DatePosition datePosition, GeoLocation geoLocation) {
        this.sinLat = 0.0d;
        this.cosLat = 0.0d;
        this.dateTime = datePosition.getDateTime();
        this.geoLocation = geoLocation.copy();
        computeSinCosLat();
    }

    public void set(DatePosition datePosition) {
        this.dateTime = datePosition.dateTime ;
        this.geoLocation = datePosition.geoLocation;
        this.is24Hour = datePosition.is24Hour;
        computeSinCosLat();
    }

    public GeoLocation getGeoLocation() {
        return this.geoLocation;
    }

    public GeoLocation getGeoLocationCopy() {
        return this.geoLocation.copy();
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation.copy();
        computeSinCosLat();
    }

    public boolean is24Hour() {
        return this.is24Hour;
    }

    public void set24Hours(boolean is24Hour) {
        this.is24Hour = is24Hour;
    }

    private void computeSinCosLat() {
        this.sinLat = MathUtils.Sin(this.geoLocation.getLatitudeDeg());
        this.cosLat = MathUtils.Cos(this.geoLocation.getLatitudeDeg());
    }

    public double getLatitudeDeg() {
        return this.geoLocation.getLatitudeDeg();
    }

    public double getLongitudeDeg() {
        return this.geoLocation.getLongitudeDeg();
    }

    public long getTimeInMillis() {
        return this.dateTime;
    }

    public void addJD(double d) {
        computeJD();
        this.jd += d;
        this.setDateTime(JulianDate.DateFromJD(this.jd));
    }

    public void setDateTime(long datetime) {
            this.dateTime = datetime;
    }

    public String getName() {
        return this.geoLocation.getName();
    }

    public boolean isSameLocation(DatePosition dp) {
        return dp != null && this.geoLocation.equals(dp.getGeoLocation());
    }

    public void computeJD() {
        this.jd = JulianDate.JD(this.dateTime);
    }

    public double getJD() {
        computeJD();
        return this.jd;
    }

    public void setJD(double jd) {
        this.jd = jd;
        this.dateTime = JulianDate.DateFromJD(jd);
    }

    public String toString() {
        return "DatePosition [dateTime=" + this.dateTime + "]";
    }

    public double getSinLat() {
        return this.sinLat;
    }

    public double getCosLat() {
        return this.cosLat;
    }

    public static int getDayDifference(DatePosition dp1, DatePosition dp2) {
        return (int) ((dp2.getTimeInMillis() - dp1.getTimeInMillis()) / 86400000);
    }

    public static double getJDDifference(DatePosition dp1, DatePosition dp2) {
        return ((double) (dp2.getTimeInMillis() - dp1.getTimeInMillis())) / 8.64E7d;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void add(int timetype, int amount) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(this.getTimeInMillis());
        instance.add(timetype, amount);
        this.setDateTime(instance.getTimeInMillis());
    }

    public boolean before(DatePosition datePositionEnd) {
        return datePositionEnd.getDateTime()>this.getDateTime();
    }
}
