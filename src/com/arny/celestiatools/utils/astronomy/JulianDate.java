package com.arny.celestiatools.utils.astronomy;


import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class JulianDate {
    public static DatePosition getDate(double jd, DatePosition calendar) {
        DatePosition dp = calendar.copy();
        dp.setDateTime(JulianDate.DateFromJD(jd));
        return dp;
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
     * Вычисление юлианской даты
     * @return double
     */
    public static double getJD(DatePosition datePosition) {
        double epochDay = datePosition.getTimeInMillis() / 86400000d;
        return epochDay + 2440587.5d;
    }

    public static double getJD0UT(DatePosition datePosition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datePosition.getTimeInMillis());
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        return JD(calendar.getTimeInMillis());
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

    public static double getJD0UT(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        return JD(calendar.getTimeInMillis());
    }
}
