package com.arny.celestiatools.utils.astronomy;


public class BasisCelestialObject {
    protected double alt;
    protected double az;
    protected String constellationAbbrev;
    protected double dec;
    protected float mag;
    protected String objectId = null;
    protected double ra;
    protected float x;
    protected float y;

    public double getH0() {
        return Math.toRadians(-0.5667d);
    }

    public BasisCelestialObject(String objectId) {
        initialize(objectId);
    }

    protected BasisCelestialObject(BasisCelestialObject original) {
        this.ra = original.ra;
        this.dec = original.dec;
        this.alt = original.alt;
        this.az = original.az;
        this.x = original.x;
        this.y = original.y;
        this.mag = original.mag;
        this.objectId = original.objectId;
        this.constellationAbbrev = original.constellationAbbrev;
    }

    public void initialize(String objectId) {
        this.objectId = objectId.trim();
    }

    public float getMag() {
        return this.mag;
    }

    public void setMag(float mag) {
        this.mag = mag;
    }

    public double getRA() {
        return this.ra;
    }

    public void setRA(float ra) {
        this.ra = (double) ra;
    }

    public double getDec() {
        return this.dec;
    }

    public void setDec(float dec) {
        this.dec = (double) dec;
    }

    public double getAltitude() {
        return this.alt;
    }

    public void setAltitude(float alt) {
        this.alt = (double) alt;
    }

    public double getAzimuth() {
        return this.az;
    }

    public void setAzimuth(float az) {
        this.az = (double) az;
    }

    public void setAzAlt(CoordinatesFloat3D coordAzAlt) {
        this.az = (double) coordAzAlt.getAzimuth();
        this.alt = (double) coordAzAlt.getAltitude();
    }

    public void setAzAlt(double az, double alt) {
        this.az = az;
        this.alt = alt;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getType() {
        return 0;
    }

    public short getCatalogID() {
        return (short) -1;
    }


    public byte getSpecTypeCode() {
        return (byte) 0;
    }

    public int getHR() {
        return 0;
    }

    public float getAngularRadiusMaxRad() {
        return 0.0f;
    }

    public float getAngularRadiusMinRad() {
        return 0.0f;
    }

    public void setAngularRadius(float angularRadius) {
    }

    public boolean isActive(DatePosition currentDate) {
        return false;
    }

    public float getVelocity() {
        return 0.0f;
    }

    public float getZhr() {
        return 0.0f;
    }

    public DatePosition getFromDate() {
        return null;
    }

    public DatePosition getToDate() {
        return null;
    }

    public DatePosition getMaxDate() {
        return null;
    }

    public int hashCode() {
        return (this.objectId == null ? 0 : this.objectId.hashCode()) + 31;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BasisCelestialObject other = (BasisCelestialObject) obj;
        if (this.objectId == null) {
            if (other.objectId != null) {
                return false;
            }
            return true;
        } else if (this.objectId.equals(other.objectId)) {
            return true;
        } else {
            return false;
        }
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getConstellationAbbrev() {
        return this.constellationAbbrev;
    }

    public void setConstellation(String constellation) {
        this.constellationAbbrev = constellation;
    }

    public boolean search(String key) {
        return key.equalsIgnoreCase(this.objectId);
    }


    public float getPositionAngle() {
        return 0.0f;
    }

    public float getPositionAngleDeg() {
        return 0.0f;
    }

    public String toString() {
        return "ra=" + this.ra + "dec" + this.dec + "mag=" + this.mag;
    }
}
