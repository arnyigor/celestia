package com.arny.celestiatools.utils.astronomy;

import java.util.ArrayList;
import java.util.List;

public class EllipticMotion {
    public static final int EPOCH_PRECISION_INTERPOLATED = 0;
    public static final int EPOCH_PRECISION_OUTSIDE = 1;
    public static final int EPOCH_PRECISION_TABULAR = 2;
    private Coordinates3D coordSunRectangular;
    private double decl;
    private double distanceAU;
    private double distanceSunAU;
    private EllipticMotionParameters emp;
    private List<EllipticMotionParameters> empArray = new ArrayList<>();
    private int epochPrecision = EPOCH_PRECISION_OUTSIDE;
    private Coordinates3D geoEquCoord = new Coordinates3D();
    private Coordinates3D heliEquCoord;
    private double jd;
    private final double k = 0.01720209895d;
    private double px;
    private double py;
    private double pz;
    private double qx;
    private double qy;
    private double qz;
    private double ra;

    public EllipticMotion(List<EllipticMotionParameters> empArray) {
        this.empArray = empArray;
    }

    protected EllipticMotion(EllipticMotion original) {
        this.jd = original.jd;
        this.emp = original.emp.copy();
        this.empArray = new ArrayList(original.empArray);
        this.distanceAU = original.distanceAU;
        this.decl = original.decl;
        this.ra = original.ra;
        this.distanceSunAU = original.distanceSunAU;
        this.px = original.px;
        this.py = original.py;
        this.pz = original.pz;
        this.qx = original.qx;
        this.qy = original.qy;
        this.qz = original.qz;
        try {
            this.coordSunRectangular = original.coordSunRectangular.copy();
        } catch (Exception e) {
        }
        try {
            this.geoEquCoord = original.geoEquCoord.copy();
        } catch (Exception e2) {
        }
        try {
            this.heliEquCoord = original.heliEquCoord.copy();
        } catch (Exception e3) {
        }
    }

    public EllipticMotion copy() {
        return new EllipticMotion(this);
    }

    public void setSunRectangularCoordinates(Coordinates3D coordSunRectangular) {
        this.coordSunRectangular = coordSunRectangular;
    }

    public void compute(DatePosition datePosition) {
        this.jd = JulianDate.getJD(datePosition);
        interpolateEllipticMotionParameters(this.jd);
        computeTimeIndependentParameters();
        this.coordSunRectangular = new SunObject().getGeocentricRectangularEquatorialCoordinates(this.jd).copy();
        computeRectangularCoordinates(0.0d);
        computeRectangularCoordinates(0.0057755183d * this.distanceAU);
    }

    public float getOrbitalPeriodDays() {
        if (this.emp.getOrbitType() == (byte) 0) {
            return (float) (6.2831854820251465d / this.emp.getN());
        }
        return 0.0f;
    }

    public double getPerihelionDistanceAU() {
        if (this.emp.getE() != 1.0f) {
            return Math.abs(this.emp.getSMA() * (1.0f - this.emp.getE()));
        }
        return this.emp.getQ();
    }

    public double getAphelionDistanceAU() {
        if (this.emp.getOrbitType() == (byte) 0) {
            return this.emp.getSMA() * (1.0f + this.emp.getE());
        }
        return 0.0f;
    }

