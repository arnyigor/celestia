package com.arny.celestiatools.utils.astronomy;


public class SunObject extends SolarSystemObject {
    double h0 = Math.toRadians(-0.8333d);
    private Sun sun = new Sun();

    public SunObject copy() {
        return new SunObject(this);
    }

    protected SunObject(SunObject original) {
        super((SolarSystemObject) original);
    }

    public SunObject() {
        super("ID10SolarSystem0");
    }

    public BasisCelestialObject getBasisObject() {
        return this.sun;
    }

    public String getName() {
        return "Sun";
    }
    public int getID() {
        return 0;
    }

    @Override
    public int getInformationActivity() {
        return 0;
    }

    public float getRadius_km() {
        return 696342.0f;
    }

    public int getOrbitResourceID() {
        return -1;
    }

    public double getMeanDiameterArcsec() {
        return 2000.0d;
    }

    @Override
    public float getRadiusTopViewPixel() {
        return 0;
    }

    public double geth0() {
        return this.h0;
    }

    public double getm0() {
        return 0.0d;
    }

    public double getSunMaxSearchAltitude() {
        return Math.toRadians(100.0d);
    }

    public double getObjectMinSearchAltitude() {
        return 0.0d;
    }

    public double getIlluminatedFraction() {
        return 1.0d;
    }

    public String getColor() {
        return "G5";
    }

    public Coordinates3D getRotationalAxis() {
        double T = (this.jd - 2451545.0d) / 36525.0d;
        return new Coordinates3D((40.66d - (0.036d * T)) + (4.731d * T), (83.52d - (0.004d * T)) + (0.407d * T), 0.0d).toRadians();
    }

    public double getZeroMeridian() {
        return 0.0d;
    }

    public double getCentralMeridian() {
        return 0.0d;
    }

    public float getVmag() {
        return -26.78f;
    }

    public double getDiameter1AUarcsec() {
        return 1919.26d;
    }

    @Override
    public int getImageTopViewResourceID() {
        return 0;
    }

    public static double getMeanLongitude(double jd) {
        double T = Ephemeris.getCenturiesSince1900(jd);
        return ((((((279.69668d + (36000.76892d * T)) + ((3.025E-4d * T) * T)) + (0.00134d * Math.cos(Math.toRadians(153.23d + (22518.7541d * T))))) + (0.00154d * Math.cos(Math.toRadians(216.57d + (45037.5082d * T))))) + (0.002d * Math.cos(Math.toRadians(312.69d + (32964.3577d * T))))) + (0.00179d * Math.sin(Math.toRadians((350.74d + (445267.1142d * T)) - ((0.00144d * T) * T))))) + (0.00178d * Math.sin(Math.toRadians(231.19d + (20.2d * T))));
    }

    public static double getMeanAnomaly(double jd) {
        double T = Ephemeris.getCenturiesSince1900(jd);
        return ((358.47583d + (35999.04975d * T)) - ((1.5E-4d * T) * T)) - (((3.3E-6d * T) * T) * T);
    }

    public void computeElements(DatePosition datePosition) {
        this.datePosition = datePosition;
        this.jd = datePosition.getJD();
        if (this.jd != this.lastJD) {
            this.T = Ephemeris.getCenturiesSince2000(this.jd);
            this.T2 = this.T * this.T;
            this.T3 = this.T2 * this.T;
            this.T4 = this.T3 * this.T;
            this.T5 = this.T4 * this.T;
            this.lastJD = this.jd;
        }
        if (datePosition.getGeoLocation() != null) {
            computeHeliocentricEclipticalCoord(this.jd, this.geoEclCoord);
            this.geoEclCoordSun = this.geoEclCoord;
            this.heliEclCoord = new Coordinates3D(0.0d, 0.0d, 0.0d);
            Ephemeris.getEquatorialFromEcliptic(this.geoEclCoord, Ecliptic.getObliquity(this.jd), this.geoEquCoord);
            Ephemeris.computeTopocentricCoord(datePosition, this.geoEquCoord, this.topoEquCoord);
        }
    }

    public Coordinates3D getGeocentricRectangularEquatorialCoordinates(double jd) {
        double T = (jd - 2451545.0d) / 365250.0d;
        double T2 = T * T;
        double T3 = T2 * T;
        double T4 = T3 * T;
        double T5 = T4 * T;
        this.L5 = 0.0d;
        this.L4 = 0.0d;
        this.L3 = 0.0d;
        this.L2 = 0.0d;
        this.L1 = 0.0d;
        this.L0 = 0.0d;
        this.B5 = 0.0d;
        this.B4 = 0.0d;
        this.B3 = 0.0d;
        this.B2 = 0.0d;
        this.B1 = 0.0d;
        this.B0 = 0.0d;
        this.R5 = 0.0d;
        this.R4 = 0.0d;
        this.R3 = 0.0d;
        this.R2 = 0.0d;
        this.R1 = 0.0d;
        this.R0 = 0.0d;
        computePeriodicTermsJ2000(T);
        double B = -((((this.B0 + (this.B1 * T)) + (this.B2 * T2)) + (this.B3 * T3)) + (this.B4 * T4));
        double R = (((this.R0 + (this.R1 * T)) + (this.R2 * T2)) + (this.R3 * T3)) + (this.R4 * T4);
        double L = (Ephemeris.PI + (((((this.L0 + (this.L1 * T)) + (this.L2 * T2)) + (this.L3 * T3)) + (this.L4 * T4)) + (this.L5 * T5))) % Ephemeris.PI2;
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
        double cosb = Math.cos(B);
        double x = (R * cosb) * Math.cos(L);
        double y = (R * cosb) * Math.sin(L);
        double z = R * Math.sin(B);
        return new Coordinates3D(((4.4036E-7d * z) + x) - (1.90919E-7d * z), ((-4.79966E-7d * x) + (0.917482137087d * y)) - (0.397776982902d * z), (0.397776982902d * y) + (0.917482137087d * z));
    }

    public static double getEquationOfTime(double jd) {
        double eps = Ecliptic.getObliquity(jd);
        double l = getMeanLongitude(jd);
        double e = new EarthObject().getEccentricity(jd);
        double m = getMeanAnomaly(jd);
        double y = Math.pow(Math.tan(eps / 2.0d), 2.0d);
        return Math.toDegrees(((((Math.sin(Math.toRadians(2.0d * l)) * y) - ((2.0d * e) * Math.sin(Math.toRadians(m)))) + ((((4.0d * e) * y) * Math.sin(Math.toRadians(m))) * Math.cos(Math.toRadians(2.0d * l)))) - (((0.5d * y) * y) * Math.sin(Math.toRadians(4.0d * l)))) - (((1.25d * e) * e) * Math.sin(Math.toRadians(2.0d * m)))) / 15.0d;
    }

    public static double getEquationOfTime(DatePosition datePosition) {
        return getEquationOfTime(JulianDate.getJD(datePosition));
    }

    private static boolean isNorthernHemisphere(GeoLocation geoLocation) {
        return geoLocation.getLatitudeDeg() >= 0.0f;
    }

    public static double getSpring(int year) {
        double y = ((double) (year - 2000)) / 1000.0d;
        return computeSeasonCorrection((((2451623.80984d + (365242.37404d * y)) + ((0.05169d * y) * y)) - (((0.00411d * y) * y) * y)) - ((((5.7E-4d * y) * y) * y) * y));
    }

    public static double getSummer(int year) {
        double y = ((double) (year - 2000)) / 1000.0d;
        return computeSeasonCorrection((((2451716.56767d + (365241.62603d * y)) + ((0.00325d * y) * y)) + (((0.00888d * y) * y) * y)) - ((((3.0E-4d * y) * y) * y) * y));
    }

    public static double getAutumn(int year) {
        double y = ((double) (year - 2000)) / 1000.0d;
        return computeSeasonCorrection((((2451810.21715d + (365242.01767d * y)) - ((0.11575d * y) * y)) - (((0.00337d * y) * y) * y)) + ((((7.8E-4d * y) * y) * y) * y));
    }

    public static double getWinter(int year) {
        double y = ((double) (year - 2000)) / 1000.0d;
        return computeSeasonCorrection((((2451900.05952d + (365242.74049d * y)) - ((0.06223d * y) * y)) - (((0.00823d * y) * y) * y)) + ((((3.2E-4d * y) * y) * y) * y));
    }

    private static double computeSeasonCorrection(double jde0) {
        double[] A = new double[]{485.0d, 203.0d, 199.0d, 182.0d, 156.0d, 136.0d, 77.0d, 74.0d, 70.0d, 58.0d, 52.0d, 50.0d, 45.0d, 44.0d, 29.0d, 18.0d, 17.0d, 16.0d, 14.0d, 12.0d, 12.0d, 12.0d, 9.0d, 8.0d};
        double[] B = new double[]{324.96d, 337.23d, 342.08d, 27.85d, 73.14d, 171.52d, 222.54d, 296.72d, 243.58d, 119.81d, 297.17d, 21.02d, 247.54d, 325.15d, 60.93d, 155.12d, 288.79d, 198.04d, 199.76d, 95.39d, 287.11d, 320.81d, 227.73d, 15.45d};
        double[] C = new double[]{1934.136d, 32964.467d, 20.186d, 445267.112d, 45036.886d, 22518.443d, 65928.934d, 3034.906d, 9037.513d, 33718.147d, 150.678d, 2281.226d, 29929.562d, 31555.956d, 4443.417d, 67555.328d, 4562.452d, 62894.029d, 31436.921d, 14577.848d, 31931.756d, 34777.259d, 1222.114d, 16859.074d};
        double T = (jde0 - 2451545.0d) / 36525.0d;
        double W = Ephemeris.TORAD * ((35999.373d * T) - 2.47d);
        double deltaLambda = (1.0d + (0.0334d * Math.cos(W))) + (7.0E-4d * Math.cos(2.0d * W));
        double S = 0.0d;
        for (int i = 0; i < A.length; i++) {
            S += A[i] * Math.cos(Ephemeris.TORAD * (B[i] + (C[i] * T)));
        }
//        return ((PlanetOpenGLView.DISTANCE_SCALE * S) / deltaLambda) + jde0;
        return 0.0;
    }

