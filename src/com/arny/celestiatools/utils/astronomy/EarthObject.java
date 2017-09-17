package com.arny.celestiatools.utils.astronomy;


public class EarthObject extends SolarSystemObject {
    private Earth earth = new Earth();

    public EarthObject copy() {
        return new EarthObject(this);
    }

    protected EarthObject(EarthObject original) {
        super((SolarSystemObject) original);
    }

    public EarthObject() {
        super("ID10SolarSystem10");
    }


    public BasisCelestialObject getBasisObject() {
        return this.earth;
    }

    public double getEccentricity(double jd) {
        double T = Ephemeris.getCenturiesSince1900(jd);
        return (0.01675104d - (4.18E-5d * T)) - ((1.26E-7d * T) * T);
    }

    public double getCentralMeridian() {
        return 0.0d;
    }

    public String getColor() {
        return null;
    }

    public double getDiameter1AUarcsec() {
        return 0.0d;
    }

    @Override
    public int getImageTopViewResourceID() {
        return 0;
    }

    public double getMeanDiameterArcsec() {
        return 0.0d;
    }

    public float getRadius_km() {
        return 6371.009f;
    }


    public float getRadiusTopViewPixel() {
        return 10.0f;
    }

    public Coordinates3D getRotationalAxis() {
        return new Coordinates3D(Math.toRadians(0.0d), Math.toRadians(90.0d), 0.0d);
    }

    public double getZeroMeridian() {
        double d = this.jd - 2451545.0d;
        return Math.toRadians((176.655d + (361.0d * d)) + (0.62d * (d / 36525.0d)));
    }

    public double getm0() {
        return 0.0d;
    }

    public int getID() {
        return 10;
    }

    @Override
    public int getInformationActivity() {
        return 0;
    }

    @Override
    public String getName() {
        return "Earth";
    }

    public double geth0() {
        return 0.0d;
    }

    public void computeElements(DatePosition datePosition) {
        super.computeElements(datePosition);
        this.heliEclCoord = new Coordinates3D(Ephemeris.PI + this.geoEclCoordSun.getLongitude(), -this.geoEclCoordSun.getLatitude(), this.geoEclCoordSun.getRadius());
        this.geoEclCoord = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.topoEquCoord = new Coordinates3D();
    }
}
