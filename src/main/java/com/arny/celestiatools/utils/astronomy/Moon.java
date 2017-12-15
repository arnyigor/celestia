package com.arny.celestiatools.utils.astronomy;


public class Moon extends BasisCelestialObject {
    public Moon() {
        super("ID10SolarSystem1");
    }

    protected Moon(Moon original) {
        super((BasisCelestialObject) original);
    }

    public Moon copy() {
        return new Moon(this);
    }

    public CelestialObject getCelestialObject() {
        return new MoonObject();
    }

}
