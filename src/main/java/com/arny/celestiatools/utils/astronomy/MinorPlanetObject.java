package com.arny.celestiatools.utils.astronomy;


public class MinorPlanetObject extends SolarSystemObject {
    public static final int MINOR_PLANET_ID = 300;
    double h0 = Math.toRadians(-0.5667d);
    protected MinorPlanet minorPlanet;

    public MinorPlanetObject copy() {
        return new MinorPlanetObject(this);
    }

    protected MinorPlanetObject(MinorPlanetObject original) {
        super((SolarSystemObject) original);
        try {
            this.minorPlanet = original.minorPlanet.copy();
        } catch (Exception e) {
        }
    }

    public String toString() {
        return CelestialObject.MINOR_PLANET;
    }

    public MinorPlanetObject(MinorPlanet minorPlanet) {
        super(minorPlanet.getObjectId());
        this.minorPlanet = minorPlanet;
        this.topoEquCoord = new Coordinates3D();
        this.heliEclCoord = new Coordinates3D();
        this.geoEquCoord = new Coordinates3D();
        this.geoEclCoord = new Coordinates3D();
    }

    public BasisCelestialObject getBasisObject() {
        return this.minorPlanet;
    }

    public double getGeocentricDiameterArcsec(double distanceAU) {
        return -1.0d;
    }

    public double getDistanceAU() {
        return (double) this.minorPlanet.getDistanceAU();
    }

    public int getID() {
        return MINOR_PLANET_ID;
    }

    public float getRadius_km() {
        return 500.0f;
    }

    public float getRadiusTopViewPixel() {
        return 7.0f;
    }

    public int getOrbitResourceID() {
        return -1;
    }

    public double getMeanDiameterArcsec() {
        return 0.0d;
    }

    public double geth0() {
        return this.h0;
    }

    public double getm0() {
        double tanb2d = Math.tan(getPhaseAngle() / 2.0d);
        return this.minorPlanet.getMagAbs() - (2.5d * Math.log10(((1.0d - this.minorPlanet.getMagSlope()) * Math.exp(-1.33d * Math.pow(tanb2d, 0.63d))) + (this.minorPlanet.getMagSlope() * Math.exp(-1.87d * Math.pow(tanb2d, 1.22d)))));
    }

    public String getColor() {
        return "A0";
    }

//    public TableView getOrbitalDataTable(Context context) {
//        TableView tableView = new TableView(context, null);
//        tableView.setVerticalScroll(false);
//        SpannableStringCollection row = new SpannableStringCollection(context, true);
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
//        EllipticMotion em = this.minorPlanet.getEllipticMotion();
//        if (em == null) {
//            return null;
//        }
//        float h = (float) this.minorPlanet.getMagAbs();
//        float g = (float) this.minorPlanet.getMagSlope();
//        double epoch = em.getEpoch();
//        double meanAnomaly = ((double) em.getMeanAnomaly()) * Ephemeris.TODEG;
//        double argumentPerihelion = ((double) em.getArgumentPerihelion()) * Ephemeris.TODEG;
//        double ascendingNode = ((double) em.getAscendingNode()) * Ephemeris.TODEG;
//        double inclination = ((double) em.getOrbitalInclination()) * Ephemeris.TODEG;
//        double eccentricity = em.getEccentricity();
//        double semimajorAxis = (double) em.getSemimajorAxisAU();
//        int epochPrecision = em.getEpochPrecision();
//        if (epochPrecision == 0) {
//            tableView.setHeader((int) R.string.EpochInterpolated);
//        } else if (epochPrecision == 1) {
//            tableView.setHeader((int) R.string.EpochOutsideRange);
//        } else {
//            tableView.setHeader((int) R.string.EpochTabulated);
//        }
//        tableView.setField(PhysicalDataTableField.SemiMajorAxis, Ephemeris.AU * semimajorAxis);
//        tableView.setField(PhysicalDataTableField.Perihelion, ((double) em.getQ()) * Ephemeris.AU);
//        tableView.setField(PhysicalDataTableField.Aphelion, ((double) em.getAphelionDistanceAU()) * Ephemeris.AU);
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