    private void interpolateEllipticMotionParameters(double jd) {
        if (this.empArray.size() == 3) {
            double sa;
            double e;
            double i;
            double omega;
            double omega2;
            double nn;
            double m0;
            double epoch;
            if (jd < (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getEpoch()) {
                sa = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getSMA();
                e = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getE();
                i = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getI();
                omega = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getomega();
                omega2 = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getOmega();
                nn = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getN();
                m0 = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getM0();
                epoch = (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getEpoch();
                this.epochPrecision = EPOCH_PRECISION_OUTSIDE;
            } else if (jd > (this.empArray.get(EPOCH_PRECISION_TABULAR)).getEpoch()) {
                sa = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getSMA();
                e = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getE();
                i = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getI();
                omega = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getomega();
                omega2 = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getOmega();
                nn = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getN();
                m0 = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getM0();
                epoch = (this.empArray.get(EPOCH_PRECISION_TABULAR)).getEpoch();
                this.epochPrecision = EPOCH_PRECISION_OUTSIDE;
            } else {
                double n = ((2.0d * (jd - (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getEpoch())) / ((this.empArray.get(EPOCH_PRECISION_TABULAR)).getEpoch() - (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getEpoch())) - 1.0d;
                float sa2 = (float) Interpolation.interpolate(n, (double) (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getSMA(), (double) (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getSMA(), (double) (this.empArray.get(EPOCH_PRECISION_TABULAR)).getSMA());
                float e2 = (float) Interpolation.interpolate(n, (double) (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getE(), (double) (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getE(), (double) (this.empArray.get(EPOCH_PRECISION_TABULAR)).getE());
                float i2 = (float) Interpolation.interpolate(n, (double) (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getI(), (double) (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getI(), (double) (this.empArray.get(EPOCH_PRECISION_TABULAR)).getI());
                float omega3 = (float) Interpolation.interpolate(n, (double) (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getomega(), (double) (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getomega(), (double) (this.empArray.get(EPOCH_PRECISION_TABULAR)).getomega());
                float omega22 = (float) Interpolation.interpolate(n, (double) (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getOmega(), (double) (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getOmega(), (double) (this.empArray.get(EPOCH_PRECISION_TABULAR)).getOmega());
                nn = (double) ((float) Interpolation.interpolate(n, (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getN(), (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getN(), (this.empArray.get(EPOCH_PRECISION_TABULAR)).getN()));
                m0 = (float) Interpolation.interpolate(n, (double) (this.empArray.get(EPOCH_PRECISION_INTERPOLATED)).getM0(), (double) (this.empArray.get(EPOCH_PRECISION_OUTSIDE)).getM0(), (double) (this.empArray.get(EPOCH_PRECISION_TABULAR)).getM0());
                epoch = jd;
                this.epochPrecision = EPOCH_PRECISION_INTERPOLATED;
                omega2 = omega22;
                omega = omega3;
                i = i2;
                e = e2;
                sa = sa2;
            }
            this.emp.initialize(sa, e, i, omega, omega2, nn, epoch, m0);
            return;
        }
        this.emp = this.empArray.get(EPOCH_PRECISION_INTERPOLATED);
        this.epochPrecision = EPOCH_PRECISION_OUTSIDE;
    }

    private void computeRectangularCoordinates(double lightTimeCorrection) {
        Coordinates3D coordRect;
        if (((double) this.emp.getE()) < 0.96d || this.emp.getOrbitType() == (byte) 0) {
            double E = solveEquationOfKepler(((double) this.emp.getM0()) + (((this.jd - this.emp.getEpoch()) - lightTimeCorrection) * this.emp.getN()), (double) this.emp.getE());
            double xv = ((double) this.emp.getSMA()) * (Math.cos(E) - ((double) this.emp.getE()));
            double yv = ((double) this.emp.getSMA()) * (Math.sqrt(1.0d - ((double) (this.emp.getE() * this.emp.getE()))) * Math.sin(E));
            coordRect = computeRectangularCoordinates(Math.atan2(yv, xv), Math.sqrt((xv * xv) + (yv * yv)));
        } else {
            coordRect = computeNearParabolicMotion();
        }
        double chi = this.coordSunRectangular.getX() + coordRect.getX();
        double eta = this.coordSunRectangular.getY() + coordRect.getY();
        double zeta = this.coordSunRectangular.getZ() + coordRect.getZ();
        this.ra = Math.atan2(eta, chi);
        if (this.ra < 0.0d) {
            this.ra += Ephemeris.PI2;
        }
        this.distanceAU = Math.sqrt(((chi * chi) + (eta * eta)) + (zeta * zeta));
        this.decl = Math.asin(zeta / this.distanceAU);
        this.distanceSunAU = Math.sqrt(((coordRect.getX() * coordRect.getX()) + (coordRect.getY() * coordRect.getY())) + (coordRect.getZ() * coordRect.getZ()));
        this.geoEquCoord.setRADecDistance((double) ((float) this.ra), (double) ((float) this.decl), (double) ((float) this.distanceAU));
        this.heliEquCoord = SphericalMath.getCarthToSphericalF(coordRect.getX(), coordRect.getY(), coordRect.getZ());
    }

    private Coordinates3D computeNearParabolicMotion() {
        double G = (double) ((1.0f - this.emp.getE()) / (1.0f + this.emp.getE()));
        double T = this.jd - this.emp.getEpoch();
        double Q2 = ((0.01720209895d / (2.0d * (this.emp.getQ()))) * Math.sqrt((1.0d + ((double) this.emp.getE())) / ((double) this.emp.getQ()))) * T;
        double S = 2.0d / Math.tan(2.0d * Math.atan(Math.pow(Math.tan(Math.atan(2.0d / (3.0d * Math.abs(Q2))) / 2.0d), 0.3333333333333333d)));
        if (T < 0.0d) {
            S = -S;
        }
        if (this.emp.getE() != 1.0f) {
            int L = EPOCH_PRECISION_INTERPOLATED;
            boolean stop = false;
            double S0;
            do {
                S0 = S;
                int Z = EPOCH_PRECISION_OUTSIDE;
                double Y = S * S;
                double G1 = (-Y) * S;
                double Q3 = Q2 + ((((2.0d * G) * S) * Y) / 3.0d);
                double F;
                do {
                    Z += EPOCH_PRECISION_OUTSIDE;
                    G1 = ((-G1) * G) * Y;
                    F = ((((double) Z) - (((double) (Z + EPOCH_PRECISION_OUTSIDE)) * G)) / ((2.0d * ((double) Z)) + 1.0d)) * G1;
                    Q3 += F;
                    if (Z > 50 || Math.abs(F) > 10000.0d) {
                        stop = true;
                    }
                    if (stop) {
                        break;
                    }
                } while (Math.abs(F) > 1.0E-12d);
                L += EPOCH_PRECISION_OUTSIDE;
                if (L > 50) {
                    stop = true;
                } else {
                    double S1;
                    do {
                        S1 = S;
                        S = (((((2.0d * S) * S) * S) / 3.0d) + Q3) / ((S * S) + 1.0d);
                    } while (Math.abs(S - S1) > 1.0E-12d);
                }
                if (stop) {
                    break;
                }
            } while (Math.abs(S - S0) > 1.0E-12d);
        }
        double nu = Math.atan(S) * 2.0d;
        return computeRectangularCoordinates(nu, ((double) (this.emp.getQ() * (1.0f + this.emp.getE()))) / (1.0d + (((double) this.emp.getE()) * Math.cos(nu))));
    }

    private Coordinates3D computeRectangularCoordinates(double nu, double r) {
        return new Coordinates3D(r * ((this.px * Math.cos(nu)) + (this.qx * Math.sin(nu))), r * ((this.py * Math.cos(nu)) + (this.qy * Math.sin(nu))), r * ((this.pz * Math.cos(nu)) + (this.qz * Math.sin(nu))));
    }

    private void computeTimeIndependentParameters() {
        double epsilon = Ecliptic.getObliquity(this.jd);
        double sinepsilon = Math.sin(epsilon);
        double cosepsilon = Math.cos(epsilon);
        double sinomega = Math.sin((double) this.emp.getomega());
        double cosomega = Math.cos((double) this.emp.getomega());
        double sinOmega = Math.sin((double) this.emp.getOmega());
        double cosOmega = Math.cos((double) this.emp.getOmega());
        double cosi = Math.cos((double) this.emp.getI());
        double sini = Math.sin((double) this.emp.getI());
        this.px = (cosomega * cosOmega) - ((sinomega * sinOmega) * cosi);
        this.py = (((cosomega * sinOmega) + ((sinomega * cosOmega) * cosi)) * cosepsilon) - ((sinepsilon * sinomega) * sini);
        this.pz = (((cosomega * sinOmega) + ((sinomega * cosOmega) * cosi)) * sinepsilon) + ((cosepsilon * sinomega) * sini);
        this.qx = ((-sinomega) * cosOmega) - ((cosomega * sinOmega) * cosi);
        this.qy = ((((cosomega * cosOmega) * cosi) - (sinomega * sinOmega)) * cosepsilon) - ((sinepsilon * cosomega) * sini);
        this.qz = ((((cosomega * cosOmega) * cosi) - (sinomega * sinOmega)) * sinepsilon) + ((cosepsilon * cosomega) * sini);
    }

    public double solveEquationOfKepler(double M, double e) {
        double E = M;
        int i = EPOCH_PRECISION_INTERPOLATED;
        double E0;
        do {
            E0 = E;
            double fraction = (((StrictMath.sin(E0) * e) + M) - E0) / (1.0d - (StrictMath.cos(E0) * e));
            if (Math.abs(fraction) > 0.5d) {
                fraction = 0.5d * Math.signum(fraction);
            }
            E = E0 + fraction;
            i += EPOCH_PRECISION_OUTSIDE;
            if (i >= 10) {
                break;
            }
        } while (Math.abs(E - E0) > 1.0E-8d);
        return E;
    }

    public Coordinates3D getGeocentricEquatorialCoordinates() {
        return this.geoEquCoord;
    }

    public Coordinates3D getHeliocentricEquatorialCoordinates() {
        return this.heliEquCoord;
    }

    public double getDistanceAU() {
        return this.distanceAU;
    }

    public double getDistanceSunAU() {
        return this.distanceSunAU;
    }

    public double getEpoch() {
        return this.emp.getEpoch();
    }

    public double getMeanAnomaly() {
        return this.emp.getM0();
    }

    public double getArgumentPerihelion() {
        return this.emp.getomega();
    }

    public double getAscendingNode() {
        return this.emp.getOmega();
    }

    public double getOrbitalInclination() {
        return this.emp.getI();
    }

    public double getEccentricity() {
        return this.emp.getE();
    }

    public double getSemimajorAxisAU() {
        return this.emp.getSMA();
    }

    public byte getOrbitType() {
        return this.emp.getOrbitType();
    }

    public double getQ() {
        return this.emp.getQ();
    }

    public int getEpochPrecision() {
        return this.epochPrecision;
    }
}
