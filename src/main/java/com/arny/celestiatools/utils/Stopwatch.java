package com.arny.celestiatools.utils;

import java.math.BigDecimal;
public class Stopwatch {
    private long startTime = 0;
    private long stopTime = 0;
    private long elapsed = 0;
    private boolean running = false;
    public void start() {
        this.startTime = System.nanoTime();
        this.running = true;
    }
    public void stop() {
        this.stopTime = System.nanoTime();
        this.running = false;
    }
    public void restart() {
        this.startTime = 0;
        this.stopTime = 0;
        this.startTime = System.nanoTime();
        this.running = true;
    }
    //elaspsed time in microseconds
    public long getElapsedTimeMicro() {
        if (running) {
            elapsed = ((System.nanoTime() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }
    //elaspsed time in milliseconds
    public long getElapsedTimeMili() {
        if (running) {
            elapsed = ((System.nanoTime() - startTime) / 1000000);
        } else {
            elapsed = ((stopTime - startTime) / 1000000);
        }
        return elapsed;
    }

    //elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.nanoTime() - startTime) / 1000000000) % 60;
        }
        return elapsed;
    }

	public double getElapsedTimeSecs(int scale) {
		double elapsed = 0;
		if (running) {
			elapsed = ((double) (System.nanoTime() - startTime) / 1000000000);
		}
		return new BigDecimal(elapsed).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

    //elaspsed time in minutes
    public long getElapsedTimeMin() {
        long elapsed = 0;
        if (running) {
            elapsed = (((System.nanoTime() - startTime) / 1000000000) / 60 ) % 60;
        }
        return elapsed;
    }

    //elaspsed time in hours
    public long getElapsedTimeHour() {
        long elapsed = 0;
        if (running) {
            elapsed = ((((System.nanoTime() - startTime) / 1000000000) / 60 ) / 60);
        }
        return elapsed;
    }

    public String toString() {
        return getElapsedTimeHour() + ":" + getElapsedTimeMin() + ":"
                + getElapsedTimeSecs() + "." + getElapsedTimeMili();
    }
}