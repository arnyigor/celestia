package com.arny.celestiatools.utils.astronomy;


public abstract class SolarSystemObject extends CelestialObject {
    public static final int COMET_ID = 400;
    public static final int EARTH_ID = 10;
    public static final int JUPITER_ID = 5;
    public static final int MARS_ID = 4;
    public static final int MERCURY_ID = 2;
    public static final int MOON_ID = 1;
    public static final int NEPTUNE_ID = 8;
    public static final int PLUTO_ID = 9;
    public static final int SATURN_ID = 6;
    public static final int SUN_ID = 0;
    public static final String SolarSystemObjectMap = "SolarSystemObjectMap";
    public static final String SolarSystemObjectName = "SolarSystemObjectName";
    public static final int URANUS_ID = 7;
    public static final int VENUS_ID = 3;
    protected double B0;
    protected double B1;
    protected double B2;
    protected double B3;
    protected double B4;
    protected double B5;
    protected double L0;
    protected double L1;
    protected double L2;
    protected double L3;
    protected double L4;
    protected double L5;
    protected double R0;
    protected double R1;
    protected double R2;
    protected double R3;
    protected double R4;
    protected double R5;
    public double T;
    public double T2;
    public double T3;
    public double T4;
    public double T5;
    protected DatePosition datePosition;
    protected double distanceAU;
    protected Coordinates3D geoEclCoord;
    protected Coordinates3D geoEclCoordSun;
    protected Coordinates3D geoEquCoord;
    protected Coordinates3D heliEclCoord;
    protected double jd;
    protected double lastJD;
    private double lastJDMeanAnomalies;
    protected Coordinates3D topoEquCoord;

    public enum Planet {
        Sun("ID10SolarSystem0"),
        Moon("ID10SolarSystem1"),
        Mercury("ID10SolarSystem2"),
        Venus("ID10SolarSystem3"),
        Mars("ID10SolarSystem4"),
        Jupiter("ID10SolarSystem5"),
        Saturn("ID10SolarSystem6"),
        Uranus("ID10SolarSystem7"),
        Neptune("ID10SolarSystem8"),
        Pluto("ID10SolarSystem9");
        public String objectId;

        private Planet(String objectId) {
            this.objectId = objectId;
        }

        public String getObjectId() {
            return this.objectId;
        }

    }

    public abstract double getCentralMeridian();

    public abstract double getDiameter1AUarcsec();

    public abstract int getImageTopViewResourceID();

    public abstract double getMeanDiameterArcsec();

    public abstract float getRadiusTopViewPixel();

    public abstract float getRadius_km();

    public abstract Coordinates3D getRotationalAxis();

    public abstract double getZeroMeridian();

    public abstract double getm0();

    protected SolarSystemObject(SolarSystemObject original) {
        super((CelestialObject) original);
        this.heliEclCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
        this.geoEclCoordSun = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.topoEquCoord = new Coordinates3D();
        this.lastJD = -99.0d;
        this.lastJDMeanAnomalies = -99.0d;
        this.T = -99.0d;
        this.T2 = -99.0d;
        this.T3 = -99.0d;
        this.T4 = -99.0d;
        this.T5 = -99.0d;
        this.L0 = 0.0d;
        this.L1 = 0.0d;
        this.L2 = 0.0d;
        this.L3 = 0.0d;
        this.L4 = 0.0d;
        this.L5 = 0.0d;
        this.B0 = 0.0d;
        this.B1 = 0.0d;
        this.B2 = 0.0d;
        this.B3 = 0.0d;
        this.B4 = 0.0d;
        this.B5 = 0.0d;
        this.R0 = 0.0d;
        this.R1 = 0.0d;
        this.R2 = 0.0d;
        this.R3 = 0.0d;
        this.R4 = 0.0d;
        this.R5 = 0.0d;
        this.jd = original.jd;
        this.distanceAU = original.distanceAU;
        this.lastJD = original.lastJD;
        this.lastJDMeanAnomalies = original.lastJDMeanAnomalies;
        this.T = original.T;
        this.T2 = original.T2;
        this.T3 = original.T3;
        this.T4 = original.T4;
        this.T5 = original.T5;
        try {
            this.heliEclCoord = original.heliEclCoord.copy();
        } catch (Exception e) {
        }
        try {
            this.geoEclCoord = original.geoEclCoord.copy();
        } catch (Exception e2) {
        }
        try {
            this.geoEclCoordSun = original.geoEclCoordSun.copy();
        } catch (Exception e3) {
        }
        try {
            this.geoEquCoord = original.geoEquCoord.copy();
        } catch (Exception e4) {
        }
        try {
            this.topoEquCoord = original.topoEquCoord.copy();
        } catch (Exception e5) {
        }
        try {
            this.datePosition = original.datePosition.copy();
        } catch (Exception e6) {
        }
    }

