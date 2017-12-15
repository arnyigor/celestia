package com.arny.celestiatools.utils.astronomy;

public class PhysicalData {
    private float aphelion;
    private float axialTilt;
    private float density;
    private float diameter;
    private int discoveryYear;
    private float eccentricity;
    private float escapeVelocity;
    private float gravity;
    private float lengthOfDay;
    private boolean magneticField;
    private float majorAxis;
    private float mass;
    private float maxDiameter;
    private float maxMagnitude;
    private float meanTemperature;
    private String name;
    private int numberOfMoons;
    private float orbitalInclination;
    private float orbitalPeriod;
    private float orbitalVelocity;
    private float perihelion;
    private boolean rings;
    private float rotationPeriod;
    private float surfacePressure;

    protected PhysicalData(PhysicalData original) {
        this.name = original.name;
        this.diameter = original.diameter;
        this.mass = original.mass;
        this.density = original.density;
        this.gravity = original.gravity;
        this.escapeVelocity = original.escapeVelocity;
        this.rotationPeriod = original.rotationPeriod;
        this.majorAxis = original.majorAxis;
        this.perihelion = original.perihelion;
        this.aphelion = original.aphelion;
        this.orbitalPeriod = original.orbitalPeriod;
        this.orbitalVelocity = original.orbitalVelocity;
        this.orbitalInclination = original.orbitalInclination;
        this.eccentricity = original.eccentricity;
        this.meanTemperature = original.meanTemperature;
        this.rings = original.rings;
        this.magneticField = original.magneticField;
        this.surfacePressure = original.surfacePressure;
        this.axialTilt = original.axialTilt;
        this.lengthOfDay = original.lengthOfDay;
        this.numberOfMoons = original.numberOfMoons;
        this.maxMagnitude = original.maxMagnitude;
        this.maxDiameter = original.maxDiameter;
        this.discoveryYear = original.discoveryYear;
    }

    public PhysicalData copy() {
        return new PhysicalData(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscoveryYear() {
        return this.discoveryYear;
    }

    public void setDiscoveryYear(int discoveryYear) {
        this.discoveryYear = discoveryYear;
    }

    public float getMaxDiameter() {
        return this.maxDiameter;
    }

    public void setMaxDiameter(float maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    public float getMaxMagnitude() {
        return this.maxMagnitude;
    }

    public void setMaxMagnitude(float maxMagnitude) {
        this.maxMagnitude = maxMagnitude;
    }

    public int getNumberOfMoons() {
        return this.numberOfMoons;
    }

    public void setNumberOfMoons(int numberOfMoons) {
        this.numberOfMoons = numberOfMoons;
    }

    public float getSurfacePressure() {
        return this.surfacePressure;
    }

    public void setSurfacePressure(float surfacePressure) {
        this.surfacePressure = surfacePressure;
    }

    public float getAxialTilt() {
        return this.axialTilt;
    }

    public void setAxialTilt(float axialTilt) {
        this.axialTilt = axialTilt;
    }

    public float getLengthOfDay() {
        return this.lengthOfDay;
    }

    public void setLengthOfDay(float lengthOfDay) {
        this.lengthOfDay = lengthOfDay;
    }

    public float getDiameter() {
        return this.diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getDensity() {
        return this.density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getGravity() {
        return this.gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getEscapeVelocity() {
        return this.escapeVelocity;
    }

    public void setEscapeVelocity(float escapeVelocity) {
        this.escapeVelocity = escapeVelocity;
    }

    public float getRotationPeriod() {
        return this.rotationPeriod;
    }

    public void setRotationPeriod(float rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public float getMajorAxis() {
        return this.majorAxis;
    }

    public void setMajorAxis(float majorAxis) {
        this.majorAxis = majorAxis;
    }

    public float getPerihelion() {
        return this.perihelion;
    }

    public void setPerihelion(float perihelion) {
        this.perihelion = perihelion;
    }

    public float getAphelion() {
        return this.aphelion;
    }

    public void setAphelion(float aphelion) {
        this.aphelion = aphelion;
    }

    public float getOrbitalPeriod() {
        return this.orbitalPeriod;
    }

    public void setOrbitalPeriod(float orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    public float getOrbitalVelocity() {
        return this.orbitalVelocity;
    }

    public void setOrbitalVelocity(float orbitalVelocity) {
        this.orbitalVelocity = orbitalVelocity;
    }

    public float getOrbitalInclination() {
        return this.orbitalInclination;
    }

    public void setOrbitalInclination(float orbitalInclination) {
        this.orbitalInclination = orbitalInclination;
    }

    public float getEccentricity() {
        return this.eccentricity;
    }

    public void setEccentricity(float eccentricity) {
        this.eccentricity = eccentricity;
    }

    public float getMeanTemperature() {
        return this.meanTemperature;
    }

    public void setMeanTemperature(float meanTemperature) {
        this.meanTemperature = meanTemperature;
    }

    public boolean isRings() {
        return this.rings;
    }

    public void setRings(boolean rings) {
        this.rings = rings;
    }

    public boolean isMagneticField() {
        return this.magneticField;
    }

    public void setMagneticField(boolean magneticField) {
        this.magneticField = magneticField;
    }
}
