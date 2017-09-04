package com.arny.celestiatools.utils;

import java.math.BigDecimal;
import java.util.Calendar;

import static com.arny.celestiatools.utils.MathUtils.*;
import static com.arny.celestiatools.utils.BaseUtils.*;

/**
 * @author i.sedoy
 */
public class AstroUtils {

    //    public static double getR1() {
//        double delim = a1 * (1 - Math.pow(e1, 2));
//        double delit = 1 + e1 * Cos(getTrueAnom(e1, ExcAnom(e1,M1)));
//        return delim / delit;
//    }
//
//    public static double getX1() {
//        return getR1() * (Cos(node1) * Cos(node1 + getTrueAnom(e1, ExcAnom(e1,M1))) - Sin(node1) * Sin(node1 + getTrueAnom(e1, ExcAnom(e1,M1))) * Cos(i1));
//    }
//
//    public static double getY1() {
//        return getR1() * (Sin(node1) * Cos(node1 + getTrueAnom(e1, ExcAnom(e1,M1))) - Cos(node1) * Sin(node1 + getTrueAnom(e1, ExcAnom(e1,M1))) * Cos(i1));
//    }
//
//    public static double getZ1() {
//        return getR1() * Sin(node1 + getTrueAnom(e1, ExcAnom(e1,M1))) * Sin(i1);
//    }
//
//    public static double getR2() {
//        double delim = a2 * (1 - Math.pow(e2, 2));
//        double delit = 1 + e2 * Cos(getTrueAnom(e2, ExcAnom(e2,M2)));
//        return delim / delit;
//    }
//
//    public static double getX2() {
//        return getR2() * (Cos(node2) * Cos(node2 + getTrueAnom(e2, ExcAnom(e2,M2))) - Sin(node2) * Sin(node2 + getTrueAnom(e2, ExcAnom(e2,M2))) * Cos(i2));
////        return getR2() * Cos(ExcAnom(e2,M2));
//    }
//
//    public static double getY2() {
//        return getR2() * (Sin(node2) * Cos(node2 + getTrueAnom(e1, ExcAnom(e2,M2))) - Cos(node2) * Sin(node2 + getTrueAnom(e2, ExcAnom(e2,M2))) * Cos(i2));
////        return getR1() * Sin(ExcAnom(e1,M1)) ;
//    }
//
//    public static double getZ2() {
//        return getR2() * Sin(node2 + getTrueAnom(e2, ExcAnom(e2,M2))) * Sin(i2);
////        return 0;
//    }
//public static double getMOID() {
//    double tmp1 = Math.pow((getX1() - getX2()), 2);
//    double tmp2 = Math.pow((getY1() - getY2()), 2);
//    double tmp3 = Math.pow((getZ1() - getZ2()), 2);
//    return Math.sqrt(tmp1 + tmp2 + tmp3);
//}

    public enum DistanceTypes {
        metre, km, AU, LY, PC
    }

    public static double DistanceConvert(double distance, DistanceTypes input, DistanceTypes output) {
        double res = 0;
        switch (input) {
            case metre:
                switch (output) {
                    case metre:
                        res = distance;
                        break;
                    case km:
                        res = distance / 1000;
                        break;
                    case AU:
                        res = distance / AstroConst.AU;
                        break;
                    case LY:
                        res = distance / AstroConst.LY;
                        break;
                    case PC:
                        res = distance / AstroConst.PC;
                        break;
                }
                break;
            case km:
                switch (output) {
                    case metre:
                        res = distance * 1000;
                        break;
                    case km:
                        res = distance;
                        break;
                    case AU:
                        res = (distance * 1000) / AstroConst.AU;
                        break;
                    case LY:
                        res = (distance * 1000) / AstroConst.LY;
                        break;
                    case PC:
                        res = (distance * 1000) / AstroConst.PC;
                        break;
                }
                break;
            case AU:
                switch (output) {
                    case metre:
                        res = distance * AstroConst.AU;
                        break;
                    case km:
                        res = (distance * AstroConst.AU) / 1000;
                        break;
                    case AU:
                        res = distance;
                        break;
                    case LY:
                        res = (distance * AstroConst.AU) / AstroConst.LY;
                        break;
                    case PC:
                        res = (distance * AstroConst.AU) / AstroConst.PC;
                        break;
                }
                break;
            case PC:
                switch (output) {
                    case metre:
                        res = distance * AstroConst.PC;
                        break;
                    case km:
                        res = (distance * AstroConst.PC) / 1000;
                        break;
                    case AU:
                        res = (distance * AstroConst.PC) / AstroConst.AU;
                        break;
                    case LY:
                        res = (distance * AstroConst.PC) / AstroConst.LY;
                        break;
                    case PC:
                        res = distance;
                        break;
                }
                break;
        }
        return res;
    }