    public void computeElements(DatePosition datePosition) {
        super.computeElements(datePosition);
        this.minorPlanet.computePosition(datePosition);
        this.geoEquCoord.setRADecDistance(this.minorPlanet.getRA(), this.minorPlanet.getDec(), (double) this.minorPlanet.getDistanceAU());
        Ephemeris.computeTopocentricCoord(datePosition, this.geoEquCoord, this.topoEquCoord);
        float obliquity = (float) Ecliptic.getObliquity(this.jd);
        this.geoEclCoord = Ephemeris.getEclipticalFromEquatorial(this.geoEquCoord, (double) obliquity);
        this.heliEclCoord = Ephemeris.getEclipticalFromEquatorial(this.minorPlanet.getHeliocentricEquatorialCoordinates(), (double) obliquity);
    }
//
//    public TableView getShortInformationTable(Context context, DatePosition datePosition) {
//        TableView tv = new TableView(context, null);
//        SpannableStringCollection row = new SpannableStringCollection(context, true);
//        row.add(PhysicalDataTableField.Constellation);
//        row.add(PhysicalDataTableField.RAGeo);
//        row.add(PhysicalDataTableField.DeclinationGeo);
//        row.add(PhysicalDataTableField.Azimuth);
//        row.add(PhysicalDataTableField.Altitude);
//        row.add(PhysicalDataTableField.Rise);
//        row.add(PhysicalDataTableField.Transit);
//        row.add(PhysicalDataTableField.Set);
//        row.add(PhysicalDataTableField.Magnitude);
//        row.add(PhysicalDataTableField.Distance);
//        SpannableStringCollection columns = new SpannableStringCollection(context);
//        columns.add(context.getString(R.string.EmptyString));
//        tv.setVerticalFieldPadding(1);
//        tv.setVerticalScroll(false);
//        tv.setStretchAllColumns(true);
//        tv.setBoldHeaders(false);
//        tv.setAutoColor(true);
//        tv.setLayout(row, columns, R.style.TextViewTableRowHeader, R.style.TextViewTableCell, null, null);
//        Coordinates3D coordGeo = getGeocentricEquatorialCoordinates(datePosition);
//        Coordinates3D coordTopo = getTopocentricEquatorialCoordinates();
//        Coordinates3D coordAzAlt = new Coordinates3D();
//        Ephemeris.getAzAltFromRADec(datePosition, coordTopo, coordAzAlt);
//        Object constellation = StarsDataBaseManager.getConstellationName(context, this);
//        double ra = Math.toDegrees(coordGeo.getRA()) / 15.0d;
//        double dec = Math.toDegrees(coordGeo.getDec());
//        double azimuth = Math.toDegrees(coordAzAlt.getAzimuth());
//        double altitude = Math.toDegrees(coordAzAlt.getAltitude());
//        double mag = (double) getVmag();
//        double distance = getDistanceAU();
//        RiseSetEvent jdRise = getRise(datePosition);
//        RiseSetEvent jdTransit = getTransit(datePosition);
//        RiseSetEvent jdSet = getSet(datePosition);
//        tv.setField(PhysicalDataTableField.Constellation, constellation);
//        tv.setField(PhysicalDataTableField.RAGeo, ra, true);
//        tv.setField(PhysicalDataTableField.DeclinationGeo, dec);
//        tv.setField(PhysicalDataTableField.Azimuth, azimuth);
//        tv.setField(PhysicalDataTableField.Altitude, altitude);
//        tv.setField(PhysicalDataTableField.Magnitude, mag);
//        tv.setField(PhysicalDataTableField.Distance, distance);
//        int i = 6 + 1;
//        tv.field[6][0].setTextRiseSetEvent(jdRise, datePosition.is24Hour(), false);
//        int i2 = i + 1;
//        tv.field[i][0].setTextTransitEvent(jdTransit, datePosition.is24Hour(), false);
//        i = i2 + 1;
//        tv.field[i2][0].setTextRiseSetEvent(jdSet, datePosition.is24Hour(), false);
//        return tv;
//    }

    public int getInformationActivity() {
        return 4;
    }

    @Override
    public String getName() {
        return null;
    }


}