    public RiseSetEvent getMorningTwilight(DatePosition datePosition, double h0) {
        this.h0 = h0;
        RiseSetEvent jd = getRise(datePosition);
        this.h0 = -0.014543828656868749d;
        return jd;
    }

    public RiseSetEvent getEveningTwilight(DatePosition datePosition, double h0) {
        this.h0 = h0;
        RiseSetEvent jd = getSet(datePosition);
        this.h0 = -0.014543828656868749d;
        return jd;
    }

    public void computeHeliocentricEclipticalCoord(double jd, Coordinates3D helioEclCoord) {
        double T = (jd - 2451545.0d) / 365250.0d;
        double T2 = T * T;
        double T3 = T2 * T;
        double T4 = T3 * T;
        double T5 = T4 * T;
        this.L5 = 0.0d;
        this.L4 = 0.0d;
        this.L3 = 0.0d;
        this.L2 = 0.0d;
        this.L1 = 0.0d;
        this.L0 = 0.0d;
        this.B5 = 0.0d;
        this.B4 = 0.0d;
        this.B3 = 0.0d;
        this.B2 = 0.0d;
        this.B1 = 0.0d;
        this.B0 = 0.0d;
        this.R5 = 0.0d;
        this.R4 = 0.0d;
        this.R3 = 0.0d;
        this.R2 = 0.0d;
        this.R1 = 0.0d;
        this.R0 = 0.0d;
        if (this.isLowPrecision) {
            computePeriodicTermsLowPrecision(T);
        } else {
            computePeriodicTermsHighPrecision(T);
        }
        double B = -((((this.B0 + (this.B1 * T)) + (this.B2 * T2)) + (this.B3 * T3)) + (this.B4 * T4));
        double R = (((this.R0 + (this.R1 * T)) + (this.R2 * T2)) + (this.R3 * T3)) + (this.R4 * T4);
        double L = (Ephemeris.PI + (((((this.L0 + (this.L1 * T)) + (this.L2 * T2)) + (this.L3 * T3)) + (this.L4 * T4)) + (this.L5 * T5))) % Ephemeris.PI2;
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
        helioEclCoord.setLonLatDist((double) ((float) L), (double) ((float) B), (double) ((float) R));
    }