    public SolarSystemObject(String objectId) {
        super(objectId);
        this.heliEclCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
        this.geoEclCoordSun = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.topoEquCoord = new Coordinates3D();
        this.lastJD = -99.0d;
        this.lastJDMeanAnomalies = -99.0d;
        this.T = -99.0d;
        this.T2 = -99.0d;
        this.T3 = -99.0d;
        this.T4 = -99.0d;
        this.T5 = -99.0d;
        this.L0 = 0.0d;
        this.L1 = 0.0d;
        this.L2 = 0.0d;
        this.L3 = 0.0d;
        this.L4 = 0.0d;
        this.L5 = 0.0d;
        this.B0 = 0.0d;
        this.B1 = 0.0d;
        this.B2 = 0.0d;
        this.B3 = 0.0d;
        this.B4 = 0.0d;
        this.B5 = 0.0d;
        this.R0 = 0.0d;
        this.R1 = 0.0d;
        this.R2 = 0.0d;
        this.R3 = 0.0d;
        this.R4 = 0.0d;
        this.R5 = 0.0d;
        this.heliEclCoord = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
    }

    public SolarSystemObject(String objectId, double searchEventSmallGap, double searchEventLargeGap) {
        super(objectId, searchEventSmallGap, searchEventLargeGap);
        this.heliEclCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
        this.geoEclCoordSun = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.topoEquCoord = new Coordinates3D();
        this.lastJD = -99.0d;
        this.lastJDMeanAnomalies = -99.0d;
        this.T = -99.0d;
        this.T2 = -99.0d;
        this.T3 = -99.0d;
        this.T4 = -99.0d;
        this.T5 = -99.0d;
        this.L0 = 0.0d;
        this.L1 = 0.0d;
        this.L2 = 0.0d;
        this.L3 = 0.0d;
        this.L4 = 0.0d;
        this.L5 = 0.0d;
        this.B0 = 0.0d;
        this.B1 = 0.0d;
        this.B2 = 0.0d;
        this.B3 = 0.0d;
        this.B4 = 0.0d;
        this.B5 = 0.0d;
        this.R0 = 0.0d;
        this.R1 = 0.0d;
        this.R2 = 0.0d;
        this.R3 = 0.0d;
        this.R4 = 0.0d;
        this.R5 = 0.0d;
        this.heliEclCoord = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
    }


    public int getOrbitResourceID() {
        return SUN_ID;
    }

    public double getPositionAngle() {
        return 0.0d;
    }

    public double getJDLightTimeCorrected() {
        return this.jd - (0.0057755208d * this.distanceAU);
    }


    public void computeElements(DatePosition datePosition) {
        this.datePosition = datePosition.copy();
        this.jd = this.datePosition.getJD();
        if (this.jd != this.lastJD || this.geoEclCoordSun == null) {
            this.T = Ephemeris.getCenturiesSince2000(this.jd);
            this.T2 = this.T * this.T;
            this.T3 = this.T2 * this.T;
            this.T4 = this.T3 * this.T;
            this.T5 = this.T4 * this.T;
            this.lastJD = this.jd;
            new SunObject().computeHeliocentricEclipticalCoord(this.jd, this.geoEclCoordSun);
        }
    }

