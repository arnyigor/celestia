package com.arny.celestiatools.utils.astronomy;

public class GeoLocation {
    public static final int ID_NOT_DEFINED = -1;
    public static final String NO_TIMEZONE_ID = "Antarctica/South_Pole";
    protected double altitude;
    protected double distance;
    protected int id;
    protected double latitude;
    protected double longitude;
    protected String name;
    protected double timeZoneOffsetHrs;

    public GeoLocation() {
        this.timeZoneOffsetHrs = -999.0f;
        this.id = ID_NOT_DEFINED;
        this.name = "-";
        this.longitude = 0.0f;
        this.latitude = 0.0f;
        this.altitude = 0.0f;
        this.distance = 0.0f;
    }

    public GeoLocation(String name, double latitude, double longitude) {
        this.timeZoneOffsetHrs = -999.0f;
        this.id = ID_NOT_DEFINED;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = 0.0f;
        this.distance = 0.0f;
    }

    public GeoLocation(int id, String name,  double longitude, double latitude) {
        this.timeZoneOffsetHrs = -999.0f;
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = 0.0f;
        this.distance = 0.0f;
    }

    public GeoLocation(int id, String name,  double longitude, double latitude, int timeZoneOffsetHrs) {
        this.timeZoneOffsetHrs = -999.0f;
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = 0.0f;
        this.distance = 0.0f;
        this.timeZoneOffsetHrs = (double) timeZoneOffsetHrs;
    }

    @Override
    public boolean equals(Object obj) {
        GeoLocation other = (GeoLocation) obj;
        return this.longitude == other.longitude && this.latitude == other.latitude && this.id == other.id;
    }

    public String getName() {
        return this.name;
    }

    public void setTimeZoneOffsetHrs(double timeZoneOffsetHrs) {
        this.timeZoneOffsetHrs = timeZoneOffsetHrs;
    }

    public GeoLocation copy() {
        GeoLocation gl = new GeoLocation(this.id, this.name,  this.longitude, this.latitude);
        gl.setTimeZoneOffsetHrs(this.timeZoneOffsetHrs);
        gl.setAltitude(this.altitude);
        gl.setDistance(this.distance);
        return gl;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitudeDeg() {
        return this.latitude;
    }

    public double getLongitudeDeg() {
        return this.longitude;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isTimeZoneAuto() {
        return this.timeZoneOffsetHrs < -12.0f;
    }

    public boolean isClose(GeoLocation myGeoLocation, double maxDistance) {
        return (40000.0d * SphericalMath.getAngularDistanceSphere(Math.toRadians(this.longitude), Math.toRadians(this.latitude), Math.toRadians((myGeoLocation.longitude)), Math.toRadians(myGeoLocation.latitude)) / Ephemeris.PI2 <= (maxDistance));
    }

    public String toString() {
        return "GeoLocation{ \'' longitude=" + this.longitude + ", latitude=" + this.latitude + ", timeZoneOffsetHrs=" + this.timeZoneOffsetHrs + ", name='" + this.name + '\'' + ", id=" + this.id + ", altitude=" + this.altitude + ", distance=" + this.distance + ", country='" + '\'' + "} " + super.toString();
    }

    public int describeContents() {
        return 0;
    }
}