    protected void computePeriodicTermsJ2000(double T) {
        this.L0 += 1.75347045673d * Math.cos(0.0d + (0.0d * T));
        this.L0 += 0.03341656456d * Math.cos(4.66925680417d + (6283.0758499914d * T));
        this.L0 += 3.4894275E-4d * Math.cos(4.62610241759d + (12566.1516999828d * T));
        this.L0 += 3.417571E-5d * Math.cos(2.82886579606d + (3.523118349d * T));
        this.L0 += 3.497056E-5d * Math.cos(2.74411800971d + (5753.3848848968d * T));
        this.L0 += 3.135896E-5d * Math.cos(3.62767041758d + (77713.7714681205d * T));
        this.L0 += 2.676218E-5d * Math.cos(4.41808351397d + (7860.4193924392d * T));
        this.L0 += 2.342687E-5d * Math.cos(6.13516237631d + (3930.2096962196d * T));
        this.L0 += 1.273166E-5d * Math.cos(2.03709655772d + (529.6909650946d * T));
        this.L0 += 1.324292E-5d * Math.cos(0.74246356352d + (11506.7697697936d * T));
        this.L0 += 9.01855E-6d * Math.cos(2.04505443513d + (26.2983197998d * T));
        this.L0 += 1.199167E-5d * Math.cos(1.10962944315d + (1577.3435424478d * T));
        this.L0 += 8.57223E-6d * Math.cos(3.50849156957d + (398.1490034082d * T));
        this.L0 += 7.79786E-6d * Math.cos(1.17882652114d + (5223.6939198022d * T));
        this.L0 += 9.9025E-6d * Math.cos(5.23268129594d + (5884.9268465832d * T));
        this.L0 += 7.53141E-6d * Math.cos(2.53339053818d + (5507.5532386674d * T));
        this.L0 += 5.05264E-6d * Math.cos(4.58292563052d + (18849.2275499742d * T));
        this.L0 += 4.92379E-6d * Math.cos(4.20506639861d + (775.522611324d * T));
        this.L0 += 3.56655E-6d * Math.cos(2.91954116867d + (0.0673103028d * T));
        this.L0 += 2.84125E-6d * Math.cos(1.89869034186d + (796.2980068164d * T));
        this.L0 += 2.4281E-6d * Math.cos(0.34481140906d + (5486.777843175d * T));
        this.L0 += 3.17087E-6d * Math.cos(5.84901952218d + (11790.6290886588d * T));
        this.L0 += 2.71039E-6d * Math.cos(0.31488607649d + (10977.078804699d * T));
        this.L0 += 2.0616E-6d * Math.cos(4.80646606059d + (2544.3144198834d * T));
        this.L0 += 2.05385E-6d * Math.cos(1.86947813692d + (5573.1428014331d * T));
        this.L0 += 2.02261E-6d * Math.cos(2.45767795458d + (6069.7767545534d * T));
        this.L0 += 1.26184E-6d * Math.cos(1.0830263021d + (20.7753954924d * T));
        this.L0 += 1.55516E-6d * Math.cos(0.83306073807d + (213.299095438d * T));
        this.L0 += 1.15132E-6d * Math.cos(0.64544911683d + (0.9803210682d * T));
        this.L0 += 1.02851E-6d * Math.cos(0.63599846727d + (4694.0029547076d * T));
        this.L0 += 1.01724E-6d * Math.cos(4.26679821365d + (7.1135470008d * T));
        this.L0 += 9.9206E-7d * Math.cos(6.20992940258d + (2146.1654164752d * T));
        this.L0 += 1.32212E-6d * Math.cos(3.41118275555d + (2942.4634232916d * T));
        this.L0 += 9.7607E-7d * Math.cos(0.6810127227d + (155.4203994342d * T));
        this.L0 += 8.5128E-7d * Math.cos(1.29870743025d + (6275.9623029906d * T));
        this.L0 += 7.4651E-7d * Math.cos(1.75508916159d + (5088.6288397668d * T));
        this.L0 += 1.01895E-6d * Math.cos(0.97569221824d + (15720.8387848784d * T));
        this.L0 += 8.4711E-7d * Math.cos(3.67080093025d + (71430.6956181291d * T));
        this.L0 += 7.3547E-7d * Math.cos(4.67926565481d + (801.8209311238d * T));
        this.L0 += 7.3874E-7d * Math.cos(3.50319443167d + (3154.6870848956d * T));
        this.L0 += 7.8756E-7d * Math.cos(3.03698313141d + (12036.4607348882d * T));
        this.L0 += 7.9637E-7d * Math.cos(1.807913307d + (17260.1546546904d * T));
        this.L0 += 8.5803E-7d * Math.cos(5.98322631256d + (161000.685737674d * T));
        this.L0 += 5.6963E-7d * Math.cos(2.78430398043d + (6286.5989683404d * T));
        this.L0 += 6.1148E-7d * Math.cos(1.81839811024d + (7084.8967811152d * T));
        this.L0 += 6.9627E-7d * Math.cos(0.83297596966d + (9437.762934887d * T));
        this.L0 += 5.6116E-7d * Math.cos(4.38694880779d + (14143.4952424306d * T));
        this.L0 += 6.2449E-7d * Math.cos(3.97763880587d + (8827.3902698748d * T));
        this.L0 += 5.1145E-7d * Math.cos(0.28306864501d + (5856.4776591154d * T));
        this.L0 += 5.5577E-7d * Math.cos(3.47006009062d + (6279.5527316424d * T));
        this.L0 += 4.1036E-7d * Math.cos(5.36817351402d + (8429.2412664666d * T));
        this.L0 += 5.1605E-7d * Math.cos(1.33282746983d + (1748.016413067d * T));
        this.L0 += 5.1992E-7d * Math.cos(0.18914945834d + (12139.5535091068d * T));
        this.L0 += 4.9E-7d * Math.cos(0.48735065033d + (1194.4470102246d * T));
        this.L0 += 3.92E-7d * Math.cos(6.16832995016d + (10447.3878396044d * T));
        this.L0 += 3.5566E-7d * Math.cos(1.77597314691d + (6812.766815086d * T));
        this.L0 += 3.677E-7d * Math.cos(6.04133859347d + (10213.285546211d * T));
        this.L0 += 3.6596E-7d * Math.cos(2.56955238628d + (1059.3819301892d * T));
        this.L0 += 3.3291E-7d * Math.cos(0.59309499459d + (17789.845619785d * T));
        this.L0 += 3.5954E-7d * Math.cos(1.70876111898d + (2352.8661537718d * T));
        this.L0 += 4.0938E-7d * Math.cos(2.39850881707d + (19651.048481098d * T));
        this.L0 += 3.0047E-7d * Math.cos(2.73975123935d + (1349.8674096588d * T));
        this.L0 += 3.0412E-7d * Math.cos(0.44294464135d + (83996.8473181119d * T));
        this.L0 += 2.3663E-7d * Math.cos(0.48473567763d + (8031.0922630584d * T));
        this.L0 += 2.3574E-7d * Math.cos(2.06527720049d + (3340.6124266998d * T));
        this.L0 += 2.1089E-7d * Math.cos(4.14825464101d + (951.7184062506d * T));
        this.L0 += 2.4738E-7d * Math.cos(0.21484762138d + (3.5904286518d * T));
        this.L0 += 2.5352E-7d * Math.cos(3.16470953405d + (4690.4798363586d * T));
        this.L0 += 2.282E-7d * Math.cos(5.22197888032d + (4705.7323075436d * T));
        this.L0 += 2.1419E-7d * Math.cos(1.42563735525d + (16730.4636895958d * T));
        this.L0 += 2.1891E-7d * Math.cos(5.55594302562d + (553.5694028424d * T));
        this.L0 += 1.7481E-7d * Math.cos(4.56052900359d + (135.0650800354d * T));
        this.L0 += 1.9925E-7d * Math.cos(5.22208471269d + (12168.0026965746d * T));
        this.L0 += 1.986E-7d * Math.cos(5.77470167653d + (6309.3741697912d * T));
        this.L0 += 2.03E-7d * Math.cos(0.37133792946d + (283.8593188652d * T));
        this.L0 += 1.4421E-7d * Math.cos(4.19315332546d + (242.728603974d * T));
        this.L0 += 1.6225E-7d * Math.cos(5.98837722564d + (11769.8536931664d * T));
        this.L0 += 1.5077E-7d * Math.cos(4.19567181073d + (6256.7775301916d * T));
        this.L0 += 1.9124E-7d * Math.cos(3.82219996949d + (23581.2581773176d * T));
        this.L0 += 1.8888E-7d * Math.cos(5.38626880969d + (149854.400134808d * T));
        this.L0 += 1.4346E-7d * Math.cos(3.72355084422d + (38.0276726358d * T));
        this.L0 += 1.7898E-7d * Math.cos(2.21490735647d + (13367.9726311066d * T));
        this.L1 += 6283.07584999d * Math.cos(0.0d + (0.0d * T));
        this.L1 += 0.00206058863d * Math.cos(2.67823455584d + (6283.0758499914d * T));
        this.L1 += 4.30343E-5d * Math.cos(2.63512650414d + (12566.1516999828d * T));
        this.L1 += 4.25264E-6d * Math.cos(1.59046980729d + (3.523118349d * T));
        this.L1 += 1.08977E-6d * Math.cos(2.96618001993d + (1577.3435424478d * T));
        this.L1 += 9.3478E-7d * Math.cos(2.59212835365d + (18849.2275499742d * T));
        this.L1 += 1.19261E-6d * Math.cos(5.79557487799d + (26.2983197998d * T));
        this.L1 += 7.2122E-7d * Math.cos(1.13846158196d + (529.6909650946d * T));
        this.L1 += 6.7768E-7d * Math.cos(1.87472304791d + (398.1490034082d * T));
        this.L1 += 6.7327E-7d * Math.cos(4.40918235168d + (5507.5532386674d * T));
        this.L1 += 5.9027E-7d * Math.cos(2.8879703846d + (5223.6939198022d * T));
        this.L1 += 5.5976E-7d * Math.cos(2.17471680261d + (155.4203994342d * T));
        this.L1 += 4.5407E-7d * Math.cos(0.39803079805d + (796.2980068164d * T));
        this.L1 += 3.6369E-7d * Math.cos(0.46624739835d + (775.522611324d * T));
        this.L1 += 2.8958E-7d * Math.cos(2.64707383882d + (7.1135470008d * T));
        this.L1 += 1.9097E-7d * Math.cos(1.84628332577d + (5486.777843175d * T));
        this.L1 += 2.0844E-7d * Math.cos(5.34138275149d + (0.9803210682d * T));
        this.L1 += 1.8508E-7d * Math.cos(4.96855124577d + (213.299095438d * T));
        this.L1 += 1.6233E-7d * Math.cos(0.03216483047d + (2544.3144198834d * T));
        this.L1 += 1.7293E-7d * Math.cos(2.99116864949d + (6275.9623029906d * T));
        this.L1 += 1.5832E-7d * Math.cos(1.43049285325d + (2146.1654164752d * T));
        this.L1 += 1.4615E-7d * Math.cos(1.20532366323d + (10977.078804699d * T));
        this.L1 += 1.1877E-7d * Math.cos(3.25804815607d + (5088.6288397668d * T));
        this.L1 += 1.1514E-7d * Math.cos(2.07502418155d + (4694.0029547076d * T));
        this.L1 += 9.721E-8d * Math.cos(4.23925472239d + (1349.8674096588d * T));
        this.L1 += 9.969E-8d * Math.cos(1.30262991097d + (6286.5989683404d * T));
        this.L1 += 9.452E-8d * Math.cos(2.69957062864d + (242.728603974d * T));
        this.L1 += 1.2461E-7d * Math.cos(2.83432285512d + (1748.016413067d * T));
        this.L2 += 8.722E-5d * Math.cos(1.0725d + (6283.075d * T));
        this.L2 += 9.91E-6d * Math.cos(3.1416d);
        this.L2 += 2.95E-6d * Math.cos(0.437d + (12566.152d * T));
        this.L2 += 2.7E-7d * Math.cos(0.05d + (3.52d * T));
        this.L2 += 1.6E-7d * Math.cos(5.19d + (26.3d * T));
        this.L2 += 1.6E-7d * Math.cos(3.69d + (155.42d * T));
        this.L2 += 9.0E-8d * Math.cos(0.3d + (18849.23d * T));
        this.L2 += 9.0E-8d * Math.cos(2.06d + (77713.77d * T));
        this.L2 += 7.0E-8d * Math.cos(0.83d + (775.52d * T));
        this.L2 += 5.0E-8d * Math.cos(4.66d + (1577.34d * T));
        this.L2 += 4.0E-8d * Math.cos(1.03d + (7.11d * T));
        this.L2 += 4.0E-8d * Math.cos(3.44d + (5573.14d * T));
        this.L2 += 3.0E-8d * Math.cos(5.14d + (796.3d * T));
        this.L2 += 3.0E-8d * Math.cos(6.05d + (5507.55d * T));
        this.L2 += 3.0E-8d * Math.cos(1.19d + (242.73d * T));
        this.L2 += 3.0E-8d * Math.cos(6.12d + (529.69d * T));
        this.L2 += 3.0E-8d * Math.cos(0.3d + (398.15d * T));
        this.L2 += 3.0E-8d * Math.cos(2.28d + (553.57d * T));
        this.L2 += 2.0E-8d * Math.cos(4.38d + (5223.69d * T));
        this.L2 += 2.0E-8d * Math.cos(3.75d + (0.98d * T));
        this.L3 += 2.89226E-6d * Math.cos(5.84384198723d + (6283.0758499914d * T));
        this.L3 += 2.1E-7d * Math.cos(6.05d + (12566.15d * T));
        this.L3 += 3.0E-8d * Math.cos(5.19577265202d + (155.4203994342d * T));
        this.L3 += 3.0E-8d * Math.cos(3.14d + (0.0d * T));
        this.L3 += 1.0E-8d * Math.cos(4.72200252235d + (3.523118349d * T));
        this.L3 += 1.0E-8d * Math.cos(5.96925937141d + (242.728603974d * T));
        this.L3 += 1.0E-8d * Math.cos(5.54d + (18849.2275499742d * T));
        this.L4 += 7.717E-8d * Math.cos(4.13446589358d + (6283.0758499914d * T));
        this.L4 += 1.0E-8d * Math.cos(3.28d + (12566.1516999828d * T));
        this.B0 += 2.7962E-6d * Math.cos(3.19870156017d + (84334.6615813083d * T));
        this.B0 += 1.01643E-6d * Math.cos(5.42248619256d + (5507.5532386674d * T));
        this.B0 += 8.0445E-7d * Math.cos(3.88013204458d + (5223.6939198022d * T));
        this.B0 += 4.3806E-7d * Math.cos(3.70444689758d + (2352.8661537718d * T));
        this.B0 += 3.1933E-7d * Math.cos(4.00026369781d + (1577.3435424478d * T));
        this.B0 += 2.2724E-7d * Math.cos(3.9847383156d + (1047.7473117547d * T));
        this.B0 += 1.6392E-7d * Math.cos(3.56456119782d + (5856.4776591154d * T));
        this.B1 += 0.00227778d * Math.cos(3.413766d + (6283.07585d * T));
        this.B1 += 3.806E-5d * Math.cos(3.3706d + (12566.1517d * T));
        this.B1 += 3.62E-5d * Math.cos(0.0d + (0.0d * T));
        this.B1 += 7.2E-7d * Math.cos(3.33d + (18849.23d * T));
        this.B1 += 8.0E-8d * Math.cos(3.89d + (5507.55d * T));
        this.B1 += 8.0E-8d * Math.cos(1.79d + (5223.69d * T));
        this.B1 += 6.0E-8d * Math.cos(5.2d + (2352.87d * T));
        this.B2 += 9.721E-5d * Math.cos(5.1519d + (6283.07585d * T));
        this.B2 += 2.33E-6d * Math.cos(3.1416d);
        this.B2 += 1.34E-6d * Math.cos(0.644d + (12566.152d * T));
        this.B2 += 7.0E-8d * Math.cos(1.07d + (18849.23d * T));
        this.B3 += 2.76E-6d * Math.cos(0.595d + (6283.076d * T));
        this.B3 += 1.7E-7d * Math.cos(3.14d);
        this.B3 += 4.0E-8d * Math.cos(0.12d + (12566.15d * T));
        this.B4 += 6.0E-8d * Math.cos(2.27d + (6283.08d * T));
        this.B4 += 1.0E-8d;
        this.R0 += 1.00013988799d * Math.cos(0.0d + (0.0d * T));
        this.R0 += 0.01670699626d * Math.cos(3.09846350771d + (6283.0758499914d * T));
        this.R0 += 1.3956023E-4d * Math.cos(3.0552460962d + (12566.1516999828d * T));
        this.R0 += 3.08372E-5d * Math.cos(5.19846674381d + (77713.7714681205d * T));
        this.R0 += 1.628461E-5d * Math.cos(1.17387749012d + (5753.3848848968d * T));
        this.R0 += 1.575568E-5d * Math.cos(2.84685245825d + (7860.4193924392d * T));
        this.R0 += 9.24799E-6d * Math.cos(5.45292234084d + (11506.7697697936d * T));
        this.R0 += 5.42444E-6d * Math.cos(4.56409149777d + (3930.2096962196d * T));
        this.R0 += 4.7211E-6d * Math.cos(3.66100022149d + (5884.9268465832d * T));
        this.R0 += 3.2878E-6d * Math.cos(5.89983646482d + (5223.6939198022d * T));
        this.R0 += 3.45983E-6d * Math.cos(0.96368617687d + (5507.5532386674d * T));
        this.R0 += 3.06784E-6d * Math.cos(0.29867139512d + (5573.1428014331d * T));
        this.R0 += 1.74844E-6d * Math.cos(3.01193636534d + (18849.2275499742d * T));
        this.R0 += 2.43189E-6d * Math.cos(4.27349536153d + (11790.6290886588d * T));
        this.R0 += 2.11829E-6d * Math.cos(5.84714540314d + (1577.3435424478d * T));
        this.R0 += 1.85752E-6d * Math.cos(5.02194447178d + (10977.078804699d * T));
        this.R0 += 1.09835E-6d * Math.cos(5.05510636285d + (5486.777843175d * T));
        this.R0 += 9.8316E-7d * Math.cos(0.88681311277d + (6069.7767545534d * T));
        this.R0 += 8.6499E-7d * Math.cos(5.68959778254d + (15720.8387848784d * T));
        this.R0 += 8.5825E-7d * Math.cos(1.27083733351d + (161000.685737674d * T));
        this.R0 += 6.2916E-7d * Math.cos(0.92177108832d + (529.6909650946d * T));
        this.R0 += 5.7056E-7d * Math.cos(2.01374292014d + (83996.8473181119d * T));
        this.R0 += 6.4903E-7d * Math.cos(0.27250613787d + (17260.1546546904d * T));
        this.R0 += 4.9384E-7d * Math.cos(3.24501240359d + (2544.3144198834d * T));
        this.R0 += 5.5736E-7d * Math.cos(5.24159798933d + (71430.6956181291d * T));
        this.R0 += 4.2515E-7d * Math.cos(6.01110242003d + (6275.9623029906d * T));
        this.R0 += 4.6963E-7d * Math.cos(2.57805070386d + (775.522611324d * T));
        this.R0 += 3.8968E-7d * Math.cos(5.36071738169d + (4694.0029547076d * T));
        this.R0 += 4.4661E-7d * Math.cos(5.53715807302d + (9437.762934887d * T));
        this.R0 += 3.566E-7d * Math.cos(1.67468058995d + (12036.4607348882d * T));
        this.R0 += 3.1921E-7d * Math.cos(0.18368229781d + (5088.6288397668d * T));
        this.R0 += 3.1846E-7d * Math.cos(1.77775642085d + (398.1490034082d * T));
        this.R0 += 3.3193E-7d * Math.cos(0.24370300098d + (7084.8967811152d * T));
        this.R0 += 3.8245E-7d * Math.cos(2.39255343974d + (8827.3902698748d * T));
        this.R0 += 2.8464E-7d * Math.cos(1.21344868176d + (6286.5989683404d * T));
        this.R0 += 3.749E-7d * Math.cos(0.82952922332d + (19651.048481098d * T));
        this.R0 += 3.6957E-7d * Math.cos(4.90107591914d + (12139.5535091068d * T));
        this.R0 += 3.4537E-7d * Math.cos(1.84270693282d + (2942.4634232916d * T));
        this.R0 += 2.7793E-7d * Math.cos(1.89934330904d + (6279.5527316424d * T));
        this.R0 += 2.6275E-7d * Math.cos(4.58896850401d + (10447.3878396044d * T));
        this.R0 += 2.4596E-7d * Math.cos(3.78660875483d + (8429.2412664666d * T));
        this.R0 += 2.3587E-7d * Math.cos(0.26866117066d + (796.2980068164d * T));
        this.R0 += 2.3927E-7d * Math.cos(4.99598548138d + (5856.4776591154d * T));
        this.R0 += 2.0349E-7d * Math.cos(4.65267995431d + (2146.1654164752d * T));
        this.R0 += 2.3287E-7d * Math.cos(2.80783650928d + (14143.4952424306d * T));
        this.R0 += 2.2103E-7d * Math.cos(1.95004702988d + (3154.6870848956d * T));
        this.R0 += 1.9506E-7d * Math.cos(5.38227371393d + (2352.8661537718d * T));
        this.R0 += 1.7958E-7d * Math.cos(0.19871379385d + (6812.766815086d * T));
        this.R1 += 0.00103018608d * Math.cos(1.10748969588d + (6283.0758499914d * T));
        this.R1 += 1.721238E-5d * Math.cos(1.06442301418d + (12566.1516999828d * T));
        this.R1 += 7.02215E-6d * Math.cos(3.14159265359d + (0.0d * T));
        this.R1 += 3.2346E-7d * Math.cos(1.02169059149d + (18849.2275499742d * T));
        this.R1 += 3.0799E-7d * Math.cos(2.84353804832d + (5507.5532386674d * T));
        this.R1 += 2.4971E-7d * Math.cos(1.31906709482d + (5223.6939198022d * T));
        this.R1 += 1.8485E-7d * Math.cos(1.42429748614d + (1577.3435424478d * T));
        this.R1 += 1.0078E-7d * Math.cos(5.91378194648d + (10977.078804699d * T));
        this.R1 += 8.634E-8d * Math.cos(0.27146150602d + (5486.777843175d * T));
        this.R1 += 8.654E-8d * Math.cos(1.42046854427d + (6275.9623029906d * T));
        this.R1 += 5.069E-8d * Math.cos(1.68613426734d + (5088.6288397668d * T));
        this.R1 += 4.985E-8d * Math.cos(6.01401770704d + (6286.5989683404d * T));
        this.R1 += 4.669E-8d * Math.cos(5.98724494073d + (529.6909650946d * T));
        this.R1 += 4.395E-8d * Math.cos(0.51800238019d + (4694.0029547076d * T));
        this.R1 += 3.872E-8d * Math.cos(4.74969833437d + (2544.3144198834d * T));
        this.R1 += 3.75E-8d * Math.cos(5.07097685568d + (796.2980068164d * T));
        this.R2 += 4.359385E-5d * Math.cos(5.78455133738d + (6283.0758499914d * T));
        this.R2 += 1.23633E-6d * Math.cos(5.57934722157d + (12566.1516999828d * T));
        this.R2 += 1.2341E-7d * Math.cos(3.14159265359d + (0.0d * T));
        this.R2 += 8.792E-8d * Math.cos(3.62777733395d + (77713.7714681205d * T));
        this.R2 += 5.689E-8d * Math.cos(1.86958905084d + (5573.1428014331d * T));
        this.R2 += 3.301E-8d * Math.cos(5.47027913302d + (18849.2275499742d * T));
        this.R2 += 1.471E-8d * Math.cos(4.48028885617d + (5507.5532386674d * T));
        this.R2 += 1.013E-8d * Math.cos(2.81456417694d + (5223.6939198022d * T));
        this.R2 += 8.54E-9d * Math.cos(3.10878241236d + (1577.3435424478d * T));
        this.R2 += 1.102E-8d * Math.cos(2.84173992403d + (161000.685737674d * T));
        this.R2 += 6.48E-9d * Math.cos(5.47349498544d + (775.522611324d * T));
        this.R3 += 1.44595E-6d * Math.cos(4.27319435148d + (6283.0758499914d * T));
        this.R3 += 6.729E-8d * Math.cos(3.91697608662d + (12566.1516999828d * T));
        this.R3 += 7.74E-9d * Math.cos(0.0d + (0.0d * T));
        this.R3 += 2.47E-9d * Math.cos(3.73019298781d + (18849.2275499742d * T));
        this.R3 += 3.6E-10d * Math.cos(2.8008140905d + (6286.5989683404d * T));
        this.R4 += 3.858E-8d * Math.cos(2.56384387339d + (6283.0758499914d * T));
        this.R4 += 3.06E-9d * Math.cos(2.2676950123d + (12566.1516999828d * T));
        this.R4 += 5.3E-10d * Math.cos(3.44031471924d + (5573.1428014331d * T));
    }

