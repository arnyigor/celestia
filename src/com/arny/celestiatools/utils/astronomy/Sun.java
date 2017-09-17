package com.arny.celestiatools.utils.astronomy;

public class Sun extends BasisCelestialObject {
    public Sun() {
        super("ID10SolarSystem0");
        this.mag = -26.8f;
    }

    protected Sun(Sun original) {
        super((BasisCelestialObject) original);
    }

    public Sun copy() {
        return new Sun(this);
    }

    public CelestialObject getCelestialObject() {
        return new SunObject();
    }

}
