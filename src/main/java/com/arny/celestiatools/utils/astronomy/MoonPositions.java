package com.arny.celestiatools.utils.astronomy;

import java.util.ArrayList;

public class MoonPositions {
    public ArrayList<CoordinatesFloat3D> coordXYZ = new ArrayList();
    public ArrayList<CoordinatesFloat3D> coordXYZShadow = new ArrayList();
    public ArrayList<Boolean> eclipsed = new ArrayList();
    private ArrayList<Integer> indicesSorted = new ArrayList();
    public ArrayList<Boolean> occultation = new ArrayList();
    private boolean planetVisible;
    private float redSpotLonDiff;
    public ArrayList<Boolean> shadowVisible = new ArrayList();
    public ArrayList<Boolean> transit = new ArrayList();

    public MoonPositions(int numberMoons) {
        for (int i = 0; i < numberMoons; i++) {
            this.coordXYZ.add(new CoordinatesFloat3D());
            this.coordXYZShadow.add(new CoordinatesFloat3D());
            this.eclipsed.add(Boolean.valueOf(false));
            this.shadowVisible.add(Boolean.valueOf(false));
            this.occultation.add(Boolean.valueOf(false));
            this.transit.add(Boolean.valueOf(false));
        }
    }

    public MoonPositions copy() {
        return new MoonPositions(this);
    }

    public MoonPositions(MoonPositions original) {
        this.coordXYZ = new ArrayList(original.coordXYZ);
        this.coordXYZShadow = new ArrayList(original.coordXYZShadow);
        this.eclipsed = new ArrayList(original.eclipsed);
        this.shadowVisible = new ArrayList(original.shadowVisible);
        this.occultation = new ArrayList(original.occultation);
        this.transit = new ArrayList(original.transit);
        this.planetVisible = original.planetVisible;
        this.redSpotLonDiff = original.redSpotLonDiff;
        this.indicesSorted = new ArrayList(original.indicesSorted);
    }


    public ArrayList<Integer> getIndicesSorted() {
        return this.indicesSorted;
    }

    public void setPlanetVisible(boolean planetVisible) {
        this.planetVisible = planetVisible;
    }

    public boolean isPlanetVisible() {
        return this.planetVisible;
    }

    public float getRedSpotLonDiff() {
        return this.redSpotLonDiff;
    }

    public void setRedSpotLonDiff(float redSpotLonDiff) {
        this.redSpotLonDiff = redSpotLonDiff;
    }
}