    protected void computePeriodicTermsHighPrecision(double T) {
        this.L0 += 1.75347045673d * Math.cos(0.0d + (0.0d * T));
        this.L0 += 0.03341656456d * Math.cos(4.66925680417d + (6283.0758499914d * T));
        this.L0 += 3.4894275E-4d * Math.cos(4.62610241759d + (12566.1516999828d * T));
        this.L0 += 3.417571E-5d * Math.cos(2.82886579606d + (3.523118349d * T));
        this.L0 += 3.497056E-5d * Math.cos(2.74411800971d + (5753.3848848968d * T));
        this.L0 += 3.135896E-5d * Math.cos(3.62767041758d + (77713.7714681205d * T));
        this.L0 += 2.676218E-5d * Math.cos(4.41808351397d + (7860.4193924392d * T));
        this.L0 += 2.342687E-5d * Math.cos(6.13516237631d + (3930.2096962196d * T));
        this.L0 += 1.273166E-5d * Math.cos(2.03709655772d + (529.6909650946d * T));
        this.L0 += 1.324292E-5d * Math.cos(0.74246356352d + (11506.7697697936d * T));
        this.L0 += 9.01855E-6d * Math.cos(2.04505443513d + (26.2983197998d * T));
        this.L0 += 1.199167E-5d * Math.cos(1.10962944315d + (1577.3435424478d * T));
        this.L0 += 8.57223E-6d * Math.cos(3.50849156957d + (398.1490034082d * T));
        this.L0 += 7.79786E-6d * Math.cos(1.17882652114d + (5223.6939198022d * T));
        this.L0 += 9.9025E-6d * Math.cos(5.23268129594d + (5884.9268465832d * T));
        this.L0 += 7.53141E-6d * Math.cos(2.53339053818d + (5507.5532386674d * T));
        this.L0 += 5.05264E-6d * Math.cos(4.58292563052d + (18849.2275499742d * T));
        this.L0 += 4.92379E-6d * Math.cos(4.20506639861d + (775.522611324d * T));
        this.L0 += 3.56655E-6d * Math.cos(2.91954116867d + (0.0673103028d * T));
        this.L0 += 2.84125E-6d * Math.cos(1.89869034186d + (796.2980068164d * T));
        this.L0 += 2.4281E-6d * Math.cos(0.34481140906d + (5486.777843175d * T));
        this.L0 += 3.17087E-6d * Math.cos(5.84901952218d + (11790.6290886588d * T));
        this.L0 += 2.71039E-6d * Math.cos(0.31488607649d + (10977.078804699d * T));
        this.L0 += 2.0616E-6d * Math.cos(4.80646606059d + (2544.3144198834d * T));
        this.L0 += 2.05385E-6d * Math.cos(1.86947813692d + (5573.1428014331d * T));
        this.L0 += 2.02261E-6d * Math.cos(2.45767795458d + (6069.7767545534d * T));
        this.L0 += 1.26184E-6d * Math.cos(1.0830263021d + (20.7753954924d * T));
        this.L0 += 1.55516E-6d * Math.cos(0.83306073807d + (213.299095438d * T));
        this.L0 += 1.15132E-6d * Math.cos(0.64544911683d + (0.9803210682d * T));
        this.L0 += 1.02851E-6d * Math.cos(0.63599846727d + (4694.0029547076d * T));
        this.L0 += 1.01724E-6d * Math.cos(4.26679821365d + (7.1135470008d * T));
        this.L0 += 9.9206E-7d * Math.cos(6.20992940258d + (2146.1654164752d * T));
        this.L0 += 1.32212E-6d * Math.cos(3.41118275555d + (2942.4634232916d * T));
        this.L0 += 9.7607E-7d * Math.cos(0.6810127227d + (155.4203994342d * T));
        this.L0 += 8.5128E-7d * Math.cos(1.29870743025d + (6275.9623029906d * T));
        this.L0 += 7.4651E-7d * Math.cos(1.75508916159d + (5088.6288397668d * T));
        this.L0 += 1.01895E-6d * Math.cos(0.97569221824d + (15720.8387848784d * T));
        this.L0 += 8.4711E-7d * Math.cos(3.67080093025d + (71430.6956181291d * T));
        this.L0 += 7.3547E-7d * Math.cos(4.67926565481d + (801.8209311238d * T));
        this.L0 += 7.3874E-7d * Math.cos(3.50319443167d + (3154.6870848956d * T));
        this.L0 += 7.8756E-7d * Math.cos(3.03698313141d + (12036.4607348882d * T));
        this.L0 += 7.9637E-7d * Math.cos(1.807913307d + (17260.1546546904d * T));
        this.L0 += 8.5803E-7d * Math.cos(5.98322631256d + (161000.685737674d * T));
        this.L0 += 5.6963E-7d * Math.cos(2.78430398043d + (6286.5989683404d * T));
        this.L0 += 6.1148E-7d * Math.cos(1.81839811024d + (7084.8967811152d * T));
        this.L0 += 6.9627E-7d * Math.cos(0.83297596966d + (9437.762934887d * T));
        this.L0 += 5.6116E-7d * Math.cos(4.38694880779d + (14143.4952424306d * T));
        this.L0 += 6.2449E-7d * Math.cos(3.97763880587d + (8827.3902698748d * T));
        this.L0 += 5.1145E-7d * Math.cos(0.28306864501d + (5856.4776591154d * T));
        this.L0 += 5.5577E-7d * Math.cos(3.47006009062d + (6279.5527316424d * T));
        this.L0 += 4.1036E-7d * Math.cos(5.36817351402d + (8429.2412664666d * T));
        this.L0 += 5.1605E-7d * Math.cos(1.33282746983d + (1748.016413067d * T));
        this.L0 += 5.1992E-7d * Math.cos(0.18914945834d + (12139.5535091068d * T));
        this.L0 += 4.9E-7d * Math.cos(0.48735065033d + (1194.4470102246d * T));
        this.L0 += 3.92E-7d * Math.cos(6.16832995016d + (10447.3878396044d * T));
        this.L0 += 3.5566E-7d * Math.cos(1.77597314691d + (6812.766815086d * T));
        this.L0 += 3.677E-7d * Math.cos(6.04133859347d + (10213.285546211d * T));
        this.L0 += 3.6596E-7d * Math.cos(2.56955238628d + (1059.3819301892d * T));
        this.L0 += 3.3291E-7d * Math.cos(0.59309499459d + (17789.845619785d * T));
        this.L0 += 3.5954E-7d * Math.cos(1.70876111898d + (2352.8661537718d * T));
        this.L0 += 4.0938E-7d * Math.cos(2.39850881707d + (19651.048481098d * T));
        this.L0 += 3.0047E-7d * Math.cos(2.73975123935d + (1349.8674096588d * T));
        this.L0 += 3.0412E-7d * Math.cos(0.44294464135d + (83996.8473181119d * T));
        this.L0 += 2.3663E-7d * Math.cos(0.48473567763d + (8031.0922630584d * T));
        this.L0 += 2.3574E-7d * Math.cos(2.06527720049d + (3340.6124266998d * T));
        this.L0 += 2.1089E-7d * Math.cos(4.14825464101d + (951.7184062506d * T));
        this.L0 += 2.4738E-7d * Math.cos(0.21484762138d + (3.5904286518d * T));
        this.L0 += 2.5352E-7d * Math.cos(3.16470953405d + (4690.4798363586d * T));
        this.L0 += 2.282E-7d * Math.cos(5.22197888032d + (4705.7323075436d * T));
        this.L0 += 2.1419E-7d * Math.cos(1.42563735525d + (16730.4636895958d * T));
        this.L0 += 2.1891E-7d * Math.cos(5.55594302562d + (553.5694028424d * T));
        this.L0 += 1.7481E-7d * Math.cos(4.56052900359d + (135.0650800354d * T));
        this.L0 += 1.9925E-7d * Math.cos(5.22208471269d + (12168.0026965746d * T));
        this.L0 += 1.986E-7d * Math.cos(5.77470167653d + (6309.3741697912d * T));
        this.L0 += 2.03E-7d * Math.cos(0.37133792946d + (283.8593188652d * T));
        this.L0 += 1.4421E-7d * Math.cos(4.19315332546d + (242.728603974d * T));
        this.L0 += 1.6225E-7d * Math.cos(5.98837722564d + (11769.8536931664d * T));
        this.L0 += 1.5077E-7d * Math.cos(4.19567181073d + (6256.7775301916d * T));
        this.L0 += 1.9124E-7d * Math.cos(3.82219996949d + (23581.2581773176d * T));
        this.L0 += 1.8888E-7d * Math.cos(5.38626880969d + (149854.400134808d * T));
        this.L0 += 1.4346E-7d * Math.cos(3.72355084422d + (38.0276726358d * T));
        this.L0 += 1.7898E-7d * Math.cos(2.21490735647d + (13367.9726311066d * T));
        this.L1 += 6283.31966747491d * Math.cos(0.0d + (0.0d * T));
        this.L1 += 0.00206058863d * Math.cos(2.67823455584d + (6283.0758499914d * T));
        this.L1 += 4.30343E-5d * Math.cos(2.63512650414d + (12566.1516999828d * T));
        this.L1 += 4.25264E-6d * Math.cos(1.59046980729d + (3.523118349d * T));
        this.L1 += 1.08977E-6d * Math.cos(2.96618001993d + (1577.3435424478d * T));
        this.L1 += 9.3478E-7d * Math.cos(2.59212835365d + (18849.2275499742d * T));
        this.L1 += 1.19261E-6d * Math.cos(5.79557487799d + (26.2983197998d * T));
        this.L1 += 7.2122E-7d * Math.cos(1.13846158196d + (529.6909650946d * T));
        this.L1 += 6.7768E-7d * Math.cos(1.87472304791d + (398.1490034082d * T));
        this.L1 += 6.7327E-7d * Math.cos(4.40918235168d + (5507.5532386674d * T));
        this.L1 += 5.9027E-7d * Math.cos(2.8879703846d + (5223.6939198022d * T));
        this.L1 += 5.5976E-7d * Math.cos(2.17471680261d + (155.4203994342d * T));
        this.L1 += 4.5407E-7d * Math.cos(0.39803079805d + (796.2980068164d * T));
        this.L1 += 3.6369E-7d * Math.cos(0.46624739835d + (775.522611324d * T));
        this.L1 += 2.8958E-7d * Math.cos(2.64707383882d + (7.1135470008d * T));
        this.L1 += 1.9097E-7d * Math.cos(1.84628332577d + (5486.777843175d * T));
        this.L1 += 2.0844E-7d * Math.cos(5.34138275149d + (0.9803210682d * T));
        this.L1 += 1.8508E-7d * Math.cos(4.96855124577d + (213.299095438d * T));
        this.L1 += 1.6233E-7d * Math.cos(0.03216483047d + (2544.3144198834d * T));
        this.L1 += 1.7293E-7d * Math.cos(2.99116864949d + (6275.9623029906d * T));
        this.L1 += 1.5832E-7d * Math.cos(1.43049285325d + (2146.1654164752d * T));
        this.L1 += 1.4615E-7d * Math.cos(1.20532366323d + (10977.078804699d * T));
        this.L1 += 1.1877E-7d * Math.cos(3.25804815607d + (5088.6288397668d * T));
        this.L1 += 1.1514E-7d * Math.cos(2.07502418155d + (4694.0029547076d * T));
        this.L1 += 9.721E-8d * Math.cos(4.23925472239d + (1349.8674096588d * T));
        this.L1 += 9.969E-8d * Math.cos(1.30262991097d + (6286.5989683404d * T));
        this.L1 += 9.452E-8d * Math.cos(2.69957062864d + (242.728603974d * T));
        this.L1 += 1.2461E-7d * Math.cos(2.83432285512d + (1748.016413067d * T));
        this.L1 += 1.1808E-7d * Math.cos(5.2737979048d + (1194.4470102246d * T));
        this.L1 += 8.577E-8d * Math.cos(5.64475868067d + (951.7184062506d * T));
        this.L1 += 1.0641E-7d * Math.cos(0.76614199202d + (553.5694028424d * T));
        this.L1 += 7.576E-8d * Math.cos(5.30062664886d + (2352.8661537718d * T));
        this.L1 += 5.834E-8d * Math.cos(1.76649917904d + (1059.3819301892d * T));
        this.L1 += 6.385E-8d * Math.cos(2.65033984967d + (9437.762934887d * T));
        this.L1 += 5.223E-8d * Math.cos(5.66135767624d + (71430.6956181291d * T));
        this.L1 += 5.305E-8d * Math.cos(0.90857521574d + (3154.6870848956d * T));
        this.L1 += 6.101E-8d * Math.cos(4.66632584188d + (4690.4798363586d * T));
        this.L1 += 4.33E-8d * Math.cos(0.24102555403d + (6812.766815086d * T));
        this.L1 += 5.041E-8d * Math.cos(1.42490103709d + (6438.4962494256d * T));
        this.L1 += 4.259E-8d * Math.cos(0.77355900599d + (10447.3878396044d * T));
        this.L1 += 5.198E-8d * Math.cos(1.85353197345d + (801.8209311238d * T));
        this.L1 += 3.744E-8d * Math.cos(2.00119516488d + (8031.0922630584d * T));
        this.L1 += 3.558E-8d * Math.cos(2.42901552681d + (14143.4952424306d * T));
        this.L1 += 3.372E-8d * Math.cos(3.86210700128d + (1592.5960136328d * T));
        this.L1 += 3.374E-8d * Math.cos(0.88776219727d + (12036.4607348882d * T));
        this.L1 += 3.175E-8d * Math.cos(3.18785710594d + (4705.7323075436d * T));
        this.L1 += 3.221E-8d * Math.cos(0.61599835472d + (8429.2412664666d * T));
        this.L1 += 4.132E-8d * Math.cos(5.23992859705d + (7084.8967811152d * T));
        this.L1 += 2.97E-8d * Math.cos(6.07026318493d + (4292.3308329504d * T));
        this.L1 += 2.9E-8d * Math.cos(2.32464208411d + (20.3553193988d * T));
        this.L2 += 5.291887E-4d * Math.cos(0.0d + (0.0d * T));
        this.L2 += 8.719837E-5d * Math.cos(1.07209665242d + (6283.0758499914d * T));
        this.L2 += 3.09125E-6d * Math.cos(0.86728818832d + (12566.1516999828d * T));
        this.L2 += 2.7339E-7d * Math.cos(0.05297871691d + (3.523118349d * T));
        this.L2 += 1.6334E-7d * Math.cos(5.18826691036d + (26.2983197998d * T));
        this.L2 += 1.5752E-7d * Math.cos(3.6845788943d + (155.4203994342d * T));
        this.L2 += 9.541E-8d * Math.cos(0.75742297675d + (18849.2275499742d * T));
        this.L2 += 8.937E-8d * Math.cos(2.05705419118d + (77713.7714681205d * T));
        this.L2 += 6.952E-8d * Math.cos(0.8267330541d + (775.522611324d * T));
        this.L2 += 5.064E-8d * Math.cos(4.66284525271d + (1577.3435424478d * T));
        this.L2 += 4.061E-8d * Math.cos(1.03057162962d + (7.1135470008d * T));
        this.L2 += 3.463E-8d * Math.cos(5.14074632811d + (796.2980068164d * T));
        this.L2 += 3.169E-8d * Math.cos(6.05291851171d + (5507.5532386674d * T));
        this.L2 += 3.02E-8d * Math.cos(1.19246506441d + (242.728603974d * T));
        this.L2 += 2.886E-8d * Math.cos(6.11652627155d + (529.6909650946d * T));
        this.L2 += 3.81E-8d * Math.cos(3.4405080349d + (5573.1428014331d * T));
        this.L2 += 2.714E-8d * Math.cos(0.30637881025d + (398.1490034082d * T));
        this.L2 += 2.371E-8d * Math.cos(4.38118838167d + (5223.6939198022d * T));
        this.L2 += 2.538E-8d * Math.cos(2.27992810679d + (553.5694028424d * T));
        this.L2 += 2.079E-8d * Math.cos(3.75435330484d + (0.9803210682d * T));
        this.L2 += 1.675E-8d * Math.cos(0.90216407959d + (951.7184062506d * T));
        this.L2 += 1.534E-8d * Math.cos(5.75900462759d + (1349.8674096588d * T));
        this.L2 += 1.224E-8d * Math.cos(2.97328088405d + (2146.1654164752d * T));
        this.L2 += 1.449E-8d * Math.cos(4.3641591397d + (1748.016413067d * T));
        this.L2 += 1.341E-8d * Math.cos(3.72061130861d + (1194.4470102246d * T));
        this.L2 += 1.254E-8d * Math.cos(2.94846826628d + (6438.4962494256d * T));
        this.L2 += 9.99E-9d * Math.cos(5.98640014468d + (6286.5989683404d * T));
        this.L2 += 9.17E-9d * Math.cos(4.79788687522d + (5088.6288397668d * T));
        this.L3 += 2.89226E-6d * Math.cos(5.84384198723d + (6283.0758499914d * T));
        this.L3 += 3.4955E-7d * Math.cos(0.0d + (0.0d * T));
        this.L3 += 1.6819E-7d * Math.cos(5.48766912348d + (12566.1516999828d * T));
        this.L3 += 2.962E-8d * Math.cos(5.19577265202d + (155.4203994342d * T));
        this.L3 += 1.288E-8d * Math.cos(4.72200252235d + (3.523118349d * T));
        this.L3 += 6.35E-9d * Math.cos(5.96925937141d + (242.728603974d * T));
        this.L3 += 7.14E-9d * Math.cos(5.30045809128d + (18849.2275499742d * T));
        this.L3 += 4.02E-9d * Math.cos(3.78682982419d + (553.5694028424d * T));
        this.L3 += 7.2E-10d * Math.cos(4.2976812618d + (6286.5989683404d * T));
        this.L3 += 6.7E-10d * Math.cos(0.90721687647d + (6127.6554505572d * T));
        this.L3 += 3.6E-10d * Math.cos(5.24029648014d + (6438.4962494256d * T));
        this.L3 += 2.4E-10d * Math.cos(5.16003960716d + (25132.3033999656d * T));
        this.L3 += 2.3E-10d * Math.cos(3.01921570335d + (6309.3741697912d * T));
        this.L4 += 1.14084E-6d * Math.cos(3.14159265359d + (0.0d * T));
        this.L4 += 7.717E-8d * Math.cos(4.13446589358d + (6283.0758499914d * T));
        this.L4 += 7.65E-9d * Math.cos(3.83803776214d + (12566.1516999828d * T));
        this.L4 += 4.2E-9d * Math.cos(0.41925861858d + (155.4203994342d * T));
        this.L4 += 4.0E-10d * Math.cos(3.5984758584d + (18849.2275499742d * T));
        this.L4 += 4.1E-10d * Math.cos(3.14398414077d + (3.523118349d * T));
        this.L4 += 3.5E-10d * Math.cos(5.00298940826d + (5573.1428014331d * T));
        this.L5 += 8.78E-9d * Math.cos(3.14159265359d + (0.0d * T));
        this.L5 += 1.72E-9d * Math.cos(2.7657906951d + (6283.0758499914d * T));
        this.B0 += 2.7962E-6d * Math.cos(3.19870156017d + (84334.6615813083d * T));
        this.B0 += 1.01643E-6d * Math.cos(5.42248619256d + (5507.5532386674d * T));
        this.B0 += 8.0445E-7d * Math.cos(3.88013204458d + (5223.6939198022d * T));
        this.B0 += 4.3806E-7d * Math.cos(3.70444689758d + (2352.8661537718d * T));
        this.B0 += 3.1933E-7d * Math.cos(4.00026369781d + (1577.3435424478d * T));
        this.B0 += 2.2724E-7d * Math.cos(3.9847383156d + (1047.7473117547d * T));
        this.B0 += 1.6392E-7d * Math.cos(3.56456119782d + (5856.4776591154d * T));
        this.B0 += 1.8141E-7d * Math.cos(4.98367470263d + (6283.0758499914d * T));
        this.B0 += 1.4443E-7d * Math.cos(3.70275614914d + (9437.762934887d * T));
        this.B0 += 1.4304E-7d * Math.cos(3.41117857525d + (10213.285546211d * T));
        this.B0 += 1.1246E-7d * Math.cos(4.8282069053d + (14143.4952424306d * T));
        this.B0 += 1.09E-7d * Math.cos(2.08574562327d + (6812.766815086d * T));
        this.B0 += 9.714E-8d * Math.cos(3.47303947752d + (4694.0029547076d * T));
        this.B0 += 1.0367E-7d * Math.cos(4.05663927946d + (71092.8813549327d * T));
        this.B0 += 8.775E-8d * Math.cos(4.44016515669d + (5753.3848848968d * T));
        this.B0 += 8.366E-8d * Math.cos(4.9925151218d + (7084.8967811152d * T));
        this.B0 += 6.921E-8d * Math.cos(4.32559054073d + (6275.9623029906d * T));
        this.B0 += 9.145E-8d * Math.cos(1.14182646613d + (6620.8901131878d * T));
        this.B0 += 7.194E-8d * Math.cos(3.60193205752d + (529.6909650946d * T));
        this.B0 += 7.698E-8d * Math.cos(5.55425745881d + (167621.575850862d * T));
        this.B0 += 5.285E-8d * Math.cos(2.48446991566d + (4705.7323075436d * T));
        this.B0 += 5.208E-8d * Math.cos(6.24992674537d + (18073.7049386502d * T));
        this.B0 += 4.529E-8d * Math.cos(2.33827747356d + (6309.3741697912d * T));
        this.B0 += 5.579E-8d * Math.cos(4.41023653738d + (7860.4193924392d * T));
        this.B0 += 4.743E-8d * Math.cos(0.70995680136d + (5884.9268465832d * T));
        this.B0 += 4.301E-8d * Math.cos(1.10255777773d + (6681.2248533996d * T));
        this.B0 += 3.849E-8d * Math.cos(1.82229412531d + (5486.777843175d * T));
        this.B0 += 4.093E-8d * Math.cos(5.11700141207d + (13367.9726311066d * T));
        this.B0 += 3.681E-8d * Math.cos(0.43793170356d + (3154.6870848956d * T));
        this.B0 += 3.42E-8d * Math.cos(5.42034800952d + (6069.7767545534d * T));
        this.B0 += 3.617E-8d * Math.cos(6.04641937526d + (3930.2096962196d * T));
        this.B0 += 3.67E-8d * Math.cos(4.58210192227d + (12194.0329146209d * T));
        this.B0 += 2.918E-8d * Math.cos(1.95463881126d + (10977.078804699d * T));
        this.B0 += 2.797E-8d * Math.cos(5.61259275048d + (11790.6290886588d * T));
        this.B0 += 2.502E-8d * Math.cos(0.60499729367d + (6496.3749454294d * T));
        this.B0 += 2.319E-8d * Math.cos(5.01648216014d + (1059.3819301892d * T));
        this.B0 += 2.684E-8d * Math.cos(1.39470396488d + (22003.9146348698d * T));
        this.B0 += 2.428E-8d * Math.cos(3.24183056052d + (78051.5857313169d * T));
        this.B0 += 2.12E-8d * Math.cos(4.30691000285d + (5643.1785636774d * T));
        this.B0 += 2.257E-8d * Math.cos(3.15557225618d + (90617.7374312997d * T));
        this.B0 += 1.813E-8d * Math.cos(3.75574218285d + (3340.6124266998d * T));
        this.B0 += 2.226E-8d * Math.cos(2.79699346659d + (12036.4607348882d * T));
        this.B1 += 9.03E-8d * Math.cos(3.8972906189d + (5507.5532386674d * T));
        this.B1 += 6.177E-8d * Math.cos(1.73038850355d + (5223.6939198022d * T));
        this.B1 += 3.8E-8d * Math.cos(5.24404145734d + (2352.8661537718d * T));
        this.B1 += 2.834E-8d * Math.cos(2.4734503745d + (1577.3435424478d * T));
        this.B1 += 1.817E-8d * Math.cos(0.41874743765d + (6283.0758499914d * T));
        this.B1 += 1.499E-8d * Math.cos(1.83320979291d + (5856.4776591154d * T));
        this.B1 += 1.466E-8d * Math.cos(5.69401926017d + (5753.3848848968d * T));
        this.B1 += 1.301E-8d * Math.cos(2.18890066314d + (9437.762934887d * T));
        this.B1 += 1.233E-8d * Math.cos(4.95222451476d + (10213.285546211d * T));
        this.B1 += 1.021E-8d * Math.cos(0.12866660208d + (7860.4193924392d * T));
        this.B1 += 9.82E-9d * Math.cos(0.09005453285d + (14143.4952424306d * T));
        this.B1 += 8.65E-9d * Math.cos(1.73949953555d + (3930.2096962196d * T));
        this.B1 += 5.81E-9d * Math.cos(2.26949174067d + (5884.9268465832d * T));
        this.B1 += 5.24E-9d * Math.cos(5.65662503159d + (529.6909650946d * T));
        this.B1 += 4.73E-9d * Math.cos(6.22750969242d + (6309.3741697912d * T));
        this.B1 += 4.51E-9d * Math.cos(1.53288619213d + (18073.7049386502d * T));
        this.B1 += 3.64E-9d * Math.cos(3.61614477374d + (13367.9726311066d * T));
        this.B1 += 3.72E-9d * Math.cos(3.2247072132d + (6275.9623029906d * T));
        this.B1 += 2.68E-9d * Math.cos(2.34341267879d + (11790.6290886588d * T));
        this.B1 += 3.22E-9d * Math.cos(0.94084045832d + (6069.7767545534d * T));
        this.B1 += 2.32E-9d * Math.cos(0.26781182579d + (7058.5984613154d * T));
        this.B1 += 2.16E-9d * Math.cos(6.05952221329d + (10977.078804699d * T));
        this.B1 += 2.32E-9d * Math.cos(2.93325646109d + (22003.9146348698d * T));
        this.B1 += 2.04E-9d * Math.cos(3.86264841382d + (6496.3749454294d * T));
        this.B1 += 2.02E-9d * Math.cos(2.81892511133d + (15720.8387848784d * T));
        this.B1 += 1.85E-9d * Math.cos(4.93512381859d + (12036.4607348882d * T));
        this.B2 += 1.662E-8d * Math.cos(1.62703209173d + (84334.6615813083d * T));
        this.B2 += 4.92E-9d * Math.cos(2.41382223971d + (1047.7473117547d * T));
        this.B2 += 3.44E-9d * Math.cos(2.24353004539d + (5507.5532386674d * T));
        this.B2 += 2.58E-9d * Math.cos(6.00906896311d + (5223.6939198022d * T));
        this.B2 += 1.31E-9d * Math.cos(0.9544734524d + (6283.0758499914d * T));
        this.B2 += 8.6E-10d * Math.cos(1.67530247303d + (7860.4193924392d * T));
        this.B2 += 9.0E-10d * Math.cos(0.97606804452d + (1577.3435424478d * T));
        this.B2 += 9.0E-10d * Math.cos(0.37899871725d + (2352.8661537718d * T));
        this.B2 += 8.9E-10d * Math.cos(6.25807507963d + (10213.285546211d * T));
        this.B2 += 7.5E-10d * Math.cos(0.84213523741d + (167621.575850862d * T));
        this.B2 += 5.2E-10d * Math.cos(1.70501566089d + (14143.4952424306d * T));
        this.B2 += 5.7E-10d * Math.cos(6.15295833679d + (12194.0329146209d * T));
        this.B2 += 5.1E-10d * Math.cos(1.2761601674d + (5753.3848848968d * T));
        this.B2 += 5.1E-10d * Math.cos(5.37229738682d + (6812.766815086d * T));
        this.B2 += 3.4E-10d * Math.cos(1.73672994279d + (7058.5984613154d * T));
        this.R0 += 1.00013988799d * Math.cos(0.0d + (0.0d * T));
        this.R0 += 0.01670699626d * Math.cos(3.09846350771d + (6283.0758499914d * T));
        this.R0 += 1.3956023E-4d * Math.cos(3.0552460962d + (12566.1516999828d * T));
        this.R0 += 3.08372E-5d * Math.cos(5.19846674381d + (77713.7714681205d * T));
        this.R0 += 1.628461E-5d * Math.cos(1.17387749012d + (5753.3848848968d * T));
        this.R0 += 1.575568E-5d * Math.cos(2.84685245825d + (7860.4193924392d * T));
        this.R0 += 9.24799E-6d * Math.cos(5.45292234084d + (11506.7697697936d * T));
        this.R0 += 5.42444E-6d * Math.cos(4.56409149777d + (3930.2096962196d * T));
        this.R0 += 4.7211E-6d * Math.cos(3.66100022149d + (5884.9268465832d * T));
        this.R0 += 3.2878E-6d * Math.cos(5.89983646482d + (5223.6939198022d * T));
        this.R0 += 3.45983E-6d * Math.cos(0.96368617687d + (5507.5532386674d * T));
        this.R0 += 3.06784E-6d * Math.cos(0.29867139512d + (5573.1428014331d * T));
        this.R0 += 1.74844E-6d * Math.cos(3.01193636534d + (18849.2275499742d * T));
        this.R0 += 2.43189E-6d * Math.cos(4.27349536153d + (11790.6290886588d * T));
        this.R0 += 2.11829E-6d * Math.cos(5.84714540314d + (1577.3435424478d * T));
        this.R0 += 1.85752E-6d * Math.cos(5.02194447178d + (10977.078804699d * T));
        this.R0 += 1.09835E-6d * Math.cos(5.05510636285d + (5486.777843175d * T));
        this.R0 += 9.8316E-7d * Math.cos(0.88681311277d + (6069.7767545534d * T));
        this.R0 += 8.6499E-7d * Math.cos(5.68959778254d + (15720.8387848784d * T));
        this.R0 += 8.5825E-7d * Math.cos(1.27083733351d + (161000.685737674d * T));
        this.R0 += 6.2916E-7d * Math.cos(0.92177108832d + (529.6909650946d * T));
        this.R0 += 5.7056E-7d * Math.cos(2.01374292014d + (83996.8473181119d * T));
        this.R0 += 6.4903E-7d * Math.cos(0.27250613787d + (17260.1546546904d * T));
        this.R0 += 4.9384E-7d * Math.cos(3.24501240359d + (2544.3144198834d * T));
        this.R0 += 5.5736E-7d * Math.cos(5.24159798933d + (71430.6956181291d * T));
        this.R0 += 4.2515E-7d * Math.cos(6.01110242003d + (6275.9623029906d * T));
        this.R0 += 4.6963E-7d * Math.cos(2.57805070386d + (775.522611324d * T));
        this.R0 += 3.8968E-7d * Math.cos(5.36071738169d + (4694.0029547076d * T));
        this.R0 += 4.4661E-7d * Math.cos(5.53715807302d + (9437.762934887d * T));
        this.R0 += 3.566E-7d * Math.cos(1.67468058995d + (12036.4607348882d * T));
        this.R0 += 3.1921E-7d * Math.cos(0.18368229781d + (5088.6288397668d * T));
        this.R0 += 3.1846E-7d * Math.cos(1.77775642085d + (398.1490034082d * T));
        this.R0 += 3.3193E-7d * Math.cos(0.24370300098d + (7084.8967811152d * T));
        this.R0 += 3.8245E-7d * Math.cos(2.39255343974d + (8827.3902698748d * T));
        this.R0 += 2.8464E-7d * Math.cos(1.21344868176d + (6286.5989683404d * T));
        this.R0 += 3.749E-7d * Math.cos(0.82952922332d + (19651.048481098d * T));
        this.R0 += 3.6957E-7d * Math.cos(4.90107591914d + (12139.5535091068d * T));
        this.R0 += 3.4537E-7d * Math.cos(1.84270693282d + (2942.4634232916d * T));
        this.R0 += 2.7793E-7d * Math.cos(1.89934330904d + (6279.5527316424d * T));
        this.R0 += 2.6275E-7d * Math.cos(4.58896850401d + (10447.3878396044d * T));
        this.R0 += 2.4596E-7d * Math.cos(3.78660875483d + (8429.2412664666d * T));
        this.R0 += 2.3587E-7d * Math.cos(0.26866117066d + (796.2980068164d * T));
        this.R0 += 2.3927E-7d * Math.cos(4.99598548138d + (5856.4776591154d * T));
        this.R0 += 2.0349E-7d * Math.cos(4.65267995431d + (2146.1654164752d * T));
        this.R0 += 2.3287E-7d * Math.cos(2.80783650928d + (14143.4952424306d * T));
        this.R0 += 2.2103E-7d * Math.cos(1.95004702988d + (3154.6870848956d * T));
        this.R0 += 1.9506E-7d * Math.cos(5.38227371393d + (2352.8661537718d * T));
        this.R0 += 1.7958E-7d * Math.cos(0.19871379385d + (6812.766815086d * T));
        this.R1 += 0.00103018608d * Math.cos(1.10748969588d + (6283.0758499914d * T));
        this.R1 += 1.721238E-5d * Math.cos(1.06442301418d + (12566.1516999828d * T));
        this.R1 += 7.02215E-6d * Math.cos(3.14159265359d + (0.0d * T));
        this.R1 += 3.2346E-7d * Math.cos(1.02169059149d + (18849.2275499742d * T));
        this.R1 += 3.0799E-7d * Math.cos(2.84353804832d + (5507.5532386674d * T));
        this.R1 += 2.4971E-7d * Math.cos(1.31906709482d + (5223.6939198022d * T));
        this.R1 += 1.8485E-7d * Math.cos(1.42429748614d + (1577.3435424478d * T));
        this.R1 += 1.0078E-7d * Math.cos(5.91378194648d + (10977.078804699d * T));
        this.R1 += 8.634E-8d * Math.cos(0.27146150602d + (5486.777843175d * T));
        this.R1 += 8.654E-8d * Math.cos(1.42046854427d + (6275.9623029906d * T));
        this.R1 += 5.069E-8d * Math.cos(1.68613426734d + (5088.6288397668d * T));
        this.R1 += 4.985E-8d * Math.cos(6.01401770704d + (6286.5989683404d * T));
        this.R1 += 4.669E-8d * Math.cos(5.98724494073d + (529.6909650946d * T));
        this.R1 += 4.395E-8d * Math.cos(0.51800238019d + (4694.0029547076d * T));
        this.R1 += 3.872E-8d * Math.cos(4.74969833437d + (2544.3144198834d * T));
        this.R1 += 3.75E-8d * Math.cos(5.07097685568d + (796.2980068164d * T));
        this.R2 += 4.359385E-5d * Math.cos(5.78455133738d + (6283.0758499914d * T));
        this.R2 += 1.23633E-6d * Math.cos(5.57934722157d + (12566.1516999828d * T));
        this.R2 += 1.2341E-7d * Math.cos(3.14159265359d + (0.0d * T));
        this.R2 += 8.792E-8d * Math.cos(3.62777733395d + (77713.7714681205d * T));
        this.R2 += 5.689E-8d * Math.cos(1.86958905084d + (5573.1428014331d * T));
        this.R2 += 3.301E-8d * Math.cos(5.47027913302d + (18849.2275499742d * T));
        this.R2 += 1.471E-8d * Math.cos(4.48028885617d + (5507.5532386674d * T));
        this.R2 += 1.013E-8d * Math.cos(2.81456417694d + (5223.6939198022d * T));
        this.R2 += 8.54E-9d * Math.cos(3.10878241236d + (1577.3435424478d * T));
        this.R2 += 1.102E-8d * Math.cos(2.84173992403d + (161000.685737674d * T));
        this.R2 += 6.48E-9d * Math.cos(5.47349498544d + (775.522611324d * T));
        this.R3 += 1.44595E-6d * Math.cos(4.27319435148d + (6283.0758499914d * T));
        this.R3 += 6.729E-8d * Math.cos(3.91697608662d + (12566.1516999828d * T));
        this.R3 += 7.74E-9d * Math.cos(0.0d + (0.0d * T));
        this.R3 += 2.47E-9d * Math.cos(3.73019298781d + (18849.2275499742d * T));
        this.R3 += 3.6E-10d * Math.cos(2.8008140905d + (6286.5989683404d * T));
        this.R4 += 3.858E-8d * Math.cos(2.56384387339d + (6283.0758499914d * T));
        this.R4 += 3.06E-9d * Math.cos(2.2676950123d + (12566.1516999828d * T));
        this.R4 += 5.3E-10d * Math.cos(3.44031471924d + (5573.1428014331d * T));
    }

