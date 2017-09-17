package com.arny.celestiatools.utils.astronomy;

import com.arny.celestiatools.models.CoordXYZ;
import com.arny.celestiatools.utils.BaseUtils;
import com.arny.celestiatools.utils.DateTimeUtils;
import com.arny.celestiatools.utils.GradMinSec;
import com.arny.celestiatools.utils.MathUtils;

import java.util.Calendar;

import static com.arny.celestiatools.utils.MathUtils.*;
import static com.arny.celestiatools.utils.BaseUtils.*;

/**
 * @author i.sedoy
 */
public class AstroUtils {

    public static final double TWILIGHT = 90.8333333333333;   // 90°50' (Сумерки)
    public static final double CIVIL_TWILIGHT = 96.0;         // 96°    (Гражданские сумерки)
    public static final double NAUTICAL_TWILIGHT = 102.0;     // 102°   (Навигационные сумерки)
    public static final double ASTRONOMICAL_TWILIGHT = 108.0; // 108°   (Астрономические сумерки)

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

    /**
     * Модифицированная Юлианская дата
     *
     * @param JD
     * @return
     */
    public static double MJD(double JD) {
        return JD - 2400000.5;
    }

    /**
     * Высокосный год
     *
     * @param year
     * @return
     */
    public static boolean isVisokos(int year) {
        return year % 4 == 0 && year % 100 != 0 && year % 400 == 0;
    }

    /**
     * Порядковый день года
     *
     * @param epochMillis
     * @return
     */
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

    public static double getGrad(int grad, int min, double sec, boolean correctTo360) {
        if (correctTo360) {
            grad = (int) correctAngle(grad, 360);
        }
        GradMinSec gradMinSec = new GradMinSec(grad, min, sec);
        if (gradMinSec.getGrad() < 0 || gradMinSec.getMin() < 0 || gradMinSec.getSec() < 0) {
            gradMinSec.setSign(-1);
        }
        return getGrad(gradMinSec);
    }