    /**
     * Вычисление юлианской даты
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static double JD(int year, int month, double day) {
        int ytmp, mtmp;
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

    public static double dayFromJD(double JD) {
        long timestimp = DateFromJD(JD);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestimp);
        double mS = cal.get(Calendar.SECOND) / 60;
        double mD = (mS + cal.get(Calendar.MINUTE)) / 60;
        double h = (mD + cal.get(Calendar.HOUR)) / 24;
        return cal.get(Calendar.DAY_OF_MONTH) + h;
    }

    /**
     * JD->YMD.d
     *
     * @param JD
     * @return
     */
    public static String YMDd(double JD) {
        long timestimp = DateFromJD(JD);
        String sec = getDateTime(timestimp, "ss");
        String min = getDateTime(timestimp, "mm");
        String hrs = getDateTime(timestimp, "HH");
        String days = getDateTime(timestimp, "dd");
        String mth = getDateTime(timestimp, "MM");
        String yrs = getDateTime(timestimp, "yyyy");
        String Z = getDateTime(timestimp, "Z");
        if (!empty(sec) && !empty(min) && !empty(hrs) && !empty(days) && !empty(mth) && !empty(yrs) && !empty(Z)) {
            double dSec = Double.parseDouble(sec) / 60;
            double mMin = (dSec + Double.parseDouble(min)) / 60;
            double timeZ = Double.parseDouble(Z) / 100;
            double mHrs = Double.parseDouble(hrs);
            double dHr = (mMin + (mHrs - timeZ)) / 24;
            int mY = Integer.parseInt(yrs);
            int mM = Integer.parseInt(mth);
            int mD = Integer.parseInt(days);
            String dDay = String.valueOf(fracal(round(dHr, 6)));
            dDay = dDay.substring(1, dDay.length());
            return String.valueOf(mY).concat(BaseUtils.pad(mM)).concat(String.valueOf(mD)).concat(dDay);
        } else {
            return "0";
        }
    }

    /**
     * Вычисление юлианской даты
     *
     * @param epochMillis long
     * @return double
     */
    public static double JD(long epochMillis) {
        double epochDay = epochMillis / 86400000d;
        return epochDay + 2440587.5d;
    }

    public static double MJD(double JD) {
        return JD - 2400000.5;
    }

    /**
     * Вычисление даты от юлианской даты
     *
     * @param JD
     * @return
     */
    public static long DateFromJD(double JD) {
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
        return -1;
    }

    /**
     * Вычисление радиуса астеройда по апсолютной магнитюде
     *
     * @param magn
     * @param albedo
     * @return
     */
    public static double getRadiusFromAbsoluteMagn(double magn, double albedo) {
        double logr = Math.log10(albedo);
        double step = 0.5 * (6.259 - logr - (0.4 * magn));
        double result = Math.pow(10, step) / 2;
        return BaseUtils.roundUp(result, 3).doubleValue();
    }

