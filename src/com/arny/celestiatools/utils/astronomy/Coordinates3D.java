package com.arny.celestiatools.utils.astronomy;

public class Coordinates3D {
    public double r;
    public double x;
    public double y;

    protected Coordinates3D(Coordinates3D original) {
        this.x = original.x;
        this.y = original.y;
        this.r = original.r;
    }

    public Coordinates3D copy() {
        return new Coordinates3D(this);
    }

    public Coordinates3D() {
        this.x = 0.0d;
        this.y = 0.0d;
        this.r = 0.0d;
    }

    public Coordinates3D(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Coordinates3D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.r;
    }

    public void setZ(double r) {
        this.r = r;
    }

    public double getDistance() {
        return this.r;
    }

    public void setRotationAngle(float r) {
        this.r = (double) r;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates3D that = (Coordinates3D) o;
        return Double.compare(that.r, this.r) == 0 && Double.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = (int) ((temp >>> 32) ^ temp);
        temp = Double.doubleToLongBits(this.y);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.r);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public Coordinates3D toRadians() {
        return new Coordinates3D(Math.toRadians(this.x), Math.toRadians(this.y), this.r);
    }

    public Coordinates3D toDegrees() {
        return new Coordinates3D(Math.toDegrees(this.x), Math.toDegrees(this.y), this.r);
    }

    public void add(double dx, double dy, double dr) {
        this.x += dx;
        this.y += dy;
        this.r += dr;
    }

    public void subtract(Coordinates3D c) {
        this.x -= c.x;
        this.y -= c.y;
        this.r -= c.r;
    }

    public double getRA() {
        return this.x;
    }

    public double getDec() {
        return this.y;
    }

    public double getAzimuth() {
        return this.x;
    }

    public double getAltitude() {
        return this.y;
    }

    public double getLongitude() {
        return this.x;
    }

    public double getLatitude() {
        return this.y;
    }

    public double getRadius() {
        return this.r;
    }

    public void setRADec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setRADecDistance(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.r = z;
    }

    public void setAzAlt(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setLonLatDist(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void setAzAltRot(double x, double y, double rot) {
        this.x = x;
        this.y = y;
        this.r = rot;
    }

    public void correctRadiansAngles() {
        if (this.x < 0.0d) {
            this.x += Ephemeris.PI2;
        } else if (this.x > Ephemeris.PI2) {
            this.x -= Ephemeris.PI2;
        }
    }

    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", z=" + this.r;
    }

    public void correctQuadrant() {
        this.x %= Ephemeris.PI2;
        if (this.x < 0.0d) {
            this.x += Ephemeris.PI2;
        } else if (this.x >= Ephemeris.PI2) {
            this.x -= Ephemeris.PI2;
        }
        this.y %= Ephemeris.PI2;
        if (this.y < -3.141592653589793d) {
            this.y += Ephemeris.PI2;
        } else if (this.y > Ephemeris.PI) {
            this.y -= Ephemeris.PI2;
        }
    }

    public void normalize() {
        double length = Math.sqrt(((this.x * this.x) + (this.y * this.y)) + (this.r * this.r));
        if (length > 0.0d) {
            this.x /= length;
            this.y /= length;
            this.r /= length;
        }
    }

    public void correctAzAlt() {
        if (this.y > 1.5707963705062866d) {
            this.x += 3.1415927410125732d;
            this.y = 3.1415927410125732d - this.y;
        } else if (this.y < -1.5707963705062866d) {
            this.x += 3.1415927410125732d;
            this.y = -3.1415927410125732d - this.y;
        }
        if (this.x < 0.0d) {
            this.x += 6.2831854820251465d;
        } else if (this.x > 6.2831854820251465d) {
            this.x -= 6.2831854820251465d;
        }
    }
}