    /**
     * Преобразует градусы минуты секунды в градусы
     *
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

    /**
     * Получаем градусы минуты секунды
     *
     * @param grad
     * @return
     */
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
        GradMinSec gradMinSec = new GradMinSec(x, y, z);
        gradMinSec.setSign(sign);
        return gradMinSec;
    }

    /**
     * Получаем градусы минуты секунды
     *
     * @param gradMinSec
     * @param format
     * @return
     */
    public static String getGradMinSec(GradMinSec gradMinSec, AngleFormat format) {
        int sign = gradMinSec.getSign();
        double x = gradMinSec.getGrad();
        int D = (int) x;
        double y = gradMinSec.getMin();
        int M = (int) y;
        double z = gradMinSec.getSec();
        double S = round(z, 2);
        switch (format) {
            case Dd:
                return "" + sign * x + "\u00B0 ";
            case DMm:
                return "" + sign * D + "\u00B0 " + round(y, 2) + "\u0027";
            case DMSs:
                return "" + sign * D + "\u00B0 " + M + "\u0027 " + S + "\"";
            default:
                return "" + sign * x + "\u00B0 ";
        }
    }

    /**
     * Получаем градусы минуты секунды
     *
     * @param gradMinSec
     * @return
     */
    public static String getGradMinSec(GradMinSec gradMinSec) {
        int sign = gradMinSec.getSign();
        double x = gradMinSec.getGrad();
        int D = (int) x;
        double y = gradMinSec.getMin();
        int M = (int) y;
        double z = gradMinSec.getSec();
        double S = round(z, 3);
        return sign * D + " " + M + " " + S;
    }

    /**
     * Получаем градусы минуты секунды
     *
     * @param grad
     * @param format
     * @return
     */
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
                return "" + sign * x + "\u00B0 ";
            case DMm:
                return "" + sign * D + "\u00B0 " + round(y, 2) + "\u0027";
            case DMSs:
                return "" + sign * D + "\u00B0 " + M + "\u0027 " + S + "\"";
            default:
                return "" + sign * x + "\u00B0 ";
        }
    }

    /**
     * Детсятичные часы в часы и минуты
     *
     * @param Hh
     * @return
     */
    public static String getHHmm(double Hh) {
        int sign = Hh > 0 ? 1 : -1;
        int H = (int) Math.abs(Hh);
        double y = (Math.abs(Hh) - H) * 60;
        int M = (int) y;
        return sign * H + ":" + pad(M);
    }

    /**
     * Часы минуты в десятичные часы
     *
     * @param hh
     * @param min
     * @return
     */
    public static double getHh(int hh, int min) {
        int sign = hh > 0 ? 1 : -1;
        double y = min / 60;
        return sign * (Math.abs(hh) + y);
    }

    /**
     * Восход/заход солнца
     *
     * @param Zenith
     * @param timestamp
     * @param Lat
     * @param Lon
     * @return
     */
    public static String getSunsetRise(long timestamp, double Lat, double Lon, boolean rise, double Zenith) {
        // 1. first calculate the day of the year
        double N = dayOfYear(timestamp);
        //2. convert the longitude to hour value and calculate an approximate time
        double LngHour = Lon / 15;
        double t = 0;
        t = rise ? N + ((6 - LngHour) / 24) : N + ((18 - LngHour) / 24);
        // 3. calculate the Sun's mean anomaly
        double M = (0.9856 * t) - 3.289;
        // 4. calculate the Sun's true longitude
        //    [Note throughout the arguments of the trig functions
        //    (sin, tan) are in degrees. It will likely be necessary to
        //    convert to radians. eg sin(170.626 deg) =sin(170.626*pi/180
        //    radians)=0.16287]
        double L = M + (1.916 * Sin(M)) + (0.020 * Sin(2 * M)) + 282.634;
        // NOTE: L potentially needs to be adjusted into the range [0,360) by adding/subtracting 360
        L = correctAngle(L, 360);
        // 5a. calculate the Sun's right ascension
        double RA = (Atan(0.91764 * Tan(L)));
        // NOTE: RA potentially needs to be adjusted into the range [0,360) by adding/subtracting 360
        RA = correctAngle(RA, 360);
        // 5b. right ascension value needs to be in the same quadrant as L
        double Lquadrant = round((L / 90), 0) * 90;
        double RAquadrant = round((RA / 90), 0) * 90;
        RA = RA + (Lquadrant - RAquadrant);
        // 5c. right ascension value needs to be converted into hours
        RA = RA / 15;
        // 6. calculate the Sun's declination
        double sinDec = 0.39782 * Sin(L);
        double cosDec = Cos(Asin(sinDec));
        // 7a. calculate the Sun's local hour angle
        double HCos = (Cos(Zenith) - (sinDec * Sin(Lat))) / (cosDec * Cos(Lat));
        if ((HCos > 1) || (HCos < -1)) {
            return "";
        }

        // 7b. finish calculating H and convert into hours
        // if  rising time is desired:
        // H := 360 - RadToDeg( ArcCos( HCos ) );
        // if setting time is desired:
        // H := RadToDeg( ArcCos( HCos ) );
        double H = rise ? 360 - Acos(HCos) : Acos(HCos);
        H = H / 15;
        // 8. calculate local mean time of rising/setting
        double LocalT = H + RA - (0.06571 * t) - 6.622;
        String hHmm0 = getHHmm(LocalT);
        // 9. adjust back to UTC
        double UT = LocalT - LngHour;
        // NOTE: UT potentially needs to be adjusted into the range [0,24) by adding/subtracting 24
        UT = correctAngle(UT, 24);
        String hHmm1 = getHHmm(UT);
        // 10. convert UT value to local time zone of latitude/longitude
        String x = DateTimeUtils.getDateTime(timestamp, "X");
        double Result = UT + Double.parseDouble(x);
        String hHmm = getHHmm(Result);
        return hHmm;
    }

    /**
     * Преобразование полярных координат в прямоугольные
     *
     * @param radius
     * @param theta [-90...+90]
     * @param phi   [-360...+360]
     * @return
     */
    public static CoordXYZ getCart(double radius, double theta, double phi) {
        double rcst = radius * Cos(theta);
        double x = rcst * Cos(phi);
        double y = rcst * Sin(phi);
        double z = radius * Sin(theta);
        return new CoordXYZ(x, y, z);
    }

    /**
     * Вычисление позици солнца
     */
    public static String getSunPos(long timestamp, double Lat, double Lon) {
        double jd = JD(timestamp);
        double mjd = MJD(jd);
        double T0 = getT0(mjd);
        double S0 = getS0(T0);
        double UT = getUt(timestamp);
        double Nsec = UT * 3600; //‘ количество секунд, прошедших  от начала суток до момента наблюдения
        double NsecS = (Nsec * 366.2422) / 365.2422;// количество звездных секунд
        double SG = (S0 + NsecS) / 3600 * 15;
        SG = correctAngle(SG, 360);
        double ST = SG + Lon;
        // UT – время в часах от полуночи даты
        EclipticCoord eclipticCoord = new EclipticCoord(T0, UT).getCoords();
        double X = eclipticCoord.getX();
        double Y = eclipticCoord.getY();
        double Z = eclipticCoord.getZ();
        PryamougEcvatorCoords pryamougEcvatorCoords = new PryamougEcvatorCoords(X, Y, Z).getCoods();
        double Yp = pryamougEcvatorCoords.getYp();
        double Xp = pryamougEcvatorCoords.getXp();
        double Zp = pryamougEcvatorCoords.getZp();
        //Экваториальные геоцентрические координаты Солнца
        double Ra = Atan(Yp / Xp);
        double Dec = Atan(Zp / Sqrt(Math.pow(Xp, 2) + Math.pow(Yp, 2)));
        double Az = getAz(Lat, ST, Ra, Dec);
        return "Ra:" + Ra + " Dec:" + Dec + " Az:" + Az;
    }

    public static double correctAngle(double angle, int num) {
        while (angle >= num) {
            angle -= num;
        }
        return angle;
    }

    /**
     * Азимутальные координаты Солнца
     *
     * @param Lat широта
     * @param ST  местное звездное время
     * @param ra
     * @param dec
     * @return
     */
    private static double getAz(double Lat, double ST, double ra, double dec) {
        double Th = ST - ra; // ‘ часовой угол
        double z = Acos(Sin(Lat) * Sin(dec) + Cos(Lat) * Cos(dec) * Cos(Th));
        double H = 90 - z;
        double atan = Atan2(Sin(Th) * Cos(dec) * Cos(Lat), (Sin(H) * Sin(Lat) - Sin(dec)));
        return atan;
    }

    /**
     * UT - всемирное время в часах, момент расчета
     *
     * @param timestamp
     * @return
     */
    private static double getUt(long timestamp) {
        double hous = Double.parseDouble(DateTimeUtils.getDateTime(timestamp, "HH"));
        double min = Double.parseDouble(DateTimeUtils.getDateTime(timestamp, "mm"));
        double sec = Double.parseDouble(DateTimeUtils.getDateTime(timestamp, "ss"));
        double zona = Double.parseDouble(DateTimeUtils.getDateTime(timestamp, "X"));
        double UT = hous - zona + min / 60 + sec / 3600;
        if (UT > 24) UT = UT - 24;
        if (UT < 0) UT = UT + 24;
        return UT;
    }

    /**
     * Вычисление местного звездного времени
     *
     * @param t0 мод.юл.дата на начало суток в юлианских столетиях
     * @return
     */
    private static double getS0(double t0) {
        double a1 = 24110.54841;
        double a2 = 8640184.812;
        double a3 = 0.093104;
        double a4 = 0.0000062;
        return a1 + (a2 * t0) + (a3 * (Math.pow(t0, 2))) - (a4 * Math.pow(t0, 3));
    }

    /**
     * мод.юл.дата на начало суток в юлианских столетиях
     *
     * @param mjd мод.юл.дата
     * @return
     */
    private static double getT0(double mjd) {
        return (mjd - 51544.5) / 36525;
    }

    /**
     * Эклиптических координат Солнца
     */
    private static class EclipticCoord {
        private double t0;
        private double ut;
        private double x;
        private double y;
        private double z;

        public EclipticCoord(double t0, double UT) {
            this.t0 = t0;
            ut = UT;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        /**
         * Вычисление эклиптических координат Солнца
         *
         * @return
         */
        public EclipticCoord getCoords() {
            double M = 357.528 + 35999.05 * t0 + 0.04107 * ut;//‘ средняя аномалия
            double L0 = 280.46 + 36000.772 * t0 + 0.04107 * ut;
            M = correctAngle(M, 360);
            double L = L0 + (1.915 - 0.0048 * t0) * Sin(M) + 0.02 * Sin(2 * M);  //‘ долгота Солнца
            L = correctAngle(L, 360);
            x = Cos(L);
            y = Sin(L);
            z = 0;
            return this;
        }
    }

    private static class PryamougEcvatorCoords {
        private double x;
        private double y;
        private double z;
        private double xp;
        private double yp;
        private double zp;

        public PryamougEcvatorCoords(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getXp() {
            return xp;
        }

        public double getYp() {
            return yp;
        }

        public double getZp() {
            return zp;
        }

        /**
         * Координаты Cолнца в прямоугольной экваториальной системе координат
         *
         * @return
         */
        public PryamougEcvatorCoords getCoods() {
            double Eps = 23.439281;//   ‘ наклон эклиптики к экватору
            xp = x;
            yp = y * Cos(Eps) - z * Sin(Eps);
            zp = y * Sin(Eps) + z * Cos(Eps);
            return this;
        }
    }
}