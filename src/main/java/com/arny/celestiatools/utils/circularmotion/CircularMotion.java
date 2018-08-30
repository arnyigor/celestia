package com.arny.celestiatools.utils.circularmotion;

import com.arny.celestiatools.utils.MathUtils;
import com.arny.celestiatools.utils.astronomy.AstroConst;

public class CircularMotion {
    private double ecc;
    private double hp;
    private double uskorenie;
    private double v1;
    private double v2;
    private int hour;
    private int min;
    private int sec;
    private double ptime;

    CircularMotion(double mass, double radius, double Hp) {
        hp = Hp;
        uskorenie = (AstroConst.Gconst * mass) / Math.pow((radius + hp), 2);
        v1 = Math.sqrt((AstroConst.Gconst * mass) / (radius + hp));
        v2 = Math.sqrt(2) * v1;
        ecc = 1;
        ptime = Math.sqrt((4 * Math.pow(Math.PI, 2) * Math.pow((radius + hp), 3)) / (AstroConst.Gconst * mass));
        ptime = MathUtils.round(ptime, 0);
        hour = (int) (ptime / 3600);
        min = (int) ((ptime - hour * 3600) / 60);
        sec = (int) (ptime - hour * 3600 - min * 60);
    }

    public double getUskorenie() {
        return uskorenie;
    }

    public double getV1() {
        return v1;
    }

    public double getV2() {
        return v2;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    public double getPtime() {
        return ptime;
    }

    public double getEcc() {
        return ecc;
    }

    public void setEcc(double ecc) {
        this.ecc = ecc;
    }
}