package com.arny.celestiatools.utils.astronomy;

import java.util.ArrayList;
import java.util.List;

public class MinorPlanet extends BasisCelestialObject {
    private static final String ID_CERES = "ID20MinorPlanet1";
    private static final String ID_ERIS = "ID20MinorPlanet136199";
    private static final String ID_HAUMEA = "ID20MinorPlanet136108";
    private static final String ID_MAKEMAKE = "ID20MinorPlanet136472";
    private static final String ID_PLUTO = "ID20MinorPlanet134340";
    private static final String ID_VESTA = "ID20MinorPlanet4";
    private EllipticMotion em;
    private float magG;
    private float magH;
    private String name;

    public MinorPlanet(String id, String name, ArrayList<EllipticMotionParameters> emp, float magH, float magG) {
        super("ID_MINOR_PLANET" + id);
        this.em = new EllipticMotion(emp);
        this.name = name;
        this.magH = magH;
        this.magG = magG;
    }

    protected MinorPlanet(MinorPlanet original) {
        super(original);
        this.name = original.name;
        this.magH = original.magH;
        this.magG = original.magG;
        try {
            this.em = original.em.copy();
        } catch (Exception e) {
        }
    }

    public MinorPlanet copy() {
        return new MinorPlanet(this);
    }

    public CelestialObject getCelestialObject() {
        return new MinorPlanetObject(this);
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

    public double getMagAbs() {
        return (double) this.magH;
    }

    public double getMagSlope() {
        return (double) this.magG;
    }

    public EllipticMotion getEllipticMotion() {
        return this.em;
    }
}
