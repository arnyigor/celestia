package com.arny.celestiatools.utils.astronomy;


import java.util.Calendar;

public abstract class CelestialObject {
    public static final String COMET = "Minorplanet";
    public static final String CONSTELLATION = "Constellation";
    public static final String DEEP_SKY = "DeepSky";
    public static final String METEOR_SHOWER = "MeteorShower";
    public static final String MINOR_PLANET = "MinorPlanet";
    public static final int REQUEST_RESULT = 1;
    public static final String SOLAR_SYSTEM = "SolarSystem";
    public static final String STAR = "Star";
    protected volatile boolean isCancelled;
    protected boolean isLowPrecision;
    public double searchEventLargeGap;
    public double searchEventSmallGap;

    public abstract CelestialObject copy();

    public abstract BasisCelestialObject getBasisObject();

    public abstract String getColor();

    public abstract double getDistanceAU();

    public abstract double getElongation();

    public abstract double getGeocentricDiameterArcsec();

    public abstract double getGeocentricDiameterArcsec(double d);

    public abstract Coordinates3D getGeocentricEquatorialCoordinates(DatePosition datePosition);

    public abstract int getID();

    public abstract int getInformationActivity();

    public abstract String getName();

    public abstract Coordinates3D getTopocentricEquatorialCoordinates();

    public abstract Coordinates3D getTopocentricEquatorialCoordinates(DatePosition datePosition);

    public abstract float getVmag();

    public abstract double geth0();

    public CelestialObject(String objectId) {
        this.isCancelled = false;
        this.isLowPrecision = false;
        this.searchEventLargeGap = 10.0d;
        this.searchEventSmallGap = 0.1d;
        this.isCancelled = false;
        this.isLowPrecision = false;
    }

    public CelestialObject(String objectId, double searchEventSmallGap, double searchEventLargeGap) {
        this.isCancelled = false;
        this.isLowPrecision = false;
        this.searchEventSmallGap = searchEventSmallGap;
        this.searchEventLargeGap = searchEventLargeGap;
        this.isCancelled = false;
        this.isLowPrecision = false;
    }

    protected CelestialObject(CelestialObject original) {
        this.isCancelled = false;
        this.isLowPrecision = false;
        this.searchEventLargeGap = original.searchEventLargeGap;
        this.searchEventSmallGap = original.searchEventSmallGap;
        this.isCancelled = false;
        this.isLowPrecision = false;
    }

    public int getOrbitResourceID() {
        return 0;
    }

    public void onCancelled() {
        this.isCancelled = true;
    }

    public void reset() {
        this.isCancelled = false;
    }


    public int getCalendarTimeStep() {
        return 5;
    }

    public void computeElements(DatePosition datePosition) {
    }

    public Coordinates3D getQuickTopocentricEquatorialCoordinates(DatePosition datePosition) {
        return getTopocentricEquatorialCoordinates(datePosition);
    }

    public float getPositionAngleBrightLimb(Coordinates3D coordSun, Coordinates3D coordMoon) {
        return 0.0f;
    }


    public void setAltitude(float alt) {
        getBasisObject().setAltitude(alt);
    }

    public void setAzAlt(double az, double alt) {
        getBasisObject().setAzAlt(az, alt);
    }

    public double getSolarDistanceAU() {
        return -1.0d;
    }

    public double getPhaseAngle() {
        return -1.0d;
    }

    public double getIlluminatedFraction() {
        return -1.0d;
    }

    public Coordinates3D getHeliocentricEclipticalCoordinates() {
        return new Coordinates3D(0.0d, 0.0d, 0.0d);
    }

    public Coordinates3D getGeocentricEclipticalCoordinates() {
        return new Coordinates3D(0.0d, 0.0d, 0.0d);
    }

    public double getSearchEventLargeGap() {
        return this.searchEventLargeGap;
    }

    public double getSearchEventSmallGap() {
        return this.searchEventSmallGap;
    }

    public double getSunMaxSearchAltitude() {
        double alt = (double) (-8.0f - getVmag());
        if (alt > -6.0d) {
            alt = -6.0d;
        } else if (alt < -18.0d) {
            alt = -18.0d;
        }
        return Math.toRadians(alt);
    }

    public double getObjectMinSearchAltitude() {
        return Math.toRadians(5.0d);
    }

    public int getYearVisibilityBarDayStep() {
        return 5;
    }

    public int getVisibilitySearchStepMinutes() {
        return 10;
    }

    public boolean isVisible(DatePosition datePosition) {
        Coordinates3D coordRADec = getTopocentricEquatorialCoordinates(datePosition);
        Coordinates3D sunRADec = new SunObject().getTopocentricEquatorialCoordinates(datePosition);
        Coordinates3D sunAzAlt = new Coordinates3D();
        Coordinates3D objectAzAlt = new Coordinates3D();
        Ephemeris.getAzAltFromRADec(datePosition, sunRADec, sunAzAlt);
        Ephemeris.getAzAltFromRADec(datePosition, coordRADec, objectAzAlt);
        return Ephemeris.isVisible(sunAzAlt, objectAzAlt, getSunMaxSearchAltitude(), getObjectMinSearchAltitude());
    }

