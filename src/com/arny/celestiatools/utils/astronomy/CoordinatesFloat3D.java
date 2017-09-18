package com.arny.celestiatools.utils.astronomy;


public class CoordinatesFloat3D {
    public float r;
    public float x;
    public float y;

    protected CoordinatesFloat3D(CoordinatesFloat3D original) {
        this.x = original.x;
        this.y = original.y;
        this.r = original.r;
    }

    public CoordinatesFloat3D copy() {
        return new CoordinatesFloat3D(this);
    }

    public CoordinatesFloat3D() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.r = 0.0f;
    }

    public CoordinatesFloat3D(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public CoordinatesFloat3D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CoordinatesFloat3D(double x, double y, double r) {
        this.x = (float) x;
        this.y = (float) y;
        this.r = (float) r;
    }

    public CoordinatesFloat3D(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
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

    public float getZ() {
        return this.r;
    }

    public void setZ(float z) {
        this.r = z;
    }

    public void setAltitude(float y) {
        this.y = y;
    }

    public void setRotationAngle(float r) {
        this.r = r;
    }

    public void add(CoordinatesFloat3D c) {
        this.x += c.x;
        this.y += c.y;
        this.r += c.r;
    }

    public void subtract(CoordinatesFloat3D c) {
        this.x -= c.x;
        this.y -= c.y;
        this.r -= c.r;
    }

    public void add(float dx, float dy, float dr) {
        this.x += dx;
        this.y += dy;
        this.r += dr;
    }

    public void multiply(double dx, double dy, double dr) {
        this.x = (float) (((double) this.x) * dx);
        this.y = (float) (((double) this.y) * dy);
        this.r = (float) (((double) this.r) * dr);
    }

    public CoordinatesFloat3D multiply(double factor) {
        this.x = (float) (((double) this.x) * factor);
        this.y = (float) (((double) this.y) * factor);
        this.r = (float) (((double) this.r) * factor);
        return this;
    }

    public void add(double dx, double dy, double dr) {
        this.x += (float) dx;
        this.y += (float) dy;
        this.r += (float) dr;
    }

    public float getRA() {
        return this.x;
    }

    public float getDec() {
        return this.y;
    }

    public float getAzimuth() {
        return this.x;
    }

    public float getAltitude() {
        return this.y;
    }

    public float getLongitude() {
        return this.x;
    }

    public float getLatitude() {
        return this.y;
    }

    public float getDistance() {
        return this.r;
    }

    public float getRadius() {
        return this.r;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRADec(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setAzAlt(float x, float y) {
        this.x = x;
        this.y = y;
        this.r = 0.0f;
    }

    public void setAzAltRot(float x, float y, float rot) {
        this.x = x;
        this.y = y;
        this.r = rot;
    }

    public void setLonLatDist(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void set(CoordinatesFloat3D coord) {
        this.x = coord.x;
        this.y = coord.y;
        this.r = coord.r;
    }

    public CoordinatesFloat3D toDegrees() {
        return new CoordinatesFloat3D(Math.toDegrees((double) this.x), (Math.toDegrees((double) this.y)), (double) this.r);
    }

    public CoordinatesFloat3D toRadians() {
        return new CoordinatesFloat3D((Math.toRadians(this.x)), (Math.toRadians(this.y)), (double) this.r);
    }

    public void setRADecDistance(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.r = z;
    }

    public void setLonLatRad(double x, double y, double r) {
        this.x = (float) x;
        this.y = (float) y;
        this.r = (float) r;
    }

    public void setLonLatRad(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void correctRadiansAngles() {
        if (this.x < 0.0f) {
            this.x = (float) (((double) this.x) + Ephemeris.PI2);
        } else if (((double) this.x) > Ephemeris.PI2) {
            this.x = (float) (((double) this.x) - Ephemeris.PI2);
        }
    }

    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", z=" + this.r;
    }

    public void correctQuadrant() {
        double PI2 = Math.PI * 2;
        this.x %= PI2;
        if (this.x < 0.0f) {
            this.x += PI2;
        } else if (this.x >= PI2) {
            this.x -= PI2;
        }
        this.y %= PI2;
        if (this.y < -3.1415927f) {
            this.y += PI2;
        } else if (this.y > Ephemeris.PIf) {
            this.y -= PI2;
        }
    }

    public void correctAzAlt() {
        double PI2 = Math.PI * 2;
        if (this.y > Math.toRadians(90)) {
            this.x += Ephemeris.PIf;
            this.y = Ephemeris.PIf - this.y;
        } else if (this.y < -1.5707964f) {
            this.x += Ephemeris.PIf;
            this.y = -3.1415927f - this.y;
        }
        if (this.x < 0.0f) {

            this.x += PI2;
        } else if (this.x > PI2) {
            this.x -= PI2;
        }
    }
}
