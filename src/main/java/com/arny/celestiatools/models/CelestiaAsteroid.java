package com.arny.celestiatools.models;

public class CelestiaAsteroid {
	private String Name,OrbitType,updateTime;
	private double radius,period,sma,inc,node,ecc,peric,Ma,epoch;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public double getSma() {
        return sma;
    }

    public void setSma(double sma) {
        this.sma = sma;
    }

    public double getInc() {
        return inc;
    }

    public void setInc(double inc) {
        this.inc = inc;
    }

    public double getNode() {
        return node;
    }

    public void setNode(double node) {
        this.node = node;
    }

    public double getEcc() {
        return ecc;
    }

    public void setEcc(double ecc) {
        this.ecc = ecc;
    }

    public double getPeric() {
        return peric;
    }

    public void setPeric(double peric) {
        this.peric = peric;
    }

    public double getMa() {
        return Ma;
    }

    public void setMa(double ma) {
        Ma = ma;
    }

    public double getEpoch() {
        return epoch;
    }

    public void setEpoch(double epoch) {
        this.epoch = epoch;
    }

    public String getOrbitType() {
        return OrbitType;
    }

    public void setOrbitType(String orbitType) {
        OrbitType = orbitType;
    }

    @Override
    public String toString() {
        return "Name"+getName() +
                "\nOrbitType:" +getOrbitType() +
                "\nradius:"+getRadius() +
                "\nperiod:"+getPeriod() +
                "\nsma:"+getSma() +
                "\ninc:"+getInc() +
                "\nnode:"+getNode() +
                "\necc:"+getEcc() +
                "\nperic:"+getPeric() +
                "\nMa:"+getMa() +
                "\nepoch" + getEpoch() +
                "\nupdateTime" + getUpdateTime();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
