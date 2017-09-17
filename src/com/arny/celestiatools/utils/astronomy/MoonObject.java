package com.arny.celestiatools.utils.astronomy;


import java.util.Calendar;

public class MoonObject extends SolarSystemObject {
    public static double PHASE_EARTHSHINE_1 = 0.12d;
    public static double PHASE_EARTHSHINE_2 = 0.88d;
    public static double PHASE_FIRST_QUARTER = 0.25d;
    public static double PHASE_FULL = 0.5d;
    public static double PHASE_LAST_QUARTER = 0.75d;
    public static double PHASE_NEW = 0.0d;
    private static final double[] cos_lng = new double[]{-20905.355d, -3699.111d, -2955.968d, -569.925d, 48.888d, -3.149d, 246.158d, -152.138d, -170.733d, -204.586d, -129.62d, 108.743d, 104.755d, 10.321d, 0.0d, 79.661d, -34.782d, -23.21d, -21.636d, 24.208d, 30.824d, -8.379d, -16.675d, -12.831d, -10.445d, -11.65d, 14.403d, -7.003d, 0.0d, 10.056d, 6.322d, -9.884d, 5.751d, 0.0d, -4.95d, 4.13d, 0.0d, -3.958d, 0.0d, 3.258d, 2.616d, -1.897d, -2.117d, 2.354d, 0.0d, 0.0d, -1.423d, -1.117d, -1.571d, -1.739d, 0.0d, -4.421d, 0.0d, 0.0d, 0.0d, 0.0d, 1.165d, 0.0d, 0.0d, 8.752d};
    private static final float[] d_lat = new float[]{0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 2.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 4.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 4.0f, 4.0f, 0.0f, 4.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 4.0f, 2.0f, 2.0f, 0.0f, 2.0f, 1.0f, 1.0f, 0.0f, 2.0f, 1.0f, 2.0f, 0.0f, 4.0f, 4.0f, 1.0f, 4.0f, 1.0f, 4.0f, 2.0f};
    private static final float[] d_lng = new float[]{0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 1.0f, 0.0f, 2.0f, 0.0f, 0.0f, 4.0f, 0.0f, 4.0f, 2.0f, 2.0f, 1.0f, 1.0f, 2.0f, 2.0f, 4.0f, 2.0f, 0.0f, 2.0f, 2.0f, 1.0f, 2.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 4.0f, 0.0f, 3.0f, 2.0f, 4.0f, 0.0f, 2.0f, 2.0f, 2.0f, 4.0f, 0.0f, 4.0f, 1.0f, 2.0f, 0.0f, 1.0f, 3.0f, 4.0f, 2.0f, 0.0f, 1.0f, 2.0f, 2.0f};
    private static final float[] f_lat = new float[]{1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 3.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -3.0f, 1.0f, -3.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 3.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f};
    private static final float[] f_lng = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, 2.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, 2.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f};
    private static final float[] m_lat = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -2.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0f};
    private static final float[] m_lng = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, -2.0f, 1.0f, 2.0f, -2.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 2.0f, 2.0f, 1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 2.0f, 1.0f, 0.0f, 0.0f};
    private static final float[] mp_lat = new float[]{0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 2.0f, 1.0f, 2.0f, 0.0f, -2.0f, 1.0f, 0.0f, -1.0f, 0.0f, -1.0f, -1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 3.0f, 0.0f, -1.0f, 1.0f, -2.0f, 0.0f, 2.0f, 1.0f, -2.0f, 3.0f, 2.0f, -3.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, -2.0f, -1.0f, 1.0f, -2.0f, 2.0f, -2.0f, -1.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f};
    private static final float[] mp_lng = new float[]{1.0f, -1.0f, 0.0f, 2.0f, 0.0f, 0.0f, -2.0f, -1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 3.0f, -2.0f, -1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 2.0f, 0.0f, -3.0f, -2.0f, -1.0f, -2.0f, 1.0f, 0.0f, 2.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 2.0f, -1.0f, 1.0f, -2.0f, -1.0f, -1.0f, -2.0f, 0.0f, 1.0f, 4.0f, 0.0f, -2.0f, 0.0f, 2.0f, 1.0f, -2.0f, -3.0f, 2.0f, 1.0f, -1.0f, 3.0f, -1.0f};
    private static final double[] sin_lat = new double[]{0.0895026133439566d, 0.00489742878768112d, 0.00484665715974061d, 0.00302355603627741d, 9.67139298407617E-4d, 8.07581298190295E-4d, 5.68506097252112E-4d, 3.00161724757984E-4d, 1.61722208489794E-4d, 1.5397294661094E-4d, 1.43396251343854E-4d, 7.54680368562347E-5d, 7.33038285837618E-5d, -5.86256095744895E-5d, 4.29874594766203E-5d, 3.85892297615946E-5d, 3.60410490536829E-5d, -3.26376570122939E-5d, 3.19046187264563E-5d, -3.13112067807782E-5d, -3.05258086173808E-5d, -2.73144027937112E-5d, -2.60228591472354E-5d, -2.57436064669163E-5d, -2.460914245312E-5d, -2.34572251468038E-5d, -2.33001455141243E-5d, 1.93207948195772E-5d, 1.78198116628621E-5d, 1.45385926691128E-5d, 1.35612082879959E-5d, 1.17111592808819E-5d, 1.05941485596056E-5d, 1.04021623418862E-5d, 8.56956662729215E-6d, -7.87143492649442E-6d, 7.6619954162551E-6d, 7.36528944341606E-6d, 7.34783615089612E-6d, -6.38790506229924E-6d, -6.12610567450009E-6d, 5.77703982410122E-6d, 5.49778714378213E-6d, 5.27089434102287E-6d, -4.93928178314395E-6d, -3.99680398706701E-6d, 3.89208423194735E-6d, 3.89208423194735E-6d, -3.83972435438752E-6d, -3.83972435438752E-6d, -3.22885911618951E-6d, 3.15904594610973E-6d, -3.08923277602996E-6d, 3.07177948351002E-6d, 2.89724655831058E-6d, -2.8623399732707E-6d, 2.30383461263251E-6d, -2.07694180987325E-6d, 2.00712863979348E-6d, 1.86750229963393E-6d};
    private static final double[] sin_lng = new double[]{0.109759812213814d, 0.0222359659093058d, 0.0114897468119739d, 0.00372833744152524d, -0.00323088369812182d, -0.00199546984039015d, 0.00102613142712503d, 9.95989590943083E-4d, 9.30644463748415E-4d, 7.98627759127565E-4d, -7.14241089793639E-4d, -6.05978316292431E-4d, -5.30283386633437E-4d, 2.67506614453171E-4d, -2.18654848689849E-4d, 1.91637151868977E-4d, 1.86313897650394E-4d, 1.75126337145111E-4d, 1.49190744460475E-4d, -1.37671571397313E-4d, -1.18088977189936E-4d, -9.01113492804671E-5d, 8.70395697969571E-5d, 7.04414886104911E-5d, 6.97084503246535E-5d, 6.7387162419501E-5d, 6.39663170855921E-5d, -4.69319035861275E-5d, -4.54134671368924E-5d, 4.17133691226644E-5d, -4.09803308368268E-5d, 3.90255620745932E-5d, -3.70009801422797E-5d, -3.61108622237626E-5d, 3.57443430808438E-5d, -3.09446876378594E-5d, -2.78380015693095E-5d, 2.12057504117311E-5d, -1.9373154697137E-5d, -1.55683369277894E-5d, -1.41371669411541E-5d, 1.32470490226369E-5d, -1.24441975667196E-5d, -1.22173047639603E-5d, 1.20602251312808E-5d, 1.04021623418862E-5d, 9.58185759344886E-6d, 9.37241808320954E-6d, 9.0757121103705E-6d, -8.49975345721238E-6d, -6.96386371545737E-6d, -6.64970445009839E-6d, 6.12610567450009E-6d, -5.93411945678071E-6d, 5.75958653158128E-6d, 5.70722665402145E-6d, -5.63741348394168E-6d, 5.21853446346304E-6d, 5.13126800086332E-6d, 0.0d};
    double D;
    double E1;
    double F;
    double Ld;
    double M;
    double Md;
    private Moon moon = new Moon();
    double parallax;

    public MoonObject copy() {
        return new MoonObject(this);
    }

    protected MoonObject(MoonObject original) {
        super((SolarSystemObject) original);
    }

    public MoonObject() {
        super("ID10SolarSystem1", 0.1d, 1.0d);
    }

    public int getCalendarTimeStep() {
        return 1;
    }

    public BasisCelestialObject getBasisObject() {
        return this.moon;
    }

    public int getIconResourceIdDatePosition(DatePosition datePosition) {
        computeElements(datePosition);
        return getPhaseResource();
    }

    public int getSmallIconResourceId(DatePosition datePosition) {
        computeElements(datePosition);
        return getSmallPhaseResource();
    }

    public int getID() {
        return 1;
    }

    @Override
    public int getInformationActivity() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    public float getRadius_km() {
        return 1738.0f;
    }

    public int getOrbitResourceID() {
        return -1;
    }

    public double getMeanDiameterArcsec() {
        return 200.0d;
    }

    @Override
    public float getRadiusTopViewPixel() {
        return 0;
    }

    public double geth0() {
        return (0.7275d * this.parallax) - Math.toRadians(0.566666d);
    }

    public double getm0() {
        double phaseFactor = Math.toDegrees(getPhaseAngle()) / 100.0d;
        return ((-12.7d + (3.38d * phaseFactor)) - ((1.07d * phaseFactor) * phaseFactor)) + (((0.99d * phaseFactor) * phaseFactor) * phaseFactor);
    }

    public String getColor() {
        return "A0";
    }

    public double getSunMaxSearchAltitude() {
        return Math.toRadians(-5.0d);
    }

    public double getObjectMinSearchAltitude() {
        return Math.toRadians(2.0d);
    }

    public Coordinates3D getRotationalAxis() {
        return new Coordinates3D(4.71238898038469d, 1.1576768928478387d, 0.0d);
    }

    public double getZeroMeridian() {
        return 0.0d;
    }

    public double getCentralMeridian() {
        return 0.0d;
    }

    public float getEarthShine() {
        return (float) (0.3d * Math.exp((-getPhaseAngle()) / 10.0d));
    }

    public float getVmag() {
        return (float) getm0();
    }

    public double getDiameter1AUarcsec() {
        return 4.7924933d;
    }

    @Override
    public int getImageTopViewResourceID() {
            return 0;
    }

    public int getYearVisibilityBarDayStep() {
        return 3;
    }

    public double getGeocentricDiameterArcsec(double altitude) {
        return (1.0d + (Math.sin(altitude) * Math.sin(this.parallax))) * (7.169468E8d / (getDistanceAU() * Ephemeris.AU));
    }

    public double getPhaseAngle() {
        double psi = Math.acos(Math.cos(this.geoEclCoord.getLatitude()) * Math.cos(this.geoEclCoord.getLongitude() - this.geoEclCoordSun.getLongitude()));
        double r = this.geoEclCoordSun.getRadius();
        return Math.atan2(Math.sin(psi) * r, this.geoEclCoord.getRadius() - (Math.cos(psi) * r));
    }

    public float getPositionAngleBrightLimb(Coordinates3D coordSun, Coordinates3D coordMoon) {
        double aM = coordMoon.getRA();
        double dM = coordMoon.getDec();
        double aS = coordSun.getRA();
        double dS = coordSun.getDec();
        double angle = Math.atan2(Math.cos(dS) * Math.sin(aS - aM), (Math.cos(dM) * Math.sin(dS)) - ((Math.sin(dM) * Math.cos(dS)) * Math.cos(aS - aM)));
        if (angle < 0.0d) {
            angle += Ephemeris.PI2;
        }
        return (float) angle;
    }

    public double getPositionAngle() {
        MoonPhysicalEphemeris mpe = new MoonPhysicalEphemeris();
        mpe.setDatePosition(this.datePosition);
        return mpe.getPositionAngle(this.geoEquCoord.getRA());
    }

    public Coordinates3D getHeliocentricCoordinatesTopDownView() {
        return SphericalMath.addSpherical(new Coordinates3D(Ephemeris.PI + this.geoEclCoordSun.getLongitude(), 0.0d, this.geoEclCoordSun.getRadius()), new Coordinates3D(this.geoEclCoord.getLongitude(), this.geoEclCoord.getLatitude(), 60.0d * this.geoEclCoord.getRadius()));
    }

    public void computeElements(DatePosition datePosition) {
        super.computeElements(datePosition);
        this.geoEclCoord = computeGeocentricEclipticalCoord(this.jd);
        this.heliEclCoord = SphericalMath.addSpherical(new Coordinates3D(Ephemeris.PI + this.geoEclCoordSun.getLongitude(), this.geoEclCoordSun.getLatitude(), this.geoEclCoordSun.getRadius()), new Coordinates3D(this.geoEclCoord.getLongitude(), this.geoEclCoord.getLatitude(), this.geoEclCoord.getRadius()));
        this.heliEclCoord.correctQuadrant();
        Ephemeris.getEquatorialFromEcliptic(this.geoEclCoord, Ecliptic.getObliquity(this.jd), this.geoEquCoord);
        Ephemeris.computeTopocentricCoord(datePosition, this.geoEquCoord, this.topoEquCoord);
    }

    protected Coordinates3D computeGeocentricEclipticalCoord(double jd) {
        int i;
        this.T = (jd - 2451545.0d) / 36525.0d;
        this.T2 = this.T * this.T;
        this.T3 = this.T2 * this.T;
        this.T4 = this.T3 * this.T;
        this.T5 = this.T4 * this.T;
        double L = 0.0d;
        double B = 0.0d;
        double R = 0.0d;
        this.Ld = Math.toRadians((((218.3164477d + (481267.88123421d * this.T)) - (0.0015786d * this.T2)) + (this.T3 / 538841.0d)) - (this.T4 / 6.5194E7d));
        this.D = Math.toRadians((((297.8501921d + (445267.1114034d * this.T)) - (0.0018819d * this.T2)) + (this.T3 / 545868.0d)) - (this.T4 / 1.13065E8d));
        this.M = Math.toRadians(((357.5291092d + (35999.0502909d * this.T)) - (1.536E-4d * this.T2)) + (this.T3 / 2.449E7d));
        this.Md = Math.toRadians((((134.9633964d + (477198.8675055d * this.T)) + (0.0087414d * this.T2)) + (this.T3 / 69699.0d)) - (this.T4 / 1.4712E7d));
        this.F = Math.toRadians((((93.272095d + (483202.0175233d * this.T)) - (0.0036539d * this.T2)) - (this.T3 / 3526000.0d)) + (this.T4 / 8.6331E8d));
        double A1 = Math.toRadians(119.75d + (131.849d * this.T));
        double A2 = Math.toRadians(53.09d + (479264.29d * this.T));
        double A3 = Math.toRadians(313.45d + (481266.484d * this.T));
        this.E1 = (1.0d - (0.002516d * this.T)) - (7.4E-6d * this.T2);
        double[] E = new double[]{1.0d, this.E1, this.E1 * this.E1};
        int numTerms1 = d_lng.length;
        int numTerms2 = d_lat.length;
        if (this.isLowPrecision) {
            numTerms1 = 15;
            numTerms2 = 15;
        }
        for (i = 0; i < numTerms1; i++) {
            double arg = (((((double) d_lng[i]) * this.D) + (((double) m_lng[i]) * this.M)) + (((double) mp_lng[i]) * this.Md)) + (((double) f_lng[i]) * this.F);
            L += (sin_lng[i] * E[(int) Math.abs(m_lng[i])]) * Math.sin(arg);
            R += (cos_lng[i] * E[(int) Math.abs(m_lng[i])]) * Math.cos(arg);
        }
        for (i = 0; i < numTerms2; i++) {
            B += (sin_lat[i] * E[(int) Math.abs(m_lat[i])]) * Math.sin((((((double) d_lat[i]) * this.D) + (((double) m_lat[i]) * this.M)) + (((double) mp_lat[i]) * this.Md)) + (((double) f_lat[i]) * this.F));
        }
        L += this.Ld + (1.0E-6d * (((69.0801317939355d * Math.sin(A1)) + (34.2433599241287d * Math.sin(this.Ld - this.F))) + (5.55014702134196d * Math.sin(A2))));
        B += 1.0E-6d * ((((((-39.0081087820732d * Math.sin(this.Ld)) + (6.66715774261833d * Math.sin(A3))) + (3.05432619099007d * Math.sin(A1 - this.F))) + (3.05432619099007d * Math.sin(this.F + A1))) + (2.2165681500328d * Math.sin(this.Ld - this.Md))) - (2.00712863979348d * Math.sin(this.Ld + this.Md)));
        R += 385000.56d;
        this.parallax = Math.asin(6378.14d / R);
        R /= Ephemeris.AU;
        L %= Ephemeris.PI2;
        if (L < 0.0d) {
            L += Ephemeris.PI2;
        } else if (L >= Ephemeris.PI2) {
            L -= Ephemeris.PI2;
        }
        B %= Ephemeris.PI2;
        if (B < -3.141592653589793d) {
            B += Ephemeris.PI2;
        } else if (B > Ephemeris.PI) {
            B -= Ephemeris.PI2;
        }
        return new Coordinates3D(L, B, R);
    }

    public float getMoonAge(DatePosition mycalendar) {
        long dateNextNewMillis;
        MoonPhase moonPhase = new MoonPhase();
        long dateNowMillis = mycalendar.getTimeInMillis();
        DatePosition dp = mycalendar.copy();
        long dateNextNewMillisNow = moonPhase.getDateOfNextPhase(dp, 0.0d, false).getTimeInMillis();
        dp.add(Calendar.DAY_OF_MONTH, -29);
        long dateNextNewMillis0 = moonPhase.getDateOfNextPhase(dp, 0.0d, false).getTimeInMillis();
        dp.add(Calendar.DAY_OF_MONTH, -1);
        long dateNextNewMillis1 = moonPhase.getDateOfNextPhase(dp, 0.0d, false).getTimeInMillis();
        if (dateNextNewMillis0 == dateNextNewMillis1) {
            dateNextNewMillis = dateNextNewMillis0;
        } else if (((float) (dateNextNewMillisNow - dateNowMillis)) / 8.64E7f < 1.0f) {
            dateNextNewMillis = dateNextNewMillis1;
        } else {
            dateNextNewMillis = dateNextNewMillis0;
        }
        return ((float) (dateNowMillis - dateNextNewMillis)) / 8.64E7f;
    }

    public double getLongitudeAscendingNode() {
        return Math.toRadians((((125.0445479d - (1934.1362891d * this.T)) + (0.0020754d * this.T2)) + (this.T3 / 467441.0d)) - (this.T4 / 6.0616E7d));
    }

    public CoordinatesFloat3D getOpticalLibration() {
        double I = Math.toRadians(1.54242d);
        double lambda = this.geoEclCoord.getLongitude();
        double beta = this.geoEclCoord.getLatitude();
        double W = lambda - Math.toRadians((((126.0445479d - (1934.1362891d * this.T)) + (0.0020754d * this.T2)) + (this.T3 / 467441.0d)) - (this.T4 / 6.0616E7d));
        return new CoordinatesFloat3D(Math.atan2(((Math.sin(W) * Math.cos(beta)) * Math.cos(I)) - (Math.sin(beta) * Math.sin(I)), Math.cos(W) * Math.cos(beta)) - this.F, Math.asin((((-Math.sin(W)) * Math.cos(beta)) * Math.sin(I)) - (Math.sin(beta) * Math.cos(I))));
    }

    public double getMeanElongation() {
        return this.D;
    }

    public double getSunMeanAnomaly() {
        return this.M;
    }

    public double getMoonMeanAnomaly() {
        return this.Md;
    }

    public double getArgumentLatitude() {
        return this.F;
    }

    public double getE() {
        return this.E1;
    }
}