    /**
     * Вычисление пасхи католической
     *
     * @param year год
     * @return дата
     */
    public static String getCatholicPasha(int year) {
        double a = getOstatok(year, 19);
        double b = getCeloe(year, 100);
        double c = getOstatok(year, 100);
        double d = getCeloe(b, 4);
        double e = getOstatok(b, 4);
        double f = getCeloe(b + 8, 25);
        double g = getCeloe(b - f + 1, 3);
        double h = getOstatok((19 * a) + b - d - g + 15, 30);
        double i = getCeloe(c, 4);
        double k = getOstatok(c, 4);
        double l = getOstatok(32 + (2 * e) + (2 * i) - h - k, 7);
        double m = getCeloe(a + (11 * h) + (22 * l), 451);
        double n = getCeloe(h + l - (7 * m) + 114, 31);
        double p = getOstatok(h + l - (7 * m) + 114, 31);
        double date = p + 1;
        return String.valueOf((int) date) + " " + String.valueOf((int) n);
    }

    /**
     * Вычисление пасхи православной
     *
     * @param year год
     * @return дата
     */
    public static String getPashaPravoslavDate(int year) {
        int a = (int) getOstatok(year, 19);
        int b = (int) getOstatok(year, 4);
        int c = (int) getOstatok(year, 7);
        int d = (int) getOstatok((19 * a) + 15, 30);
        int e = (int) getOstatok((2 * b) + (4 * c) + (6 * d) + 6, 7);
        int f = d + e;
        if (f <= 9) {
            String day = String.valueOf(22 + f);
            String month = "03";
            String Y = String.valueOf(year);
            String datetime = day + " " + month + " " + Y;
            long dt = DateTimeUtils.convertTimeStringToLong(datetime, "dd MM yyyy");
            long res = DateTimeUtils.addDays(dt, 13);
            return DateTimeUtils.getDateTime(res, "dd MMM yyyy");
        } else {
            String day = String.valueOf(f - 9);
            String month = "04";
            String Y = String.valueOf(year);
            String datetime = day + " " + month + " " + Y;
            long dt = DateTimeUtils.convertTimeStringToLong(datetime, "dd MM yyyy");
            long res = DateTimeUtils.addDays(dt, 13);
            return DateTimeUtils.getDateTime(res, "dd MMM yyyy");
        }
    }


    private static int getCeloe(double first, double second) {
        return MathUtils.intact(first / second);
    }

    private static double getOstatok(double first, double second) {
        return MathUtils.round((MathUtils.fracal((first / second)) * second), 0);
    }