    public float getMoonAge(DatePosition mycalendar) {
        return 0.0f;
    }


    public int getPhaseResource() {
        return SUN_ID;
    }

    public int getSmallPhaseResource() {
        return SUN_ID;
    }


    public int getTopViewImageScaleWidth(int radius) {
        return radius;
    }

    public int getTopViewImageScaleHeight(int radius) {
        return radius;
    }

    public float getEarthShine() {
        return 0.0f;
    }

    public float getOblateness() {
        return 1.0f;
    }

    public float getVmag() {
        return (float) (getm0() + (5.0d * Math.log10(this.heliEclCoord.getRadius() * this.geoEclCoord.getRadius())));
    }

    public void setGeocentricEclipticalCoordinatesSun(Coordinates3D geoEclCoordSun) {
        this.geoEclCoordSun = geoEclCoordSun;
    }

    public Coordinates3D getHeliocentricCoordinatesTopDownView() {
        return getHeliocentricEclipticalCoordinates();
    }

    public Coordinates3D getGeocentricEquatorialCoordinates() {
        return this.geoEquCoord;
    }

    public Coordinates3D getHeliocentricEclipticalCoordinates() {
        return this.heliEclCoord;
    }

    public Coordinates3D getGeocentricEclipticalCoordinates() {
        return this.geoEclCoord;
    }

    public Coordinates3D getTopocentricEquatorialCoordinates() {
        return this.topoEquCoord;
    }

    public Coordinates3D getGeocentricEclipticalCoordinatesSun() {
        return this.geoEclCoordSun;
    }

    public double getDistanceAU() {
        return this.geoEclCoord.getRadius();
    }

    public double getSolarDistanceAU() {
        return this.heliEclCoord.getRadius();
    }

    public double getIlluminatedFraction() {
        return (1.0d + Math.cos(getPhaseAngle())) / 2.0d;
    }

    public double getPhaseAngle() {
        double beta = Math.acos(((Math.pow(this.geoEclCoord.getRadius(), 2.0d) + Math.pow(this.heliEclCoord.getRadius(), 2.0d)) - Math.pow(this.geoEclCoordSun.getRadius(), 2.0d)) / ((this.geoEclCoord.getRadius() * 2.0d) * this.heliEclCoord.getRadius()));
        if (beta < 0.0d) {
            return beta + Ephemeris.PID2;
        }
        return beta;
    }

    public double getElongation() {
        double sign = -1.0d;
        double objectLon = this.geoEclCoord.getLongitude() % Ephemeris.PI2;
        double sunLon = this.geoEclCoordSun.getLongitude() % Ephemeris.PI2;
        if (objectLon < 0.0d) {
            objectLon += Ephemeris.PI2;
        }
        if (sunLon < 0.0d) {
            sunLon += Ephemeris.PI2;
        }
        if (sunLon < objectLon) {
            sunLon += Ephemeris.PI2;
        }
        double delta = objectLon - sunLon;
        if ((delta >= 0.0d && delta < Ephemeris.PI) || delta < -3.141592653589793d) {
            sign = 1.0d;
        }
        return Math.acos(Math.cos(this.geoEclCoord.getLatitude()) * Math.cos(this.geoEclCoord.getLongitude() - this.geoEclCoordSun.getLongitude())) * sign;
    }

    public double getRawElongation() {
        return Math.acos(Math.cos(this.geoEclCoord.getLatitude()) * Math.cos(this.geoEclCoord.getLongitude() - this.geoEclCoordSun.getLongitude()));
    }

    public double getGeocentricDiameterArcsec(double distanceAU) {
        return getDiameter1AUarcsec() / distanceAU;
    }

    public double getGeocentricDiameterArcsec() {
        return getGeocentricDiameterArcsec(getDistanceAU());
    }

    public Coordinates3D getGeocentricEquatorialCoordinates(DatePosition datePosition) {
        computeElements(datePosition);
        return this.geoEquCoord;
    }

