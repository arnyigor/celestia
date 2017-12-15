package com.arny.celestiatools.utils.astronomy;


public class MoonPhysicalEphemeris {
    public static final float INCL = 0.026920307f;
    private double A;
    private double F;
    private double Omega;
    private double W;
    private double b;
    private double beta;
    private double bs;
    private double bss;
    private DatePosition datePosition;
    private double l;
    private double lambda;
    private double ls;
    private double lss;
    private MoonObject moonObject = new MoonObject();
    private double opticalLibrationLat;
    private double opticalLibrationLon;
    private double ro = 0.0d;
    private double sigma = 0.0d;

    public void setDatePosition(DatePosition datePosition) {
        if (this.datePosition == null || !this.datePosition.equals(datePosition)) {
            this.datePosition = datePosition.copy();
            Coordinates3D topoEquGeo = this.moonObject.getTopocentricEquatorialCoordinates(datePosition);
            this.lambda = this.moonObject.getGeocentricEclipticalCoordinates().getLongitude();
            this.beta = this.moonObject.getGeocentricEclipticalCoordinates().getLatitude();
            this.Omega = this.moonObject.getLongitudeAscendingNode();
            this.F = this.moonObject.getArgumentLatitude();
            this.W = this.lambda - this.Omega;
            this.A = Math.atan2(((Math.sin(this.W) * Math.cos(this.beta)) * Math.cos(0.02692030742764473d)) - (Math.sin(this.beta) * Math.sin(0.02692030742764473d)), Math.cos(this.W) * Math.cos(this.beta));
            this.ls = (float) (this.A - this.F);
            this.bs = (float) Math.asin((((-Math.sin(this.W)) * Math.cos(this.beta)) * Math.sin(0.02692030742764473d)) - (Math.sin(this.beta) * Math.cos(0.02692030742764473d)));
            computePhysicalLibrations();
            this.l =  ((this.ls + this.lss) % Ephemeris.PI2);
            if (this.l > Ephemeris.PIf) {
                this.l -= Ephemeris.PI2;
            }
            if (this.l < -3.1415927f) {
                this.l += Ephemeris.PI2;
            }
            this.b = this.bs + this.bss;
        }
    }

    private void computePhysicalLibrations() {
        this.bss = 0.0f;
        this.lss = 0.0f;
        double D = this.moonObject.getMeanElongation();
        double M = this.moonObject.getSunMeanAnomaly();
        double Ms = this.moonObject.getMoonMeanAnomaly();
        double E = this.moonObject.getE();
        double K1 = Math.toRadians(119.75d + (131.849d * this.moonObject.T));
        double K2 = Math.toRadians(72.56d + (20.186d * this.moonObject.T));
        this.ro = (((((((((-0.02752d * Math.cos(Ms)) - (0.02245d * Math.sin(this.F))) + (0.00684d * Math.cos(Ms - (2.0d * this.F)))) - (0.00293d * Math.cos(2.0d * this.F))) - (8.5E-4d * Math.cos((2.0d * this.F) - (2.0d * D)))) - (5.4E-4d * Math.cos(Ms - (2.0d * D)))) - (2.0E-4d * Math.sin(this.F + Ms))) - (2.0E-4d * Math.cos((2.0d * this.F) + Ms))) - (2.0E-4d * Math.cos(Ms - this.F))) + (1.4E-4d * Math.cos(((2.0d * this.F) + Ms) - (2.0d * D)));
        this.sigma = ((((((((((((-0.02816d * Math.sin(Ms)) + (0.02244d * Math.cos(this.F))) - (0.00682d * Math.sin(Ms - (2.0d * this.F)))) - (0.00279d * Math.sin(2.0d * this.F))) - (8.3E-4d * Math.sin((2.0d * this.F) - (2.0d * D)))) + (6.9E-4d * Math.sin(Ms - (2.0d * D)))) + (4.0E-4d * Math.cos(this.F + Ms))) - (2.5E-4d * Math.sin(2.0d * Ms))) - (2.3E-4d * Math.sin((2.0d * this.F) + Ms))) + (2.0E-4d * Math.cos(Ms - this.F))) + (1.9E-4d * Math.sin(Ms - this.F))) + (1.3E-4d * Math.sin(((2.0d * this.F) + Ms) - (2.0d * D)))) - (1.0E-4d * Math.cos(Ms - (3.0d * this.F)));
        double tau = (((((((((((((((((((0.0252d * E) * Math.sin(M)) + (0.00473d * Math.sin((2.0d * Ms) - (2.0d * this.F)))) - (0.00467d * Math.sin(Ms))) + (0.00396d * Math.sin(K1))) + (0.00276d * Math.sin((2.0d * Ms) - (2.0d * D)))) + (0.00196d * Math.sin(this.Omega))) - (0.00183d * Math.cos(Ms - this.F))) + (0.00115d * Math.sin(Ms - (2.0d * D)))) - (9.6E-4d * Math.sin(Ms - D))) + (4.6E-4d * Math.sin((2.0d * this.F) - (2.0d * D)))) - (3.9E-4d * Math.sin(Ms - this.F))) - (3.2E-4d * Math.sin((Ms - M) - D))) + (2.7E-4d * Math.sin(((2.0d * Ms) - M) - (2.0d * D)))) + (2.3E-4d * Math.sin(K2))) - (1.4E-4d * Math.sin(2.0d))) + (1.4E-4d * Math.cos((2.0d * Ms) - (2.0d * this.F)))) - (1.2E-4d * Math.sin(Ms - (2.0d * this.F)))) - (1.2E-4d * Math.sin(2.0d * Ms))) + (1.1E-4d * Math.sin(((2.0d * Ms) - (2.0d * M)) - (2.0d * D)));
        this.ro = Math.toRadians(this.ro);
        this.sigma = Math.toRadians(this.sigma);
        this.lss = (float) ((-Math.toRadians(tau)) + (((this.ro * Math.cos(this.A)) + (this.sigma * Math.sin(this.A))) * Math.tan((double) this.bs)));
        this.bss = (float) ((this.sigma * Math.cos(this.A)) - (this.ro * Math.sin(this.A)));
    }

    public double getPositionAngle(double geocentricRA) {
        double V = this.Omega + (this.sigma / Math.sin(0.02692030742764473d));
        double X = Math.sin(0.02692030742764473d + this.ro) * Math.sin(V);
        double obliquity = Ecliptic.getObliquity(this.datePosition.getJD());
        double Y = ((Math.sin(0.02692030742764473d + this.ro) * Math.cos(V)) * Math.cos(obliquity)) - (Math.cos(0.02692030742764473d + this.ro) * Math.sin(obliquity));
        return (Math.sqrt((X * X) + (Y * Y)) * Math.cos(geocentricRA - Math.atan2(X, Y))) / Math.cos((double) this.b);
    }

    public double getPhysicalLibrationLat() {
        return this.bss;
    }

    public double getPhysicalLibrationLon() {
        return this.lss;
    }

    public double getLibrationLat() {
        return this.b;
    }

    public double getLibrationLon() {
        return this.l;
    }

    public double getLibration() {
        return Math.sqrt((this.b * this.b) + (this.l * this.l));
    }
}