    public static double ExcAnom(double e, double M) {
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

    public static double getTrueAnom(double e, double E) {
        double sqrt = Sqrt((1 + e) / (1 - e));
        double tg = Tan(0.5 * E);
        double tmp0 = sqrt * tg;
        return 2 * Atan(tmp0);
    }

    /**
     * Юлианское столетие
     *
     * @param JD
     * @return
     */
    public static double JDT(double JD) {
        return (JD - 2415020) / 36525;
    }

    /**
     * Растояние в перигелии
     *
     * @param sma
     * @param e
     * @return
     */
    public static double PerigDist(double sma, double e) {
        return sma * (1 - e);
    }

    /**
     * Mean motion, n (degrees/day)
     *
     * @param sma
     * @return
     */
    public static double MeanMotion(double sma) {
        return 0.985609 / (sma * Math.sqrt(sma));
    }

    /**
     * Расстояние в афелии
     *
     * @param sma
     * @param e
     * @return
     */
    public static double ApogDist(double sma, double e) {
        return sma * (1 + e);
    }

    public static double getEarthMeanLongitude(double T) {
        double A = 99.69668;
        double B = 36000.76892 * T;
        double C = 0.0003025 * Math.pow(T, 2);
        double res = A + B + C;
        while (true) {
            if (res > 360) {
                res -= 360;
            } else {
                break;
            }
        }
        return res;
    }

    public static double getEarthExcentricity(double T) {
        double A = 0.01675104;
        double B = 0.0000418 * T;
        double C = 0.000000126 * Math.pow(T, 2);
        return A - B - C;
    }

    public static double getEarthMeanAnomaly(double T) {
        double A = 358.47583;
        double B = 35999.04975 * T;
        double C = 0.000150 * Math.pow(T, 2);
        double D = 0.0000033 * Math.pow(T, 3);
        double res = A + B - C - D;
        while (true) {
            if (res > 360) {
                res -= 360;
            } else {
                break;
            }
        }
        return res;
    }

    public static double getEarthPericenter(double T) {
        return getEarthMeanLongitude(T) - getEarthMeanAnomaly(T);
    }

    public static double getTrueAnomaly(double e, double Exc) {
        double v = ((1 + e) / (1 - e)) * 1 / 2 * Tan(Exc / 2);
        return Atan(v) * 2;
    }

    public static double getArgLat(double v, double peri) {
        return v + peri;
    }

    public static double getGeocentrDist(double sma, double e, double v) {
        return sma * (1 - Exp(e, 2)) / (1 + e * Cos(v));
    }

    public static double getGrad(int grad, int min, double sec) {
        GradMinSec gradMinSec = new GradMinSec(grad, min, sec);
        if (gradMinSec.getGrad() < 0 || gradMinSec.getMin() < 0 || gradMinSec.getSec() < 0) {
            gradMinSec.setSign(-1);
        }
        return getGrad(gradMinSec);
    }

    /**
     * Преобразует градусы минуты секунды в градусы
     * @param gradMinSec
     * @return
     */
    public static double getGrad(GradMinSec gradMinSec) {
        int sign = gradMinSec.getSign();
        double D = Math.abs(gradMinSec.getGrad());
        double M = Math.abs(gradMinSec.getMin());
        double S = Math.abs(gradMinSec.getSec());
        return sign * (D + (M / 60) + (S / 3600));
    }

    public enum AngleFormat {
        Dd,
        DMm,
        DMSs,
    }

    public static GradMinSec getGradMinSec(double grad) {
        int sign = 1;
        if (grad < 0) {
            sign = -1;
        }
        double x = Math.abs(grad);
        int D = (int) x;
        double y = (x - D) * 60;
        int M = (int) y;
        double z = (y - M) * 60;
        GradMinSec gradMinSec = new GradMinSec(x,y,z);
        gradMinSec.setSign(sign);
        return gradMinSec;
    }

    public static String getGradMinSec(GradMinSec gradMinSec, AngleFormat format) {
        int sign = gradMinSec.getSign();
        double x =gradMinSec.getGrad();
        int D = (int) x;
        double y = gradMinSec.getMin();
        int M = (int) y;
        double z = gradMinSec.getSec();
        double S = round(z, 2);
        switch (format) {
            case Dd:
                return "" + sign * x + "\u00B0 " ;
            case DMm:
                return "" + sign * D + "\u00B0 " + round(y, 2) + "\u0027";
            case DMSs:
                return "" + sign * D + "\u00B0 " + M + "\u0027 " + S + "\"";
            default:
                return "" + sign * x + "\u00B0 ";
        }
    }

    public static String getGradMinSec(double grad, AngleFormat format) {
        int sign = 1;
        if (grad < 0) {
            sign = -1;
        }
        double x = Math.abs(grad);
        int D = (int) x;
        double y = (x - D) * 60;
        int M = (int) y;
        double z = (y - M) * 60;
        double S = round(z, 2);
        switch (format) {
            case Dd:
                return "" + sign * x + "\u00B0 " ;
            case DMm:
                return "" + sign * D + "\u00B0 " + round(y, 2) + "\u0027";
            case DMSs:
                return "" + sign * D + "\u00B0 " + M + "\u0027 " + S + "\"";
            default:
                return "" + sign * x + "\u00B0 ";
        }
    }

}