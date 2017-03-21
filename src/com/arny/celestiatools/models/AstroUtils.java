package com.arny.celestiatools.models;

/**
 * @author i.sedoy
 */
public class AstroUtils {
    public static final long AU = 149597870L;
    public static final double EARTH_SMA = 1.0000002;
    public static final double EARTH_T = 1.00004;
    public static final double EARTH_DOLG_1980 = 98.833540;
    public static final double EARTH_PERIC = 102.596403;
    private static double a1, e1, i1, peri1, node1, M1;
    private static double a2, e2, i2, peri2, node2, M2;

    public static double getA1() {
        return a1;
    }

    public static double getE1() {
        return e1;
    }

    public static double getA2() {
        return a2;
    }

    public static double getE2() {
        return e2;
    }

    public static void setA1(double a1) {
        AstroUtils.a1 = a1;
    }

    public static void setE1(double e1) {
        AstroUtils.e1 = e1;
    }

    public static void setI1(double i1) {
        AstroUtils.i1 = i1;
    }

    public static void setPeri1(double peri1) {
        AstroUtils.peri1 = peri1;
    }

    public static void setNode1(double node1) {
        AstroUtils.node1 = node1;
    }

    public static void setM1(double M1) {
        AstroUtils.M1 = M1;
    }

    public static void setA2(double a2) {
        AstroUtils.a2 = a2;
    }

    public static void setE2(double e2) {
        AstroUtils.e2 = e2;
    }

    public static void setI2(double i2) {
        AstroUtils.i2 = i2;
    }

    public static void setPeri2(double peri2) {
        AstroUtils.peri2 = peri2;
    }

    public static void setNode2(double node2) {
        AstroUtils.node2 = node2;
    }

    public static void setM2(double M2) {
        AstroUtils.M2 = M2;
    }

    public static double Cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public static double Sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public static double Tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    public static double Atan(double rad) {
        return Math.toDegrees(Math.atan(rad));
    }

    public static double Pow(double num,double exp) {
        return Math.pow(num,exp);
    }

    public static double getR1() {
        double delim = a1 * (1 - Math.pow(e1, 2));
        double delit = 1 + e1 * Cos(getExc(e1,M1));
        return delim / delit;
    }

    public static double getX1() {
        return getR1() * (Cos(node1) * Cos(node1 + getExc(e1,M1)) - Sin(node1) * Sin(node1 + getExc(e1,M1)) * Cos(i1));
    }

    public static double getY1() {
        return getR1() * (Sin(node1) * Cos(node1 + getExc(e1,M1)) - Cos(node1) * Sin(node1 + getExc(e1,M1)) * Cos(i1));
    }

    public static double getZ1() {
        return getR1() * Sin(node1 + getExc(e1,M1)) * Sin(i1);
    }

    public static double getR2() {
        double delim = a2 * (1 - Math.pow(e2, 2));
        double delit = 1 + e2 * Cos(getExc(e2,M2));
        return delim / delit;
    }

    public static double getX2() {
        return getR2() * (Cos(node2) * Cos(node2 + getExc(e2,M2)) - Sin(node2) * Sin(node2 + getExc(e2,M2)) * Cos(i2));
//        return getR2() * Cos(getExc(e2,M2));
    }

    public static double getY2() {
        return getR2() * (Sin(node2) * Cos(node2 + getExc(e2,M2)) - Cos(node2) * Sin(node2 + getExc(e2,M2)) * Cos(i2));
//        return getR1() * Sin(getExc(e1,M1)) ;
    }

    public static double getZ2() {
        return getR2() * Sin(node2 + getExc(e2,M2)) * Sin(i2);
//        return 0;
    }

