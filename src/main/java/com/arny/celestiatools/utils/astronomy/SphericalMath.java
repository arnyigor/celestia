package com.arny.celestiatools.utils.astronomy;


public class SphericalMath {
    public static double getAngularDistanceSphere(double fi1, double theta1, double fi2, double theta2) {
        return Math.acos(Math.min(Math.max((Math.sin(theta1) * Math.sin(theta2)) + ((Math.cos(theta1) * Math.cos(theta2)) * Math.cos(fi1 - fi2)), -1.0d), 1.0d));
    }

    public static double getAngularDistanceSphere(Coordinates3D c1, Coordinates3D c2) {
        return Math.acos(Math.min(Math.max((Math.sin(c1.getDec()) * Math.sin(c2.getDec())) + ((Math.cos(c1.getDec()) * Math.cos(c2.getDec())) * Math.cos(c1.getRA() - c2.getRA())), -1.0d), 1.0d));
    }

    public static double getAngularDistanceSphereSmallAngle(Coordinates3D c1, Coordinates3D c2) {
        double cd1 = Math.cos(c1.getDec());
        double cd2 = Math.cos(c2.getDec());
        double sd1 = Math.sin(c1.getDec());
        double sd2 = Math.sin(c2.getDec());
        double a2ma1 = c2.getRA() - c1.getRA();
        double x = (cd1 * sd2) - ((sd1 * cd2) * Math.cos(a2ma1));
        double y = cd2 * Math.sin(a2ma1);
        return Math.atan2(Math.sqrt((x * x) + (y * y)), (sd1 * sd2) + ((cd1 * cd2) * Math.cos(a2ma1)));
    }

    public static double getAngularDistanceSphere0(Coordinates3D c1, Coordinates3D c2) {
        return Math.acos(Math.cos(c1.getRA() - c2.getRA()));
    }

    public static double getDistanceOnEarth_km(float fi1, float theta1, float fi2, float theta2) {
        return getAngularDistanceSphere((double) fi1, (double) theta1, (double) fi2, (double) theta2) * ((double)6371.009);
    }

    public static double getDistanceSqr(double x1, double y1, double x2, double y2) {
        return Math.pow(x2 - x1, 2.0d) + Math.pow(y2 - y1, 2.0d);
    }

    public static double getPositionAngleSphere(Coordinates3D c1, Coordinates3D c2) {
        return getPositionAngleSphere(c1.getRA(), c1.getDec(), c2.getRA(), c2.getDec());
    }

    public static double getPositionAngleSphere(double ra1, double dec1, double ra2, double dec2) {
        double angle = Math.atan2(Math.cos(dec2) * Math.sin(ra2 - ra1), (Math.cos(dec1) * Math.sin(dec2)) - ((Math.sin(dec1) * Math.cos(dec2)) * Math.cos(ra2 - ra1)));
        if (angle < 0.0d) {
            return angle + Ephemeris.PI2;
        }
        return angle;
    }

    public static CoordinatesFloat3D getCarthToSpherical(float x, float y, float z) {
        double radius = Math.sqrt((double) (((x * x) + (y * y)) + (z * z)));
        if (radius == 0.0d) {
            return new CoordinatesFloat3D(0.0f, 0.0f, 0.0f);
        }
        return new CoordinatesFloat3D(Math.atan2((double) y, (double) x), Math.asin(((double) z) / radius), radius);
    }

    public static Coordinates3D getCarthToSpherical(double x, double y, double z) {
        double radius = Math.sqrt(((x * x) + (y * y)) + (z * z));
        if (radius == 0.0d) {
            return new Coordinates3D(0.0d, 0.0d, 0.0d);
        }
        return new Coordinates3D(Math.atan2(y, x), Math.asin(z / radius), radius);
    }

    public static Coordinates3D getCarthToSphericalF(double x, double y, double z) {
        double radius = Math.sqrt(((x * x) + (y * y)) + (z * z));
        if (radius == 0.0d) {
            return new Coordinates3D(0.0d, 0.0d, 0.0d);
        }
        return new Coordinates3D(Math.atan2(y, x), Math.asin(z / radius), radius);
    }

    public static Coordinates3D getCarthToSpherical(Coordinates3D coordK) {
        return getCarthToSpherical(coordK.getX(), coordK.getY(), coordK.getZ());
    }

    public static Coordinates3D getSphericalToCarth(Coordinates3D coordS) {
        return getSphericalToCarth(coordS.getX(), coordS.getY(), coordS.getZ());
    }

    public static Coordinates3D getSphericalToCarth(double lon, double lat, double radius) {
        return new Coordinates3D((Math.cos(lat) * radius) * Math.cos(lon), (Math.cos(lat) * radius) * Math.sin(lon), radius * Math.sin(lat));
    }

    public static Coordinates3D addSpherical(Coordinates3D coordS1, Coordinates3D coordS2) {
        Coordinates3D coordK1 = getSphericalToCarth(coordS1);
        Coordinates3D coordK2 = getSphericalToCarth(coordS2);
        return getCarthToSpherical(new Coordinates3D(coordK1.getX() + coordK2.getX(), coordK1.getY() + coordK2.getY(), coordK1.getZ() + coordK2.getZ()));
    }

    public static float getAngle(CoordinatesFloat3D coord1, CoordinatesFloat3D coord2) {
        return getAngle(coord1.getX(), coord1.getY(), coord2.getX(), coord2.getY());
    }

    public static float getAngle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2((double) (y2 - y1), (double) (x2 - x1));
    }

    public static float getDistance(CoordinatesFloat3D coord1, CoordinatesFloat3D coord2) {
        return (float) Math.sqrt((Math.pow((double) (coord1.x - coord2.x), 2.0d) + Math.pow((double) (coord1.y - coord2.y), 2.0d)) + Math.pow((double) (coord1.r - coord2.r), 2.0d));
    }

    public static float getDistance(Coordinates3D coord1, Coordinates3D coord2) {
        return (float) Math.sqrt((Math.pow(coord1.x - coord2.x, 2.0d) + Math.pow(coord1.y - coord2.y, 2.0d)) + Math.pow(coord1.r - coord2.r, 2.0d));
    }
}
