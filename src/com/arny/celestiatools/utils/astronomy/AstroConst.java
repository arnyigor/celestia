package com.arny.celestiatools.utils.astronomy;

/**
 * Created by i.sedoy on 22.03.17.
 */
public class AstroConst {
    public static final double AU = 149597870691.0;// [m]
    public static final double PC = 3.08567758128E16;// [m]
    public static final double LY = 9.4607304725808E15;// [m]
    // Ecliptic
    public static final double EARTH_SMA = 1.0000002;
    public static final double EARTH_T = 1.00004;
    public static final double EARTH_DOLG_1980 = 98.833540;
    public static final double EARTH_PERIC = 102.596403;
    // Radius of Earth, Sun and Moon
    public static final double R_Earth = 6378136.6;     // [m]
    public static final double R_Sun = 696000.0;       // [km]
    public static final double R_Moon = 1738.0;       // [km]
    public static final double GAUSS  = 0.01720209895;
    public static final double JD2000 = 2451545.0;	// 2000.1.1 12h ET
    public static final double JD1900 = 2415021.0;	// 1900.1.1 12h ET
    public static final double TWILIGHT = 90.8333333333333;   // 90°50' (Сумерки)
    public static final double CIVIL_TWILIGHT = 96.0;         // 96°    (Гражданские сумерки)
    public static final double NAUTICAL_TWILIGHT = 102.0;     // 102°   (Навигационные сумерки)
    public static final double ASTRONOMICAL_TWILIGHT = 108.0; // 108°   (Астрономические сумерки)
}