    protected void computePeriodicTermsLowPrecision(double T) {
        this.L0 += 1.75347045673d * Math.cos(0.0d + (0.0d * T));
        this.L0 += 0.03341656456d * Math.cos(4.66925680417d + (6283.0758499914d * T));
        this.L0 += 3.4894275E-4d * Math.cos(4.62610241759d + (12566.1516999828d * T));
        this.L0 += 3.417571E-5d * Math.cos(2.82886579606d + (3.523118349d * T));
        this.L0 += 3.497056E-5d * Math.cos(2.74411800971d + (5753.3848848968d * T));
        this.L0 += 3.135896E-5d * Math.cos(3.62767041758d + (77713.7714681205d * T));
        this.L0 += 2.676218E-5d * Math.cos(4.41808351397d + (7860.4193924392d * T));
        this.L0 += 2.342687E-5d * Math.cos(6.13516237631d + (3930.2096962196d * T));
        this.L0 += 1.273166E-5d * Math.cos(2.03709655772d + (529.6909650946d * T));
        this.L0 += 1.324292E-5d * Math.cos(0.74246356352d + (11506.7697697936d * T));
        this.L0 += 9.01855E-6d * Math.cos(2.04505443513d + (26.2983197998d * T));
        this.L0 += 1.199167E-5d * Math.cos(1.10962944315d + (1577.3435424478d * T));
        this.L0 += 8.57223E-6d * Math.cos(3.50849156957d + (398.1490034082d * T));
        this.L0 += 7.79786E-6d * Math.cos(1.17882652114d + (5223.6939198022d * T));
        this.L1 += 6283.31966747491d * Math.cos(0.0d + (0.0d * T));
        this.L1 += 0.00206058863d * Math.cos(2.67823455584d + (6283.0758499914d * T));
        this.L1 += 4.30343E-5d * Math.cos(2.63512650414d + (12566.1516999828d * T));
        this.L1 += 4.25264E-6d * Math.cos(1.59046980729d + (3.523118349d * T));
        this.L1 += 1.08977E-6d * Math.cos(2.96618001993d + (1577.3435424478d * T));
        this.L1 += 9.3478E-7d * Math.cos(2.59212835365d + (18849.2275499742d * T));
        this.L1 += 1.19261E-6d * Math.cos(5.79557487799d + (26.2983197998d * T));
        this.L1 += 7.2122E-7d * Math.cos(1.13846158196d + (529.6909650946d * T));
        this.L2 += 5.291887E-4d * Math.cos(0.0d + (0.0d * T));
        this.L2 += 8.719837E-5d * Math.cos(1.07209665242d + (6283.0758499914d * T));
        this.L2 += 3.09125E-6d * Math.cos(0.86728818832d + (12566.1516999828d * T));
        this.L2 += 2.7339E-7d * Math.cos(0.05297871691d + (3.523118349d * T));
        this.L2 += 1.6334E-7d * Math.cos(5.18826691036d + (26.2983197998d * T));
        this.L2 += 1.5752E-7d * Math.cos(3.6845788943d + (155.4203994342d * T));
        this.L2 += 9.541E-8d * Math.cos(0.75742297675d + (18849.2275499742d * T));
        this.L2 += 8.937E-8d * Math.cos(2.05705419118d + (77713.7714681205d * T));
        this.L3 += 2.89226E-6d * Math.cos(5.84384198723d + (6283.0758499914d * T));
        this.L3 += 3.4955E-7d * Math.cos(0.0d + (0.0d * T));
        this.L3 += 1.6819E-7d * Math.cos(5.48766912348d + (12566.1516999828d * T));
        this.L3 += 2.962E-8d * Math.cos(5.19577265202d + (155.4203994342d * T));
        this.B0 += 2.7962E-6d * Math.cos(3.19870156017d + (84334.6615813083d * T));
        this.B0 += 1.01643E-6d * Math.cos(5.42248619256d + (5507.5532386674d * T));
        this.R0 += 1.00013988799d * Math.cos(0.0d + (0.0d * T));
        this.R0 += 0.01670699626d * Math.cos(3.09846350771d + (6283.0758499914d * T));
        this.R0 += 1.3956023E-4d * Math.cos(3.0552460962d + (12566.1516999828d * T));
        this.R0 += 3.08372E-5d * Math.cos(5.19846674381d + (77713.7714681205d * T));
        this.R0 += 1.628461E-5d * Math.cos(1.17387749012d + (5753.3848848968d * T));
        this.R0 += 1.575568E-5d * Math.cos(2.84685245825d + (7860.4193924392d * T));
        this.R0 += 9.24799E-6d * Math.cos(5.45292234084d + (11506.7697697936d * T));
        this.R0 += 5.42444E-6d * Math.cos(4.56409149777d + (3930.2096962196d * T));
        this.R0 += 4.7211E-6d * Math.cos(3.66100022149d + (5884.9268465832d * T));
        this.R0 += 3.2878E-6d * Math.cos(5.89983646482d + (5223.6939198022d * T));
        this.R0 += 3.45983E-6d * Math.cos(0.96368617687d + (5507.5532386674d * T));
        this.R0 += 3.06784E-6d * Math.cos(0.29867139512d + (5573.1428014331d * T));
        this.R0 += 1.74844E-6d * Math.cos(3.01193636534d + (18849.2275499742d * T));
        this.R0 += 2.43189E-6d * Math.cos(4.27349536153d + (11790.6290886588d * T));
        this.R1 += 0.00103018608d * Math.cos(1.10748969588d + (6283.0758499914d * T));
        this.R1 += 1.721238E-5d * Math.cos(1.06442301418d + (12566.1516999828d * T));
        this.R1 += 7.02215E-6d * Math.cos(3.14159265359d + (0.0d * T));
        this.R1 += 3.2346E-7d * Math.cos(1.02169059149d + (18849.2275499742d * T));
        this.R1 += 3.0799E-7d * Math.cos(2.84353804832d + (5507.5532386674d * T));
        this.R2 += 4.359385E-5d * Math.cos(5.78455133738d + (6283.0758499914d * T));
        this.R2 += 1.23633E-6d * Math.cos(5.57934722157d + (12566.1516999828d * T));
        this.R2 += 1.2341E-7d * Math.cos(3.14159265359d + (0.0d * T));
        this.R2 += 8.792E-8d * Math.cos(3.62777733395d + (77713.7714681205d * T));
        this.R3 += 1.44595E-6d * Math.cos(4.27319435148d + (6283.0758499914d * T));
    }
}