    /**
     * Вычисление юлианской даты
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static double getJD(int year, int month, double day) {
        int ytmp = 0, mtmp = 0;
        if (month == 1 || month == 2) {
            ytmp = year - 1;
            mtmp = month + 12;
        } else {
            ytmp = year;
            mtmp = month;
        }
        int A = ytmp / 100;
        int B = 2 - A + (int) (A / 4);
        int C = (int) (365.25 * ytmp);
        int D = (int) (30.6001 * (mtmp + 1));
        return B + C + D + day + 1720994.5;
    }

    /**
     * Вычисление юлианской даты
     *
     * @param epochMillis long
     * @return double
     */
    public static double getJD(long epochMillis) {
        double epochDay = epochMillis / 86400000d;
        return epochDay + 2440587.5d;
    }

    /**
     * Вычисление даты от юлианской даты
     *
     * @param JD
     * @return
     */
    public static long getDateFromJD(double JD) {
        double epochDay = JD - 2440587.5d;
        return (long) (epochDay * 86400000d);
    }

    public static boolean isVisokos(int year) {
        return year % 4 == 0 && year % 100 != 0 && year % 400 == 0;
    }

    public static int dayOfYear(long epochMillis) {
        String strDay = BaseUtils.getDateTime(epochMillis, "D");
        if (strDay != null) {
            return Integer.parseInt(strDay);
        }
        return 0;
    }

    public static double getRadiusFromAbsoluteMagn(double magn, double albedo) {
        double logr = Math.log10(albedo);
        double step = 0.5 * (6.259 - logr - (0.4 * magn));
        double result = Math.pow(10, step) / 2;
        return BaseUtils.roundUp(result, 3).doubleValue();
    }

    public static double getMOID() {
        double tmp1 = Math.pow((getX1() - getX2()), 2);
        double tmp2 = Math.pow((getY1() - getY2()), 2);
        double tmp3 = Math.pow((getZ1() - getZ2()), 2);
        return Math.sqrt(tmp1 + tmp2 + tmp3);
    }

    public static double getExc(double e, double M) {
        double em = e * 180 / Math.PI;
        double E = M, E1;
        while (true) {
            E1 = E;
            double tmp1 = M + (em * Sin(E)) - E;
            double tmp2 = 1 - e * Cos(E);
            E = E + tmp1 / tmp2;
            double delta = Math.abs(E1 - E);
            if (delta < 1.0E-12) {
                break;
            }
        }
        return E;
    }

    public static double getJDT(double JD) {
        return (JD - 2415020) / 36525;
    }

    public static double getPerigDist(double sma, double e) {
        return sma * (1 - e);
    }

    /**
     * Mean motion, n (degrees/day)
     * @param sma
     * @return
     */
    public static double getMeanMotion(double sma){
        return  0.985609 / (sma * Math.sqrt(sma));
    }

    public static double getAfelDist(double sma, double e) {
        return sma * (1 + e);
    }

    public static double getEarthMeanLongitude(double T){
        double A = 99.69668;
        double B = 36000.76892 * T;
        double C = 0.0003025 * Math.pow(T,2);
        double res = A + B + C;
        while (true){
            if (res > 360) {
                res-=360;
            }else{
                break;
            }
        }
        return res;
    }

    public static double getEarthExcentricity(double T){
        double A = 0.01675104;
        double B = 0.0000418 * T;
        double C = 0.000000126 * Math.pow(T,2);
        return A - B - C;
    }

    public static double getEarthMeanAnomaly(double T){
        double A = 358.47583;
        double B = 35999.04975 * T;
        double C = 0.000150 * Math.pow(T,2);
        double D = 0.0000033 * Math.pow(T,3);
        double res =  A + B - C - D;
        while (true){
            if (res > 360) {
                res-=360;
            }else{
                break;
            }
        }
        return res;
    }

    public static double getEarthPericenter(double T){
        return getEarthMeanLongitude(T) - getEarthMeanAnomaly(T);
    }

    public static double getTrueAnomaly(double e,double Exc){
        double v =  ((1 + e) / (1 - e)) * 1 / 2 * Tan(Exc / 2);
        return Atan(v) * 2;
    }

    public static double getArgLat(double v,double peri){
        return v + peri;
    }

    public static double getGeocentrDist(double sma,double e,double v){
        return sma * (1 -Pow(e,2) ) / (1 + e * Cos(v));
    }

}