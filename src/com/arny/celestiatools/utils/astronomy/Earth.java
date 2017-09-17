package com.arny.celestiatools.utils.astronomy;

public class Earth extends BasisCelestialObject {
    public Earth() {
        super("ID10SolarSystem10");
    }

    protected Earth(Earth original) {
        super(original);
    }

    public Earth copy() {
        return new Earth(this);
    }

    public CelestialObject getCelestialObject() {
        return new EarthObject();
    }

}
