package com.arny.celestiatools.utils.circularmotion;

import com.arny.celestiatools.utils.MathUtils;
import com.arny.celestiatools.utils.Utility;
import com.arny.celestiatools.utils.astronomy.AstroConst;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;

public class EllipseMotion {
    private double mass;
    private double radius;
    private double hp;
    private double vp;
    private double uskorenie;
    private double v1;
    private double v2;
    private double ecc;
    private double ha;
    private double sma;
    private double geosync;
    private int hour;
    private int min;
    private int sec;
    private double ptime;
    private double va;

    EllipseMotion(double mMass, double mRadius, double Hp, double Ha, double Vp, double Ecc, double SMA, double period) {
        mass = mMass;
        radius = mRadius;
        hp = Hp;
        ha = Ha;
        vp = Vp;
        ecc = Ecc;
        sma = SMA;
        if (period != 0) {
            geosync = Math.pow((Math.pow(period, 2) * AstroConst.Gconst * mass) / (4 * Math.pow(Math.PI, 2)), (double) 1 / 3);
            ecc = 1;
            hp = geosync - radius;
            ha = geosync - radius;
        }
        if (hp == 0 && sma != 0 && ecc != 0) {
            hp = (sma * (1 - ecc)) - radius;
        }
        if (ha == 0 && sma != 0 && ecc != 0) {
            ha = (sma * (1 + ecc)) - radius;
        }

        double peri = radius + hp;
        double apo = radius + ha;
        uskorenie = (AstroConst.Gconst * mass) / Math.pow(peri, 2);
        v1 = Math.sqrt((AstroConst.Gconst * mass) / (peri));
        v2 = Math.sqrt(2) * v1;
        if (ha == 0) {
            ha = (peri) / (((2 * AstroConst.Gconst * mass) / (((peri) * Math.pow(vp, 2))) - 1)) - radius;
        }
        if (vp == 0) {
            vp = Math.sqrt(2 * AstroConst.Gconst * mass * apo / ((peri) * (apo + peri)));
        }
        if (ecc == 0) {
            ecc = (((peri) * Math.pow(vp, 2)) / (AstroConst.Gconst * mass)) - 1;
        }
        if (sma == 0) {
            sma = (hp + ha + (2 * radius)) / 2;
        }
        if (apo < 0) {
            ha = 0;
            sma = 0;
        }
        ptime = Math.sqrt((4 * Math.pow(Math.PI, 2) * Math.pow((sma), 3)) / (AstroConst.Gconst * mass));
        ptime = MathUtils.round(ptime, 0);
        hour = (int) (ptime / 3600);
        min = (int) ((ptime - hour * 3600) / 60);
        sec = (int) (ptime - hour * 3600 - min * 60);
        va = Math.sqrt(2 * AstroConst.Gconst * mass * (peri) / (apo * (radius + ha + peri)));
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

    public double getEcc() {
        return ecc;
    }

    public double getHa() {
        return ha;
    }

    public double getHp() {
        return hp;
    }

    public double getSma() {
        return sma;
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

    public double getVa() {
        return va;
    }

    public double getVp() {
        return vp;
    }

    public double getPtime() {
        return ptime;
    }

    @Override
    public String toString() {
        return Utility.getFields(this);
    }
}