    public Coordinates3D getTopocentricEquatorialCoordinates(DatePosition datePosition) {
        computeElements(datePosition);
        return this.topoEquCoord;
    }

    public Coordinates3D getQuickTopocentricEquatorialCoordinates(DatePosition datePosition) {
        computeElements(datePosition);
        return this.topoEquCoord;
    }


    public Coordinates3D computeHeliocentricCoordinatesTopDownView(DatePosition datePosition, boolean recalculate) {
        if (recalculate || this.topoEquCoord == null) {
            getTopocentricEquatorialCoordinates(datePosition);
        }
        return getHeliocentricCoordinatesTopDownView();
    }

    public BasisCelestialObject getBasisObject() {
        return null;
    }

    public int getID() {
        return SUN_ID;
    }

    public double geth0() {
        return 0.0d;
    }

    public String getColor() {
        return null;
    }

    protected void computePeriodicTermsHighPrecision(double T) {
    }

    protected void computePeriodicTermsLowPrecision(double T) {
    }

    private void getHeliocentricEclipticalCoord(double jd, Coordinates3D helioCoord) {
        double L;
        double B;
        double R;
        this.T = (jd - 2451545.0d) / 365250.0d;
        this.T2 = this.T * this.T;
        this.T3 = this.T2 * this.T;
        this.T4 = this.T3 * this.T;
        this.T5 = this.T4 * this.T;
        this.L5 = 0.0d;
        this.L4 = 0.0d;
        this.L3 = 0.0d;
        this.L2 = 0.0d;
        this.L1 = 0.0d;
        this.L0 = 0.0d;
        this.B5 = 0.0d;
        this.B4 = 0.0d;
        this.B3 = 0.0d;
        this.B2 = 0.0d;
        this.B1 = 0.0d;
        this.B0 = 0.0d;
        this.R5 = 0.0d;
        this.R4 = 0.0d;
        this.R3 = 0.0d;
        this.R2 = 0.0d;
        this.R1 = 0.0d;
        this.R0 = 0.0d;
        if (this.isLowPrecision) {
            computePeriodicTermsLowPrecision(this.T);
            L = (((this.L0 + (this.L1 * this.T)) + (this.L2 * this.T2)) + (this.L3 * this.T3)) + (this.L4 * this.T4);
            B = (((this.B0 + (this.B1 * this.T)) + (this.B2 * this.T2)) + (this.B3 * this.T3)) + (this.B4 * this.T4);
            R = (((this.R0 + (this.R1 * this.T)) + (this.R2 * this.T2)) + (this.R3 * this.T3)) + (this.R4 * this.T4);
        } else {
            computePeriodicTermsHighPrecision(this.T);
            L = ((((this.L0 + (this.L1 * this.T)) + (this.L2 * this.T2)) + (this.L3 * this.T3)) + (this.L4 * this.T4)) + (this.L5 * this.T5);
            B = ((((this.B0 + (this.B1 * this.T)) + (this.B2 * this.T2)) + (this.B3 * this.T3)) + (this.B4 * this.T4)) + (this.B5 * this.T5);
            R = ((((this.R0 + (this.R1 * this.T)) + (this.R2 * this.T2)) + (this.R3 * this.T3)) + (this.R4 * this.T4)) + (this.R5 * this.T5);
        }
        L %= Ephemeris.PI2;
        if (L < 0.0d) {
            L += Ephemeris.PI2;
        } else if (L >= Ephemeris.PI2) {
            L -= Ephemeris.PI2;
        }
        B %= Ephemeris.PI2;
        if (B < -3.141592653589793d) {
            B += Ephemeris.PI2;
        } else if (B > Ephemeris.PI) {
            B -= Ephemeris.PI2;
        }
        helioCoord.setLonLatDist(L, B, R);
    }

    protected void computeHeliocentricEclipticalCoord(double jd, Coordinates3D helioCoord) {
        getHeliocentricEclipticalCoord(jd, helioCoord);
    }

}
