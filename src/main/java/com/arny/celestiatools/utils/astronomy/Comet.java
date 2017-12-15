package com.arny.celestiatools.utils.astronomy;

import java.util.ArrayList;

public class Comet extends BasisCelestialObject {
    private final int ID_HALLEY = 1;
    private String designation;
    private EllipticMotion em;
    private double magAbs;
    private double magSlope;
    private String name;

    public Comet(String id, String designation, String name, ArrayList<EllipticMotionParameters> emp, double magAbs, double magSlope) {
        super("ID_COMET" + id);
        this.em = new EllipticMotion(emp);
        this.designation = designation;
        this.name = name;
        this.magAbs = magAbs;
        this.magSlope = magSlope;
    }

    protected Comet(Comet original) {
        super( original);
        this.name = original.name;
        this.magAbs = original.magAbs;
        this.magSlope = original.magSlope;
        this.designation = original.designation;
        try {
            this.em = original.em.copy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Comet copy() {
        return new Comet(this);
    }

    public String getName() {
        return this.name;
    }

    public void computePosition(DatePosition datePosition) {
        this.em.compute(datePosition);
        this.ra = this.em.getGeocentricEquatorialCoordinates().getRA();
        this.dec = this.em.getGeocentricEquatorialCoordinates().getDec();
    }

    public double getDistanceAU() {
        return this.em.getDistanceAU();
    }

    public double getDistanceSunAU() {
        return this.em.getDistanceSunAU();
    }

    public Coordinates3D getHeliocentricEquatorialCoordinates() {
        return this.em.getHeliocentricEquatorialCoordinates();
    }

    public String getDesignations() {
        return this.designation;
    }

    public double getMagAbs() {
        return this.magAbs;
    }

    public double getMagSlope() {
        return this.magSlope;
    }

    public EllipticMotion getEllipticMotion() {
        return this.em;
    }

    public CelestialObject getCelestialObject() {
        return new CometObject(this);
    }

}
