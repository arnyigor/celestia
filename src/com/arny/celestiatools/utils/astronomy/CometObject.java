package com.arny.celestiatools.utils.astronomy;


public class CometObject extends SolarSystemObject {
    protected Comet comet;
    double h0 = Math.toRadians(-0.5667d);

    public CometObject copy() {
        return new CometObject(this);
    }

    protected CometObject(CometObject original) {
        super((SolarSystemObject) original);
        try {
            this.comet = original.comet.copy();
        } catch (Exception e) {
        }
    }

    public String toString() {
        return CelestialObject.COMET;
    }

    public CometObject(Comet comet) {
        super(comet.getObjectId(), 0.1d, 5.0d);
        this.comet = comet;
        this.topoEquCoord = new Coordinates3D();
        this.heliEclCoord = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
    }

    public BasisCelestialObject getBasisObject() {
        return this.comet;
    }

    public double getGeocentricDiameterArcsec(double distanceAU) {
        return -1.0d;
    }

    public double getDistanceAU() {
        return (double) this.comet.getDistanceAU();
    }

    public String getDesignations() {
        return this.comet.getDesignations();
    }

    public int getID() {
        return SolarSystemObject.COMET_ID;
    }

    public float getRadius_km() {
        return 500.0f;
    }

    public int getTopViewImageScaleWidth(int radius) {
        double solarDistance = getHeliocentricCoordinatesTopDownView().getRadius();
        if (solarDistance < 0.4d) {
            solarDistance = 0.4d;
        } else if (solarDistance > 5.0d) {
            solarDistance = 5.0d;
        }
        return (int) (((double) radius) / solarDistance);
    }

    public int getOrbitResourceID() {
        return -1;
    }

    public double getMeanDiameterArcsec() {
        return 0.0d;
    }

    @Override
    public float getRadiusTopViewPixel() {
        return 0;
    }

    public double geth0() {
        return this.h0;
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

    public float getVmag() {
        return (float) ((5.0d * Math.log10(this.geoEclCoord.getRadius())) + (this.comet.getMagAbs() + (this.comet.getMagSlope() * Math.log(getHeliocentricCoordinatesTopDownView().getRadius()))));
    }

    public String getColor() {
        return "A0";
    }

    public Coordinates3D getRotationalAxis() {
        double T = (this.jd - 2451545.0d) / 36525.0d;
        return new Coordinates3D((281.02d - (0.033d * T)) + (0.276d * T), (61.45d - (0.005d * T)) + (0.107d * T), 0.0d).toRadians();
    }

    public double getZeroMeridian() {
        return 0.0d;
    }

    public double getCentralMeridian() {
        return 0.0d;
    }

    public double getDiameter1AUarcsec() {
        return 6.68d;
    }

    @Override
    public int getImageTopViewResourceID() {
        return 0;
    }

//    public TableView getOrbitalDataTable(Context context) {
//        TableView tableView = new TableView(context, null);
//        tableView.setVerticalScroll(false);
//        SpannableStringCollection row = new SpannableStringCollection(context, true);
//        row.add(PhysicalDataTableField.PerihelionDate);
//        row.add(PhysicalDataTableField.SemiMajorAxis);
//        row.add(PhysicalDataTableField.Perihelion);
//        row.add(PhysicalDataTableField.Aphelion);
//        row.add(PhysicalDataTableField.OrbitalPeriod);
//        row.add(PhysicalDataTableField.OrbitalInclination);
//        row.add(PhysicalDataTableField.OrbitalExcentricity);
//        row.add(PhysicalDataTableField.MeanAnomaly);
//        row.add(PhysicalDataTableField.ArgumentPerihelion);
//        row.add(PhysicalDataTableField.AscendingNode);
//        row.add(PhysicalDataTableField.AbsoluteMagnitude);
//        row.add(PhysicalDataTableField.SlopeParameter);
//        row.add(PhysicalDataTableField.Epoch);
//        SpannableStringCollection columns = new SpannableStringCollection(context);
//        columns.add(context.getString(R.string.EmptyString));
//        tableView.setCellGravity(3);
//        tableView.setVerticalFieldPadding(1);
//        tableView.setAutoColor(true);
//        tableView.setLayout(row, columns, null, R.style.TextViewTableRowHeader, R.style.TextViewTableCell, null, null);
//        EllipticMotion em = this.comet.getEllipticMotion();
//        if (em == null) {
//            return null;
//        }
//        float h = (float) this.comet.getMagAbs();
//        float g = (float) this.comet.getMagSlope();
//        double epoch = em.getEpoch();
//        double meanAnomaly = ((double) em.getMeanAnomaly()) * Ephemeris.TODEG;
//        double argumentPerihelion = ((double) em.getArgumentPerihelion()) * Ephemeris.TODEG;
//        double ascendingNode = ((double) em.getAscendingNode()) * Ephemeris.TODEG;
//        double inclination = ((double) em.getOrbitalInclination()) * Ephemeris.TODEG;
//        float eccentricity = em.getEccentricity();
//        double semimajorAxis = (double) em.getSemimajorAxisAU();
//        int epochPrecision = em.getEpochPrecision();
//        Object dpPerihelion = JulianDate.getDate(em.getEpoch(), this.datePosition);
//        if (epochPrecision == 0) {
//            tableView.setHeader((int) R.string.EpochInterpolated);
//        } else if (epochPrecision == 1) {
//            tableView.setHeader((int) R.string.EpochOutsideRange);
//        } else {
//            tableView.setHeader((int) R.string.EpochTabulated);
//        }
//        tableView.setField(PhysicalDataTableField.PerihelionDate, dpPerihelion);
//        tableView.setField(PhysicalDataTableField.SemiMajorAxis, Ephemeris.AU * semimajorAxis);
//        tableView.setField(PhysicalDataTableField.Perihelion, ((double) em.getQ()) * Ephemeris.AU);
//        tableView.setField(PhysicalDataTableField.Aphelion, (em.getAphelionDistanceAU()) * Ephemeris.AU);
//        tableView.setField(PhysicalDataTableField.OrbitalPeriod, (double) em.getOrbitalPeriodDays());
//        tableView.setField(PhysicalDataTableField.OrbitalInclination, inclination);
//        tableView.setField(PhysicalDataTableField.OrbitalExcentricity, (double) eccentricity);
//        tableView.setField(PhysicalDataTableField.MeanAnomaly, meanAnomaly);
//        tableView.setField(PhysicalDataTableField.ArgumentPerihelion, argumentPerihelion);
//        tableView.setField(PhysicalDataTableField.AscendingNode, ascendingNode);
//        tableView.setField(PhysicalDataTableField.AbsoluteMagnitude, (double) h);
//        tableView.setField(PhysicalDataTableField.SlopeParameter, (double) g);
//        tableView.setField(PhysicalDataTableField.Epoch, epoch);
//        return tableView;
//    }

    public void computeElements(DatePosition datePosition) {
        super.computeElements(datePosition);
        this.comet.computePosition(datePosition);
        this.topoEquCoord.setRADecDistance(this.comet.getRA(), this.comet.getDec(), (double) this.comet.getDistanceAU());
        this.geoEquCoord = this.topoEquCoord;
        float obliquity = (float) Ecliptic.getObliquity(this.jd);
        this.geoEclCoord = Ephemeris.getEclipticalFromEquatorial(this.geoEquCoord, (double) obliquity);
        this.heliEclCoord = Ephemeris.getEclipticalFromEquatorial(this.comet.getHeliocentricEquatorialCoordinates(), (double) obliquity);
    }

    public int getInformationActivity() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    public double getm0() {
        return 0.0d;
    }

}
