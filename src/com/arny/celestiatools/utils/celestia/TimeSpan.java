/**
 * TimeSpan Class for ATime
 */
package com.arny.celestiatools.utils.celestia;

public class TimeSpan {
    private int nYear, nMonth, nDay, nHour, nMin;
    private double fSec;

	/**
	 * Constructor
	 */
	public TimeSpan(int nYear, int nMonth, int nDay,
					int nHour, int nMin, double fSec) {
		this.nYear  = nYear;
		this.nMonth = nMonth;
		this.nDay   = nDay;
		this.nHour  = nHour;
		this.nMin   = nMin;
		this.fSec   = fSec;
	}

    public int getnYear() {
        return nYear;
    }

    public void setnYear(int nYear) {
        this.nYear = nYear;
    }

    public int getnMonth() {
        return nMonth;
    }

    public void setnMonth(int nMonth) {
        this.nMonth = nMonth;
    }

    public int getnDay() {
        return nDay;
    }

    public void setnDay(int nDay) {
        this.nDay = nDay;
    }

    public int getnHour() {
        return nHour;
    }

    public void setnHour(int nHour) {
        this.nHour = nHour;
    }

    public int getnMin() {
        return nMin;
    }

    public void setnMin(int nMin) {
        this.nMin = nMin;
    }

    public double getfSec() {
        return fSec;
    }

    public void setfSec(double fSec) {
        this.fSec = fSec;
    }

    @Override
    public String toString() {
        return "nYear  = "+this.nYear+";nMonth = "+nMonth+";nDay   = "+nDay+";nHour  = "+nHour+";nMin   = "+nMin+";fSec   = "+fSec+";";
    }
}
