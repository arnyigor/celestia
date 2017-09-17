package com.arny.celestiatools.utils.astronomy;


import com.arny.celestiatools.utils.DateTimeUtils;

import java.util.Calendar;

public class MoonPhase {
    private double A1;
    private double E;
    private double F;
    private double F1;
    private double M;
    private double Mm;
    private DatePosition datePosition;
    private double jdMeanPhase;
    private double omega;
    private double phase;

    public DatePosition getDateOfNextPhase(DatePosition myDatePosition, double phase, boolean computeCalendarDay) {
        double decimalYear;
        this.phase = phase;
        this.datePosition = myDatePosition.copy();
        if (computeCalendarDay) {
            this.datePosition.add(Calendar.DAY_OF_MONTH, -1);
        }
        if (phase == 0.0d) {
            phase = 1.0d;
        }
        long dateTime = this.datePosition.getDateTime();
        double numDaysYear = DateTimeUtils.getDayofYear(dateTime);
        if (computeCalendarDay) {
//            decimalYear = ((double) this.datePosition.get(DateTimeFieldType.year())) + (((double) (this.datePosition.get(DateTimeFieldType.dayOfYear()) - 1)) / numDaysYear);
        } else {
//            decimalYear = ((double) this.datePosition.get(DateTimeFieldType.year())) + (((((double) (this.datePosition.get(DateTimeFieldType.dayOfYear()) - 1)) + (((double) this.datePosition.get(DateTimeFieldType.hourOfDay())) / 24.0d)) + (((double) this.datePosition.get(DateTimeFieldType.minuteOfHour())) / 1440.0d)) / numDaysYear);
        }
        double decimalYear1 = 365.25;
        double k = (decimalYear1 - 2000.0d) * 12.3685d;
        double fracK = Math.abs((10000.0d + k) - ((double) ((int) (10000.0d + k))));
        if (fracK - 0.15d <= phase) {
            k = ((double) ((int) k)) + phase;
        } else {
            k = (((double) ((int) k)) + phase) + 1.0d;
        }
        if (phase == 1.0d && fracK - 0.15d < 0.0d) {
            k -= 1.0d;
        }
        if (k < 0.0d) {
            k -= 1.0d;
        }
        double T = k / 1236.85d;
        double T2 = T * T;
        double T3 = T2 * T;
        double T4 = T3 * T;
        this.jdMeanPhase = (((2451550.09766d + (29.530588861d * k)) + (1.5437E-4d * T2)) - (1.5E-7d * T3)) + (7.3E-10d * T4);
        this.E = (1.0d - (0.002516d * T)) - (7.4E-6d * T2);
        this.M = Math.toRadians((((2.5534d + (29.1053567d * k)) - (1.4E-6d * T2)) - (1.1E-7d * T3)) % 360.0d);
        this.Mm = Math.toRadians(((((201.5643d + (385.81693528d * k)) + (0.0107582d * T2)) + (1.238E-5d * T3)) - (5.8E-8d * T4)) % 360.0d);
        this.F = Math.toRadians(((((160.7108d + (390.67050284d * k)) - (0.0016118d * T2)) - (2.27E-6d * T3)) + (1.1E-8d * T4)) % 360.0d);
        this.omega = Math.toRadians((((124.7746d - (1.56375588d * k)) + (0.0020672d * T2)) + (2.15E-6d * T3)) % 360.0d);
        this.F1 = this.F - (Math.toRadians(0.02665d) * Math.sin(this.omega));
        this.A1 = Math.toRadians(((299.77d + (0.107408d * k)) - (0.009173d * T2)) % 360.0d);
        double A2 = Math.toRadians((251.88d + (0.016321d * k)) % 360.0d);
        double A3 = Math.toRadians((251.83d + (26.6518863d * k)) % 360.0d);
        double A4 = Math.toRadians((349.42d + (36.412478d * k)) % 360.0d);
        double A5 = Math.toRadians((84.66d + (18.20629d * k)) % 360.0d);
        double A6 = Math.toRadians((141.74d + (53.303771d * k)) % 360.0d);
        double A7 = Math.toRadians((207.14d + (2.453732d * k)) % 360.0d);
        double A8 = Math.toRadians((154.84d + (7.30686d * k)) % 360.0d);
        double A9 = Math.toRadians((34.52d + (27.261239d * k)) % 360.0d);
        double A10 = Math.toRadians((207.19d + (0.121824d * k)) % 360.0d);
        double A11 = Math.toRadians((291.34d + (1.844379d * k)) % 360.0d);
        double A12 = Math.toRadians((161.72d + (24.198154d * k)) % 360.0d);
        double A13 = Math.toRadians((239.56d + (25.513099d * k)) % 360.0d);
        double A14 = Math.toRadians((331.55d + (3.592518d * k)) % 360.0d);
        double dJD = 0.0d;
        fracK = Math.abs(k - ((double) ((int) k)));
        if (fracK == 0.0d) {
            dJD = ((((((((((((((((((((((((-0.4072d * Math.sin(this.Mm)) + ((0.17241d * this.E) * Math.sin(this.M))) + (0.01608d * Math.sin(2.0d * this.Mm))) + (0.01039d * Math.sin(2.0d * this.F))) + ((0.00739d * this.E) * Math.sin(this.Mm - this.M))) - ((0.00514d * this.E) * Math.sin(this.Mm + this.M))) + (((0.00208d * this.E) * this.E) * Math.sin(2.0d * this.M))) - (0.00111d * Math.sin(this.Mm - (2.0d * this.F)))) - (5.7E-4d * Math.sin(this.Mm + (2.0d * this.F)))) + ((5.6E-4d * this.E) * Math.sin((2.0d * this.Mm) + this.M))) - (4.2E-4d * Math.sin(3.0d * this.Mm))) + ((4.2E-4d * this.E) * Math.sin(this.M + (2.0d * this.F)))) + ((3.8E-4d * this.E) * Math.sin(this.M - (2.0d * this.F)))) - (2.4E-4d * Math.sin((2.0d * this.Mm) - this.M))) - (1.7E-4d * Math.sin(this.omega))) - (7.0E-5d * Math.sin(this.Mm + (2.0d * this.M)))) + (4.0E-5d * Math.sin((2.0d * this.Mm) - (2.0d * this.F)))) + (4.0E-5d * Math.sin(3.0d * this.M))) + (3.0E-5d * Math.sin((this.Mm + this.M) - (2.0d * this.F)))) + (3.0E-5d * Math.sin((2.0d * this.Mm) + (2.0d * this.F)))) - (3.0E-5d * Math.sin((this.Mm + this.M) + (2.0d * this.F)))) + (3.0E-5d * Math.sin((this.Mm - this.M) + (2.0d * this.F)))) - (2.0E-5d * Math.sin((this.Mm - this.M) - (2.0d * this.F)))) - (2.0E-5d * Math.sin((3.0d * this.Mm) + this.M))) + (2.0E-5d * Math.sin(4.0d * this.Mm));
        } else if (fracK == 0.5d) {
            dJD = ((((((((((((((((((((((((-0.40614d * Math.sin(this.Mm)) + ((0.17302d * this.E) * Math.sin(this.M))) + (0.01614d * Math.sin(2.0d * this.Mm))) + (0.01043d * Math.sin(2.0d * this.F))) + ((0.00734d * this.E) * Math.sin(this.Mm - this.M))) - ((0.00515d * this.E) * Math.sin(this.Mm + this.M))) + (((0.00209d * this.E) * this.E) * Math.sin(2.0d * this.M))) - (0.00111d * Math.sin(this.Mm - (2.0d * this.F)))) - (5.7E-4d * Math.sin(this.Mm + (2.0d * this.F)))) + ((5.6E-4d * this.E) * Math.sin((2.0d * this.Mm) + this.M))) - (4.2E-4d * Math.sin(3.0d * this.Mm))) + ((4.2E-4d * this.E) * Math.sin(this.M + (2.0d * this.F)))) + ((3.8E-4d * this.E) * Math.sin(this.M - (2.0d * this.F)))) - (2.4E-4d * Math.sin((2.0d * this.Mm) - this.M))) - (1.7E-4d * Math.sin(this.omega))) - (7.0E-5d * Math.sin(this.Mm + (2.0d * this.M)))) + (4.0E-5d * Math.sin((2.0d * this.Mm) - (2.0d * this.F)))) + (4.0E-5d * Math.sin(3.0d * this.M))) + (3.0E-5d * Math.sin((this.Mm + this.M) - (2.0d * this.F)))) + (3.0E-5d * Math.sin((2.0d * this.Mm) + (2.0d * this.F)))) - (3.0E-5d * Math.sin((this.Mm + this.M) + (2.0d * this.F)))) + (3.0E-5d * Math.sin((this.Mm - this.M) + (2.0d * this.F)))) - (2.0E-5d * Math.sin((this.Mm - this.M) - (2.0d * this.F)))) - (2.0E-5d * Math.sin((3.0d * this.Mm) + this.M))) + (2.0E-5d * Math.sin(4.0d * this.Mm));
        } else if (fracK == 0.25d || fracK == 0.75d) {
            dJD = ((((((((((((((((((((((((-0.62801d * Math.sin(this.Mm)) + ((0.17172d * this.E) * Math.sin(this.M))) - ((0.01183d * this.E) * Math.sin(this.Mm + this.M))) + (0.00862d * Math.sin(2.0d * this.Mm))) + (0.00804d * Math.sin(2.0d * this.F))) + ((0.00454d * this.E) * Math.sin(this.Mm - this.M))) + (((0.00204d * this.E) * this.E) * Math.sin(2.0d * this.M))) - (0.0018d * Math.sin(this.Mm - (2.0d * this.F)))) - (7.0E-4d * Math.sin(this.Mm + (2.0d * this.F)))) - (4.0E-4d * Math.sin(3.0d * this.Mm))) - ((3.4E-4d * this.E) * Math.sin((2.0d * this.Mm) - this.M))) + ((3.2E-4d * this.E) * Math.sin(this.M + (2.0d * this.F)))) + ((3.2E-4d * this.E) * Math.sin(this.M - (2.0d * this.F)))) - (((2.8E-4d * this.E) * this.E) * Math.sin(this.Mm + (2.0d * this.M)))) + ((2.7E-4d * this.E) * Math.sin((2.0d * this.Mm) + this.M))) - (1.7E-4d * Math.sin(this.omega))) - (5.0E-5d * Math.sin((this.Mm - this.M) - (2.0d * this.F)))) + (4.0E-5d * Math.sin((2.0d * this.Mm) + (2.0d * this.F)))) - (4.0E-5d * Math.sin((this.Mm + this.M) + (2.0d * this.F)))) + (4.0E-5d * Math.sin(this.Mm - (2.0d * this.M)))) + (3.0E-5d * Math.sin((this.Mm + this.M) - (2.0d * this.F)))) + (3.0E-5d * Math.sin(3.0d * this.M))) + (2.0E-5d * Math.sin((2.0d * this.Mm) - (2.0d * this.F)))) + (2.0E-5d * Math.sin((this.Mm - this.M) + (2.0d * this.F)))) - (2.0E-5d * Math.sin((3.0d * this.Mm) + this.M));
        }
        double W = ((((0.00306d - ((3.8E-4d * this.E) * Math.cos(this.M))) + (2.6E-4d * Math.cos(this.Mm))) - (2.0E-5d * Math.cos(this.Mm - this.M))) + (2.0E-5d * Math.cos(this.Mm + this.M))) + (2.0E-5d * Math.cos(2.0d * this.F));
        if (fracK == 0.25d) {
            dJD += W;
        } else if (fracK == 0.75d) {
            dJD -= W;
        }
        return JulianDate.getDate(this.jdMeanPhase + ((((((((((((((dJD + (3.25E-4d * Math.sin(this.A1))) + (1.65E-4d * Math.sin(A2))) + (1.64E-4d * Math.sin(A3))) + (1.26E-4d * Math.sin(A4))) + (1.1E-4d * Math.sin(A5))) + (6.2E-5d * Math.sin(A6))) + (6.0E-5d * Math.sin(A7))) + (5.6E-5d * Math.sin(A8))) + (4.7E-5d * Math.sin(A9))) + (4.2E-5d * Math.sin(A10))) + (4.0E-5d * Math.sin(A11))) + (3.7E-5d * Math.sin(A12))) + (3.5E-5d * Math.sin(A13))) + (2.3E-5d * Math.sin(A14))), myDatePosition);
    }

    public DatePosition getCalendar() {
        return this.datePosition;
    }

    public double getPhase() {
        return this.phase;
    }

    public double getMm() {
        return this.Mm;
    }

    public double getM() {
        return this.M;
    }

    public double getF() {
        return this.F;
    }

    public double getOmega() {
        return this.omega;
    }

    public double getE() {
        return this.E;
    }

    public double getA1() {
        return this.A1;
    }

    public double getF1() {
        return this.F1;
    }

    public double getJDMeanPhase() {
        return this.jdMeanPhase;
    }
}