    public RiseSetEvent getRise(DatePosition datePosition) {
        setLowPrecision(true);
        DatePosition myDatePosition = datePosition.copy();
        DatePosition myDatePositionTemp = datePosition.copy();
        myDatePositionTemp.setJD(JulianDate.getJD0UT(datePosition));
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, -1);
        Coordinates3D coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        double ra1 = coord.getRA();
        double dec1 = coord.getDec();
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, 1);
        coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        double ra2 = coord.getRA();
        double dec2 = coord.getDec();
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, 1);
        coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        RiseSetEvent rise = Ephemeris.getRise(myDatePosition, ra1, dec1, ra2, dec2, coord.getRA(), coord.getDec(), geth0());
        setLowPrecision(false);
        if (rise.getTime() == Ephemeris.notVisible) {
            return rise;
        }
        if (rise.getTime() > 24.0d || rise.getTime() < 0.0d) {
            return new RiseSetEvent(Ephemeris.notVisible, Ephemeris.notVisible, Ephemeris.notVisible, 0, 0);
        }
        return Ephemeris.getTimeBetween0and24(rise);
    }

    public RiseSetEvent getTransit(DatePosition datePosition) {
        DatePosition myDatePosition = datePosition.copy();
        DatePosition myDatePositionTemp = datePosition.copy();
        setLowPrecision(true);
        RiseSetEvent rise = new RiseSetEvent(0.0d, 0.0d, 0.0d, REQUEST_RESULT, 0);
        myDatePositionTemp.setJD(JulianDate.getJD0UT(datePosition));
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, -1);
        Coordinates3D coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        double ra1 = coord.getRA();
        double dec1 = coord.getDec();
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, 1);
        coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        double ra2 = coord.getRA();
        double dec2 = coord.getDec();
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, 1);
        coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        rise = Ephemeris.getTransit(myDatePosition, ra1, dec1, ra2, dec2, coord.getRA(), coord.getDec(), geth0());
        setLowPrecision(false);
        if (rise.getTime() == Ephemeris.notVisible) {
            return rise;
        }
        if (rise.getTime() > 24.0d || rise.getTime() < 0.0d) {
            return new RiseSetEvent(Ephemeris.notVisible, Ephemeris.notVisible, Ephemeris.notVisible, REQUEST_RESULT, 0);
        }
        return Ephemeris.getTimeBetween0and24(rise);
    }

    public RiseSetEvent getSet(DatePosition datePosition) {
        DatePosition myDatePosition = datePosition.copy();
        DatePosition myDatePositionTemp = datePosition.copy();
        setLowPrecision(true);
        myDatePositionTemp.setJD(JulianDate.getJD0UT(datePosition));
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, -1);
        Coordinates3D coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        double ra1 = coord.getRA();
        double dec1 = coord.getDec();
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, 1);
        coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        double ra2 = coord.getRA();
        double dec2 = coord.getDec();
        myDatePositionTemp.add(Calendar.DAY_OF_MONTH, 1);
        coord = getTopocentricEquatorialCoordinates(myDatePositionTemp);
        RiseSetEvent rise = Ephemeris.getSet(myDatePosition, ra1, dec1, ra2, dec2, coord.getRA(), coord.getDec(), geth0());
        setLowPrecision(false);
        if (rise.getTime() == Ephemeris.notVisible) {
            return rise;
        }
        if (rise.getTime() > 24.0d || rise.getTime() < 0.0d) {
            return new RiseSetEvent(Ephemeris.notVisible, Ephemeris.notVisible, Ephemeris.notVisible, 2, 0);
        }
        return Ephemeris.getTimeBetween0and24(rise);
    }


    public float getVelocitykms(DatePosition datePosition) {
        double dS = ((double) 2) * 3600.0d;
        DatePosition datePositiont1 = datePosition.copy();
        DatePosition datePositiont2 = datePosition.copy();
        datePositiont1.add(Calendar.HOUR_OF_DAY, -1);
        datePositiont2.add(Calendar.HOUR_OF_DAY, 1);
        getTopocentricEquatorialCoordinates(datePositiont1);
        Coordinates3D coordt1 = new Coordinates3D();
        Ephemeris.getRectangularCoordinates(getHeliocentricEclipticalCoordinates(), coordt1);
        getTopocentricEquatorialCoordinates(datePositiont2);
        Coordinates3D coordt2 = new Coordinates3D();
        Ephemeris.getRectangularCoordinates(getHeliocentricEclipticalCoordinates(), coordt2);
        return (float) ((AstroConst.AU * ((double) SphericalMath.getDistance(coordt1, coordt2))) / dS);
    }

    public float getOrbitOrientation(DatePosition datePosition) {
        DatePosition datePositiont1 = datePosition.copy();
        DatePosition datePositiont2 = datePosition.copy();
        datePositiont2.add(Calendar.HOUR_OF_DAY, REQUEST_RESULT);
        getTopocentricEquatorialCoordinates(datePositiont1);
        Coordinates3D coordt1 = getHeliocentricEclipticalCoordinates().copy();
        getTopocentricEquatorialCoordinates(datePositiont2);
        Coordinates3D coordt2 = getHeliocentricEclipticalCoordinates().copy();
        double lon1 = coordt1.getLongitude();
        double lon2 = coordt2.getLongitude();
        if (lon1 > 6.0d && lon2 < 0.2d) {
            lon2 += 6.2831854820251465d;
        } else if (lon1 < 0.2d && lon2 > 6.0d) {
            lon1 += 6.2831854820251465d;
        }
        return (float) Math.signum(lon2 - lon1);
    }

    public boolean isLowPrecision() {
        return this.isLowPrecision;
    }

    public void setLowPrecision(boolean isLowPrecision) {
        this.isLowPrecision = isLowPrecision;
    }
}
