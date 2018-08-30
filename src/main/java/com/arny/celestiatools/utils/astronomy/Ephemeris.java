package com.arny.celestiatools.utils.astronomy;

import com.arny.celestiatools.utils.DateTimeUtils;
import com.arny.celestiatools.utils.MathUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class Ephemeris {
    public static final double PI = Math.PI;
    public static final double PI2 = Math.PI * 2;
    public static final double PID2 = 1.5707963267948966d;
    public static final float PIf = 3.1415927f;
    public static final double notVisible = -9999999.0d;

    public static double getCenturiesSince1900(double jd) {
        return (jd - 2415020.0d) / 36525.0d;
    }

    public static double getCenturiesSince2000(double jd) {
        return (jd - 2451545.0d) / 36525.0d;
    }

    public static double getFractionalTime(int hour, int minute, int second) {
        return (((double) hour) + (((double) minute) / 60.0d)) + (((double) second) / 3600.0d);
    }

    public static double getFractionalTime(Calendar calendar) {
        return (((double) calendar.get(Calendar.HOUR_OF_DAY) + (((double) calendar.get(Calendar.MINUTE) / 60.0d)) + (((double) calendar.get(Calendar.SECOND) / 3600.0d))));
    }

    public static double getLightTimeDays(double distanceAU) {
        return 0.0057755183d * distanceAU;
    }

    public static double getSiderealTime(DatePosition datePosition) {
        double jdUT0 = JulianDate.getJD0UT(datePosition.getDateTime());
        double lon = datePosition.getLongitudeDeg();
        double T = (jdUT0 - 2451545.0d) / 36525.0d;
        double v = (((280.46061837d + lon) + (360.98564736629d * (JulianDate.JD(datePosition.getTimeInMillis()) - 2451545.0d))) + ((3.87933E-4d * T) * T)) - (((T * T) * T) / 3.871E7d);
        double sidTime = Math.toRadians(v) % PI2;
        if (sidTime >= PI2) {
            return sidTime - PI2;
        }
        if (sidTime < 0.0d) {
            return sidTime + PI2;
        }
        return sidTime;
    }

    public static double getSiderialTimeGreenwich0UT(long dateTime) {
        DateTime time = DateTimeUtils.getDateTime(dateTime).withZone(DateTimeZone.UTC);
        return getSiderealTime(new DatePosition(new DatePosition(time.getMillis()), new GeoLocation( "geo", 0.0f, 0.0f)));
    }

    public static void convertUTToDynamical(DatePosition datePosition) {
        datePosition.add(Calendar.SECOND, (int) getEphemerisTime(JulianDate.JD(datePosition.getTimeInMillis())));
    }

    public static void convertDynamicalToUT(DatePosition datePosition) {
        datePosition.add(Calendar.SECOND, -((int) getEphemerisTime(JulianDate.JD(datePosition.getTimeInMillis()))));
    }

    public static double convertDynamicalToUT(double jd) {
        return jd - (getEphemerisTime(jd) / 86400.0d);
    }

    public static double getEphemerisTime(double jd) {
        if (jd >= 2451544.5d && jd < 2453371.5d) {
            return 65.0d;
        }
        if (jd >= 2453371.5d && jd < 2457023.5d) {
            return 69.0d;
        }
        if (jd >= 2457023.5d) {
            return 80.0d;
        }
        return 60.0d * getCenturiesSince1900(jd);
    }

    public static double getTimeBetween0and24(double t) {
        t %= 24.0d;
        if (t < 0.0d) {
            return t + 24.0d;
        }
        return t;
    }

    public static RiseSetEvent getTimeBetween0and24(RiseSetEvent t) {
        double time = t.getTime() % 24.0d;
        if (time < 0.0d) {
            time += 24.0d;
        }
        return new RiseSetEvent(time, t.getAzimuth(), t.getAltitude(), t.getType(), t.riseSetType);
    }

    public static double parallax2DistanceAU(double p) {
        return 6378.14d / (AstroConst.AU * Math.sin(p));
    }

    private static float getTimeZoneOffset(long dt) {
        String x = DateTimeUtils.getDateTime(dt, "Z");
        return Float.parseFloat(x);
    }

    public static RiseSetEvent getTransit(DatePosition datePosition, double ra1, double dec1, double ra2, double dec2, double ra3, double dec3, double h0) {
        double lon = datePosition.getLongitudeDeg();
        double lat = datePosition.getLatitudeDeg();
        double low = Math.toRadians(100.0d);
        double high = Math.toRadians(200.0d);
        if (ra1 > high && ra2 < low && ra3 < low) {
            ra1 -= PI2;
        } else if (ra1 > high && ra2 > high && ra3 < low) {
            ra3 += PI2;
        } else if (ra1 < low && ra2 > high && ra3 > high) {
            ra1 += PI2;
        } else if (ra1 < low && ra2 < low && ra3 > high) {
            ra3 -= PI2;
        }
        lon = Math.toRadians(-lon);
        lat = Math.toRadians(lat);
        double jd2 = JulianDate.getJD0UT(datePosition.getDateTime());
        double theta0 = getSiderialTimeGreenwich0UT(datePosition.getDateTime());
        double m0 = ((ra2 + lon) - theta0) / PI2;
        if (m0 < 0.0d) {
            m0 += 1.0d;
        }
        if (m0 > 1.0d) {
            m0 -= 1.0d;
        }
        double m = m0;
        datePosition.setJD(jd2 + m);
        if ((((double) getTimeZoneOffset(datePosition.getDateTime())) / 24.0d) + m < 0.0d) {
            m += 1.0d;
        } else if ((((double) getTimeZoneOffset(datePosition.getDateTime())) / 24.0d) + m > 1.0d) {
            m -= 1.0d;
        }
        double theta = theta0 + (Math.toRadians(360.985647d) * m);
        if (theta > PI2) {
            theta -= PI2;
        }
        double dT = getEphemerisTime(jd2);
        double H = (theta - lon) - Interpolation.interpolate(m + (dT / 86400.0d), ra1, ra2, ra3);
        if (H < -3.141592653589793d) {
            H += PI2;
        } else if (H > PI) {
            H -= PI2;
        }
        m += (-H) / PI2;
        theta = theta0 + (Math.toRadians(360.985647d) * m);
        if (theta > PI2) {
            theta -= PI2;
        }
        double n = m + (dT / 86400.0d);
        double raI = Interpolation.interpolate(n, ra1, ra2, ra3);
        double decI = Interpolation.interpolate(n, dec1, dec2, dec3);
        H = (theta - lon) - raI;
        if (H < -3.141592653589793d) {
            H += PI2;
        } else if (H > PI) {
            H -= PI2;
        }
        return new RiseSetEvent(((double) getTimeZoneOffset(datePosition.getDateTime())) + (24.0d * (m + ((-H) / PI2))), PI, getAltitude(lat, decI, H), 1, 0);
    }

    public static RiseSetEvent getRise(DatePosition datePosition, double ra1, double dec1, double ra2, double dec2, double ra3, double dec3, double h0) {
        double lon = (double) datePosition.getLongitudeDeg();
        double lat = (double) datePosition.getLatitudeDeg();
        double low = Math.toRadians(100.0d);
        double high = Math.toRadians(200.0d);
        if (ra1 > high && ra2 < low && ra3 < low) {
            ra1 -= PI2;
        } else if (ra1 > high && ra2 > high && ra3 < low) {
            ra3 += PI2;
        } else if (ra1 < low && ra2 > high && ra3 > high) {
            ra1 += PI2;
        } else if (ra1 < low && ra2 < low && ra3 > high) {
            ra3 -= PI2;
        }
        lon = Math.toRadians(-lon);
        lat = Math.toRadians(lat);
        double jd2 = JulianDate.getJD0UT(datePosition.getDateTime());
        double theta0 = getSiderialTimeGreenwich0UT(datePosition.getDateTime());
        double dummy = (Math.sin(h0) - (Math.sin(lat) * Math.sin(dec2))) / (Math.cos(lat) * Math.cos(dec2));
        double signumLat = Math.signum(lat);
        if (((signumLat * dec2) - PID2) + (signumLat * lat) > h0) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 0, 1);
        }
        if (((signumLat * lat) - PID2) - (signumLat * dec2) > (-h0)) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 0, 2);
        }
        double H0 = Math.acos(dummy);
        double m0 = ((ra2 + lon) - theta0) / PI2;
        if (m0 < 0.0d) {
            m0 += 1.0d;
        }
        if (m0 > 1.0d) {
            m0 -= 1.0d;
        }
        double m = m0 - (H0 / PI2);
        datePosition.setJD(jd2 + m);
        if ((((double) getTimeZoneOffset(datePosition.getDateTime())) / 24.0d) + m < 0.0d) {
            m += 1.0d;
        } else if ((((double) getTimeZoneOffset(datePosition.getDateTime())) / 24.0d) + m > 1.0d) {
            m -= 1.0d;
        }
        double theta = theta0 + (Math.toRadians(360.985647d) * m);
        if (theta > PI2) {
            theta -= PI2;
        }
        double dT = getEphemerisTime(jd2);
        double n = m + (dT / 86400.0d);
        double raI = Interpolation.interpolate(n, ra1, ra2, ra3);
        double decI = Interpolation.interpolate(n, dec1, dec2, dec3);
        if (((signumLat * decI) - PID2) + (signumLat * lat) > h0) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 2, 1);
        }
        if (((signumLat * lat) - PID2) - (signumLat * decI) > (-h0)) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 2, 2);
        }
        double H = (theta - lon) - raI;
        if (H < -3.141592653589793d) {
            H += PI2;
        } else if (H > PI) {
            H -= PI2;
        }
        m += (getAltitude(lat, decI, H) - h0) / (((PI2 * Math.cos(decI)) * Math.cos(lat)) * Math.sin(H));
        theta = theta0 + (Math.toRadians(360.985647d) * m);
        if (theta > PI2) {
            theta -= PI2;
        }
        n = m + (dT / 86400.0d);
        raI = Interpolation.interpolate(n, ra1, ra2, ra3);
        decI = Interpolation.interpolate(n, dec1, dec2, dec3);
        H = (theta - lon) - raI;
        if (H < -3.141592653589793d) {
            H += PI2;
        } else if (H > PI) {
            H -= PI2;
        }
        return new RiseSetEvent(((double) getTimeZoneOffset(datePosition.getDateTime())) + (24.0d * (m + ((getAltitude(lat, decI, H) - h0) / (((PI2 * Math.cos(decI)) * Math.cos(lat)) * Math.sin(H))))), getAzimuth(lat, decI, H), h0, 0, 0);
    }

    public static RiseSetEvent getSet(DatePosition datePosition, double ra1, double dec1, double ra2, double dec2, double ra3, double dec3, double h0) {
        double lon = (double) datePosition.getLongitudeDeg();
        double lat = (double) datePosition.getLatitudeDeg();
        double low = Math.toRadians(100.0d);
        double high = Math.toRadians(200.0d);
        if (ra1 > high && ra2 < low && ra3 < low) {
            ra1 -= PI2;
        } else if (ra1 > high && ra2 > high && ra3 < low) {
            ra3 += PI2;
        } else if (ra1 < low && ra2 > high && ra3 > high) {
            ra1 += PI2;
        } else if (ra1 < low && ra2 < low && ra3 > high) {
            ra3 -= PI2;
        }
        lon = Math.toRadians(-lon);
        lat = Math.toRadians(lat);
        double jd2 = JulianDate.getJD0UT(datePosition.getDateTime());
        double theta0 = getSiderialTimeGreenwich0UT(datePosition.getDateTime());
        double dummy = (Math.sin(h0) - (Math.sin(lat) * Math.sin(dec2))) / (Math.cos(lat) * Math.cos(dec2));
        double signumLat = Math.signum(lat);
        if (((signumLat * dec2) - PID2) + (signumLat * lat) > h0) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 2, 1);
        }
        if (((signumLat * lat) - PID2) - (signumLat * dec2) > (-h0)) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 2, 2);
        }
        double H0 = Math.acos(dummy);
        double m0 = ((ra2 + lon) - theta0) / PI2;
        if (m0 < 0.0d) {
            m0 += 1.0d;
        }
        if (m0 > 1.0d) {
            m0 -= 1.0d;
        }
        double m = m0 + (H0 / PI2);
        datePosition.setJD(jd2 + m);
        if ((((double) getTimeZoneOffset(datePosition.getDateTime())) / 24.0d) + m < 0.0d) {
            m += 1.0d;
        } else if ((((double) getTimeZoneOffset(datePosition.getDateTime())) / 24.0d) + m > 1.0d) {
            m -= 1.0d;
        }
        double theta = theta0 + (Math.toRadians(360.985647d) * m);
        if (theta > PI2) {
            theta -= PI2;
        }
        double dT = getEphemerisTime(jd2);
        double n = m + (dT / 86400.0d);
        double raI = Interpolation.interpolate(n, ra1, ra2, ra3);
        double decI = Interpolation.interpolate(n, dec1, dec2, dec3);
        double H = (theta - lon) - raI;
        if (H < -3.141592653589793d) {
            H += PI2;
        } else if (H > PI) {
            H -= PI2;
        }
        m += (getAltitude(lat, decI, H) - h0) / (((PI2 * Math.cos(decI)) * Math.cos(lat)) * Math.sin(H));
        theta = theta0 + (Math.toRadians(360.985647d) * m);
        if (theta > PI2) {
            theta -= PI2;
        }
        n = m + (dT / 86400.0d);
        raI = Interpolation.interpolate(n, ra1, ra2, ra3);
        decI = Interpolation.interpolate(n, dec1, dec2, dec3);
        if (((signumLat * decI) - PID2) + (signumLat * lat) > h0) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 2, 1);
        }
        if (((signumLat * lat) - PID2) - (signumLat * decI) > (-h0)) {
            return new RiseSetEvent(notVisible, notVisible, notVisible, 2, 2);
        }
        H = (theta - lon) - raI;
        if (H < -3.141592653589793d) {
            H += PI2;
        } else if (H > PI) {
            H -= PI2;
        }
        return new RiseSetEvent(((double) getTimeZoneOffset(datePosition.getDateTime())) + (24.0d * (m + ((getAltitude(lat, decI, H) - h0) / (((PI2 * Math.cos(decI)) * Math.cos(lat)) * Math.sin(H))))), getAzimuth(lat, decI, H), h0, 2, 0);
    }

    public static boolean isCircumpolar(GeoLocation geoLocation, Coordinates3D coord, double h0) {
        double lat = Math.toRadians((double) geoLocation.getLatitudeDeg());
        double signumLat = Math.signum(lat);
        return ((coord.getDec() * signumLat) - PID2) + (signumLat * lat) > h0;
    }

    public static boolean isAlwaysBelowHorizon(GeoLocation geoLocation, Coordinates3D coord, double h0) {
        double lat = Math.toRadians((double) geoLocation.getLatitudeDeg());
        double signumLat = Math.signum(lat);
        return ((signumLat * lat) - PID2) - (coord.getDec() * signumLat) > (-h0);
    }

    public static double getAltitude(double lat, double dec, double H) {
        return Math.asin((Math.sin(lat) * Math.sin(dec)) + ((Math.cos(lat) * Math.cos(dec)) * Math.cos(H)));
    }

    public static double getAzimuth(double lat, double dec, double ha) {
        return PI + Math.atan2(Math.sin(ha), (Math.cos(ha) * Math.sin(lat)) - (Math.tan(dec) * Math.cos(lat)));
    }

    public static double getAltitudeRadians(double lat, double dec, double H) {
        return Math.asin((Math.sin(lat) * Math.sin(dec)) + ((Math.cos(lat) * Math.cos(dec)) * Math.cos(H)));
    }

    public static double getAzimuthRadians(double lat, double dec, double ha) {
        return PI + Math.atan2(Math.sin(ha), (Math.cos(ha) * Math.sin(lat)) - (Math.tan(dec) * Math.cos(lat)));
    }

    public static double getHourAngle(DatePosition datePosition, double ra) {
        double ha = getSiderealTime(datePosition) - ra;
        if (ha > PI) {
            return ha - PI2;
        }
        if (ha < -3.141592653589793d) {
            return ha + PI2;
        }
        return ha;
    }

    public static double getHourAngleRadians(double siderialTime, double ra) {
        return (0.26179938779914946d * siderialTime) - ra;
    }

    public static void getEquatorialFromGalacticRadians(CoordinatesFloat3D eclCoord, CoordinatesFloat3D galacticCoord) {
        double lon = (double) eclCoord.getLongitude();
        double lat = (double) eclCoord.getLatitude();
        double ra = 4.9366637d + Math.atan2((Math.sin(lon) * 0.4555449068d) - (Math.tan(lat) * 0.8902128046d), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        CoordinatesFloat3D coordinatesFloat3D = galacticCoord;
        coordinatesFloat3D.setLonLatDist((float) ra, (float) Math.asin((Math.sin(lat) * 0.4555449068d) + ((Math.cos(lat) * 0.8902128046d) * Math.sin(lon))), eclCoord.getDistance());
    }

    public static void getEquatorialFromGalacticRadians(Coordinates3D eclCoord, Coordinates3D galacticCoord) {
        double lon = eclCoord.getLongitude();
        double lat = eclCoord.getLatitude();
        double ra = 4.9366637d + Math.atan2((Math.sin(lon) * 0.4555449068d) - (Math.tan(lat) * 0.8902128046d), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        galacticCoord.setLonLatDist(ra, Math.asin((Math.sin(lat) * 0.4555449068d) + ((Math.cos(lat) * 0.8902128046d) * Math.sin(lon))), eclCoord.getDistance());
    }

    public static void getEquatorialFromGalacticRadians(float lon, float lat, CoordinatesFloat3D galacticCoord) {
        lon = (float) (((double) lon) - 5.237678d);
        double sinEpsi = Math.sin(0.4398229d);
        double cosEpsi = Math.cos(0.4398229d);
        double ra = 0.24347d + Math.atan2(Math.sin((double) lon), (Math.cos((double) lon) * sinEpsi) - (Math.tan((double) lat) * cosEpsi));
        if (ra < 0.0d) {
            ra += PI2;
        }
        galacticCoord.setLonLatRad(ra, Math.asin((Math.sin((double) lat) * sinEpsi) + ((Math.cos((double) lat) * cosEpsi) * Math.cos((double) lon))), 0.0d);
    }

    public static void getEquatorialFromEcliptic(CoordinatesFloat3D eclCoord, double epsi, CoordinatesFloat3D equatorialCoord) {
        double lon = (double) eclCoord.getLongitude();
        double lat = (double) eclCoord.getLatitude();
        double ra = Math.atan2((Math.sin(lon) * Math.cos(epsi)) - (Math.tan(lat) * Math.sin(epsi)), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        CoordinatesFloat3D coordinatesFloat3D = equatorialCoord;
        coordinatesFloat3D.setRADecDistance((float) ra, (float) Math.asin((Math.sin(lat) * Math.cos(epsi)) + ((Math.cos(lat) * Math.sin(epsi)) * Math.sin(lon))), eclCoord.getDistance());
    }

    public static void getEquatorialFromEcliptic(Coordinates3D eclCoord, double epsi, Coordinates3D equatorialCoord) {
        double lon = eclCoord.getLongitude();
        double lat = eclCoord.getLatitude();
        double ra = Math.atan2((Math.sin(lon) * Math.cos(epsi)) - (Math.tan(lat) * Math.sin(epsi)), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        equatorialCoord.setRADecDistance(ra, Math.asin((Math.sin(lat) * Math.cos(epsi)) + ((Math.cos(lat) * Math.sin(epsi)) * Math.sin(lon))), eclCoord.getDistance());
    }

    public static void getEquatorialFromEclipticRadians(CoordinatesFloat3D eclCoord, double epsi, CoordinatesFloat3D euqatorialCoord) {
        double lon = (double) eclCoord.getLongitude();
        double lat = (double) eclCoord.getLatitude();
        double ra = Math.atan2((Math.sin(lon) * Math.cos(epsi)) - (Math.tan(lat) * Math.sin(epsi)), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        CoordinatesFloat3D coordinatesFloat3D = euqatorialCoord;
        coordinatesFloat3D.setLonLatDist((float) ra, (float) Math.asin((Math.sin(lat) * Math.cos(epsi)) + ((Math.cos(lat) * Math.sin(epsi)) * Math.sin(lon))), eclCoord.getDistance());
    }

    public static void getEquatorialFromEclipticRadians(Coordinates3D eclCoord, double epsi, Coordinates3D euqatorialCoord) {
        double lon = eclCoord.getLongitude();
        double lat = eclCoord.getLatitude();
        double ra = Math.atan2((Math.sin(lon) * Math.cos(epsi)) - (Math.tan(lat) * Math.sin(epsi)), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        Coordinates3D coordinates3D = euqatorialCoord;
        coordinates3D.setLonLatDist((double) ((float) ra), (double) ((float) Math.asin((Math.sin(lat) * Math.cos(epsi)) + ((Math.cos(lat) * Math.sin(epsi)) * Math.sin(lon)))), eclCoord.getDistance());
    }

    public static CoordinatesFloat3D getEquatorialFromEcliptic(Coordinates3D eclCoord, double epsi) {
        double lon = eclCoord.getLongitude();
        double lat = eclCoord.getLatitude();
        double ra = Math.atan2((Math.sin(lon) * Math.cos(epsi)) - (Math.tan(lat) * Math.sin(epsi)), Math.cos(lon));
        if (ra < 0.0d) {
            ra += PI2;
        }
        return new CoordinatesFloat3D(ra, Math.asin((Math.sin(lat) * Math.cos(epsi)) + ((Math.cos(lat) * Math.sin(epsi)) * Math.sin(lon))), eclCoord.getRadius());
    }

    public static Coordinates3D getEclipticalFromEquatorial(Coordinates3D eclCoord, double epsi) {
        return getEclipticalFromEquatorial(eclCoord.getLongitude(), eclCoord.getLatitude(), eclCoord.getRadius(), epsi);
    }

    public static Coordinates3D getEclipticalFromEquatorial(double ra, double dec, double distanceAU, double epsi) {
        double lon = Math.atan2((Math.sin(ra) * Math.cos(epsi)) + (Math.tan(dec) * Math.sin(epsi)), Math.cos(ra));
        if (lon < 0.0d) {
            lon += PI2;
        }
        return new Coordinates3D(lon, Math.asin((Math.sin(dec) * Math.cos(epsi)) - ((Math.cos(dec) * Math.sin(epsi)) * Math.sin(ra))), distanceAU);
    }

    public static void getAzAltFromRADec(DatePosition datePosition, float ra, float dec, CoordinatesFloat3D coordAzAlt) {
        getAzAltFromRADec(getSiderealTime(datePosition), datePosition, ra, dec, coordAzAlt);
    }

    public static void getAzAltFromRADec(DatePosition datePosition, double ra, double dec, Coordinates3D coordAzAlt) {
        getAzAltFromRADec(getSiderealTime(datePosition), datePosition, ra, dec, coordAzAlt);
    }

    public static void getAzAltFromRADec(DatePosition datePosition, CoordinatesFloat3D coordRADec, CoordinatesFloat3D coordAzAlt) {
        getAzAltFromRADec((double) ((float) getSiderealTime(datePosition)), datePosition, coordRADec.getRA(), coordRADec.getDec(), coordAzAlt);
    }

    public static void getAzAltFromRADec(DatePosition datePosition, Coordinates3D coordRADec, Coordinates3D coordAzAlt) {
        getAzAltFromRADec(getSiderealTime(datePosition), datePosition, coordRADec.getRA(), coordRADec.getDec(), coordAzAlt);
    }

    public static void getAzAltFromRADec(double siderialTime, DatePosition datePosition, CoordinatesFloat3D coordRADec, CoordinatesFloat3D coordAzAlt) {
        double ha = siderialTime - ((double) coordRADec.getRA());
        double cosHa = Math.cos(ha);
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        coordAzAlt.setAzAlt((float) (PI + Math.atan2(Math.sin(ha), (cosHa * sinLat) - (Math.tan((double) coordRADec.getDec()) * cosLat))), (float) Math.asin((Math.sin((double) coordRADec.getDec()) * sinLat) + ((Math.cos((double) coordRADec.getDec()) * cosLat) * cosHa)));
    }

    public static void getAzAltFromRADec(double siderialTime, DatePosition datePosition, Coordinates3D coordRADec, Coordinates3D coordAzAlt) {
        double ha = siderialTime - coordRADec.getRA();
        double cosHa = Math.cos(ha);
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        coordAzAlt.setAzAlt(PI + Math.atan2(Math.sin(ha), (cosHa * sinLat) - (Math.tan(coordRADec.getDec()) * cosLat)), Math.asin((Math.sin(coordRADec.getDec()) * sinLat) + ((Math.cos(coordRADec.getDec()) * cosLat) * cosHa)));
    }

    public static void getAzAltFromRADec(double siderialTime, DatePosition datePosition, float ra, float dec, CoordinatesFloat3D coordAzAlt) {
        double ha = siderialTime - ((double) ra);
        double cosHa = Math.cos(ha);
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        coordAzAlt.setAzAlt((float) (PI + Math.atan2(Math.sin(ha), (cosHa * sinLat) - (Math.tan((double) dec) * cosLat))), (float) Math.asin((Math.sin((double) dec) * sinLat) + ((Math.cos((double) dec) * cosLat) * cosHa)));
    }

    public static void getAzAltFromRADec(double siderialTime, DatePosition datePosition, double ra, double dec, Coordinates3D coordAzAlt) {
        double ha = siderialTime - ra;
        double cosHa = Math.cos(ha);
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        coordAzAlt.setAzAlt(PI + Math.atan2(Math.sin(ha), (cosHa * sinLat) - (Math.tan(dec) * cosLat)), Math.asin((Math.sin(dec) * sinLat) + ((Math.cos(dec) * cosLat) * cosHa)));
    }

    public static void getAzAltFromRADec(double siderialTime, DatePosition datePosition, float topoRA, float topoDec, BasisCelestialObject basisCelestialObject) {
        double ha = siderialTime - ((double) topoRA);
        double cosHa = Math.cos(ha);
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        BasisCelestialObject basisCelestialObject2 = basisCelestialObject;
        basisCelestialObject2.setAzAlt((double) ((float) (PI + Math.atan2(Math.sin(ha), (cosHa * sinLat) - (Math.tan((double) topoDec) * cosLat)))), (double) ((float) Math.asin((Math.sin((double) topoDec) * sinLat) + ((Math.cos((double) topoDec) * cosLat) * cosHa))));
    }

    public static void getAzAltFromRADec(double siderialTime, DatePosition datePosition, double topoRA, double topoDec, BasisCelestialObject basisCelestialObject) {
        double ha = siderialTime - topoRA;
        double cosHa = Math.cos(ha);
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        basisCelestialObject.setAzAlt(PI + Math.atan2(Math.sin(ha), (cosHa * sinLat) - (Math.tan(topoDec) * cosLat)), Math.asin((Math.sin(topoDec) * sinLat) + ((Math.cos(topoDec) * cosLat) * cosHa)));
    }

    public static CoordinatesFloat3D getRADecFromAzAlt(DatePosition datePosition, float siderialTime, CoordinatesFloat3D coordAzAlt) {
        double az = coordAzAlt.getAzimuth() - PI;
        double alt = coordAzAlt.getAltitude();
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        double cosAz = MathUtils.Cos(az);
        double dec = (float) Math.asin((Math.sin(alt) * sinLat) - ((Math.cos(alt) * cosLat) * (cosAz)));
        double ra = siderialTime - ((float) Math.atan2(Math.sin(az), ((cosAz) * sinLat) + (Math.tan(alt) * cosLat)));
        if (ra < 0.0f) {
            ra += PI2;
        } else if (ra > PI2) {
            ra -= PI2;
        }
        return new CoordinatesFloat3D(ra, dec);
    }

    public static Coordinates3D getRADecFromAzAlt(DatePosition datePosition, double siderialTime, Coordinates3D coordAzAlt) {
        double az = coordAzAlt.getAzimuth() - PI;
        double alt = coordAzAlt.getAltitude();
        double sinLat = datePosition.getSinLat();
        double cosLat = datePosition.getCosLat();
        double cosAz = Math.cos(az);
        double dec = Math.asin((Math.sin(alt) * sinLat) - ((Math.cos(alt) * cosLat) * cosAz));
        double ra = siderialTime - Math.atan2(Math.sin(az), (cosAz * sinLat) + (Math.tan(alt) * cosLat));
        if (ra < 0.0d) {
            ra += 6.2831854820251465d;
        } else if (ra > 6.2831854820251465d) {
            ra -= 6.2831854820251465d;
        }
        return new Coordinates3D(ra, dec);
    }

    public static String getDirectionName(double azimuth) {
        azimuth = Math.toDegrees(azimuth);
        if (azimuth >= 360.0d - 11.25d || azimuth < ((double) 1) * 11.25d) {
            return "N";
        }
        int n = -1 + 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 3) * 11.25d) {
            return "NNE";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 5) * 11.25d) {
            return "NE";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 7) * 11.25d) {
            return "ENE";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 9) * 11.25d) {
            return "E";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 11) * 11.25d) {
            return "ESE";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 13) * 11.25d) {
            return "SE";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 15) * 11.25d) {
            return "SSE";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 17) * 11.25d) {
            return "S";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 19) * 11.25d) {
            return "SSW";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 21) * 11.25d) {
            return "SW";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 23) * 11.25d) {
            return "WSW";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 25) * 11.25d) {
            return "W";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 27) * 11.25d) {
            return "WNW";
        }
        n += 2;
        if (azimuth >= ((double) n) * 11.25d && azimuth < ((double) 29) * 11.25d) {
            return "NW";
        }
        if (azimuth < ((double) (n + 2)) * 11.25d || azimuth >= ((double) 31) * 11.25d) {
            return "dir";
        }
        return "NNW";
    }

    public static void getHelioToGeocentricEclipticalCoord(Coordinates3D helEclCoord, Coordinates3D geoEclCoordSun, Coordinates3D geoEclCoord) {
        double r = helEclCoord.getRadius();
        double b = helEclCoord.getLatitude();
        double l = helEclCoord.getLongitude();
        double r0 = geoEclCoordSun.getRadius();
        double b0 = -geoEclCoordSun.getLatitude();
        double l0 = geoEclCoordSun.getLongitude() + PI;
        double x = ((Math.cos(b) * r) * Math.cos(l)) - ((Math.cos(b0) * r0) * Math.cos(l0));
        double y = ((Math.cos(b) * r) * Math.sin(l)) - ((Math.cos(b0) * r0) * Math.sin(l0));
        double z = (Math.sin(b) * r) - (Math.sin(b0) * r0);
        double delta = Math.sqrt(((x * x) + (y * y)) + (z * z));
        double lonG = Math.atan2(y, x);
        double latG = Math.atan2(z, Math.sqrt((x * x) + (y * y)));
        if (lonG < 0.0d) {
            lonG += PI2;
        }
        geoEclCoord.setLonLatDist(lonG, latG, delta);
    }

    public static void getRectangularCoordinates(Coordinates3D helEclCoord, Coordinates3D helEclRectCoord) {
        helEclRectCoord.x = (helEclCoord.r * Math.cos(helEclCoord.y)) * Math.cos(helEclCoord.x);
        helEclRectCoord.y = (helEclCoord.r * Math.cos(helEclCoord.y)) * Math.sin(helEclCoord.x);
        helEclRectCoord.r = helEclCoord.r * Math.sin(helEclCoord.y);
    }

    public static void computeTopocentricCoord(DatePosition datePosition, Coordinates3D geoEquCoord, Coordinates3D topoCentricCoord) {
        double u = Math.atan(Math.tan(Math.toRadians((double) datePosition.getLatitudeDeg())) * 0.99664719d);
        double roSinFi = 0.99664719d * Math.sin(u);
        double roCosFi = Math.cos(u);
        double ra = geoEquCoord.getRA();
        double dec = geoEquCoord.getDec();
        double sinParallax = 4.2634515103856E-5d / geoEquCoord.getRadius();
        double H = getHourAngle(datePosition, geoEquCoord.getRA());
        double dRA = Math.atan2(((-roCosFi) * sinParallax) * Math.sin(H), Math.cos(dec) - ((roCosFi * sinParallax) * Math.cos(H)));
        Coordinates3D coordinates3D = topoCentricCoord;
        coordinates3D.setRADecDistance(ra + dRA, Math.atan2((Math.sin(dec) - (roSinFi * sinParallax)) * Math.cos(dRA), Math.cos(dec) - ((roCosFi * sinParallax) * Math.cos(H))), geoEquCoord.getRadius());
    }

    private static void getAzAlt(double siderialTime, DatePosition datePosition, float limitAlt, BasisCelestialObject object) {
        object.setAzAlt(object.getRA(), object.getDec());
        computeAzAlt(object, object.getRA(), object.getDec(), siderialTime, datePosition.getSinLat(), datePosition.getCosLat(), limitAlt);
    }

    private static void computeAzAlt(BasisCelestialObject object, float ra, float dec, float siderialTime, float sinLat, float cosLat, float limitAlt) {
        float ha = siderialTime - ra;
        float alt = (float) Math.asin((double) ((MathUtils.Sin(dec) * sinLat) + ((MathUtils.Cos(dec) * cosLat) * MathUtils.Cos(ha))));
        if (alt > limitAlt) {
            object.setAzAlt((double) ((float) (PI + Math.atan2(Math.sin((double) ha), ((double) (MathUtils.Cos(ha) * sinLat)) - (Math.tan((double) dec) * ((double) cosLat))))), (double) alt);
        } else {
            object.setAltitude(RiseSetEvent.TIME_ALWAYS_ABOVE_HORIZON);
        }
    }

    private static void computeAzAlt(BasisCelestialObject object, double ra, double dec, double siderialTime, double sinLat, double cosLat, float limitAlt) {
        double ha = siderialTime - ra;
        double alt = Math.asin((Math.sin(dec) * sinLat) + ((Math.cos(dec) * cosLat) * Math.cos(ha)));
        if (alt > ((double) limitAlt)) {
            object.setAzAlt(PI + Math.atan2(Math.sin(ha), (Math.cos(ha) * sinLat) - (Math.tan(dec) * cosLat)), alt);
        } else {
            object.setAltitude(RiseSetEvent.TIME_ALWAYS_ABOVE_HORIZON);
        }
    }

    private static void computeAzAlt(CelestialObject object, float ra, float dec, float siderialTime, float sinLat, float cosLat, float limitAlt) {
        float ha = siderialTime - ra;
        float alt = (float) Math.asin( ((MathUtils.Sin(dec) * sinLat) + ((MathUtils.Cos(dec) * cosLat) * MathUtils.Cos(ha))));
        if (alt > limitAlt) {
            object.setAzAlt((double) ((float) (PI + Math.atan2( MathUtils.Sin(ha), ( (MathUtils.Cos(ha) * sinLat)) - (Math.tan((double) dec) * ((double) cosLat))))), (double) alt);
        } else {
            object.setAltitude(RiseSetEvent.TIME_ALWAYS_ABOVE_HORIZON);
        }
    }

    private static void computeAzAlt(CelestialObject object, double ra, double dec, double siderialTime, double sinLat, double cosLat, float limitAlt) {
        double ha = siderialTime - ra;
        double alt = Math.asin((Math.sin(dec) * sinLat) + ((Math.cos(dec) * cosLat) * Math.cos(ha)));
        if (alt > ((double) limitAlt)) {
            object.setAzAlt((double) ((float) (PI + Math.atan2(Math.sin(ha), (Math.cos(ha) * sinLat) - (Math.tan(dec) * cosLat)))), alt);
        } else {
            object.setAltitude(RiseSetEvent.TIME_ALWAYS_ABOVE_HORIZON);
        }
    }

    public static boolean isVisible(Coordinates3D sunAzAlt, Coordinates3D objectAzAlt, double sunMaxAltitude, double objectMinAltitude) {
        return sunAzAlt.getAltitude() < sunMaxAltitude && objectAzAlt.getAltitude() > objectMinAltitude;
    }

    public static double getLengthOfDay(DatePosition datePosition) {
        SunObject sun = new SunObject();
        Coordinates3D coord = sun.getTopocentricEquatorialCoordinates(datePosition);
        boolean circumpolar = isCircumpolar(datePosition.getGeoLocation(), coord, sun.geth0());
        if (circumpolar) {
            return 24.0d;
        }
        boolean alwaysBelowHorizon = isAlwaysBelowHorizon(datePosition.getGeoLocation(), coord, sun.geth0());
        if (alwaysBelowHorizon) {
            return 0.0d;
        }
        RiseSetEvent set = sun.getSet(datePosition);
        RiseSetEvent rise = sun.getRise(datePosition);
        double setTime = set.getTime();
        double riseTime = rise.getTime();
        return getTimeBetween0and24(setTime - riseTime);
    }

    public static double getAngularSpeed(CelestialObject celestialObject, DatePosition datePosition) {
        CelestialObject co = celestialObject.copy();
        Coordinates3D coord1 = co.getGeocentricEquatorialCoordinates(datePosition).copy();
        DatePosition datePosition2 = datePosition.copy();
        datePosition2.add(Calendar.HOUR, 1);
        return 24.0d * SphericalMath.getAngularDistanceSphereSmallAngle(coord1, co.getGeocentricEquatorialCoordinates(datePosition2).copy());
    }
}
