package com.arny.celestiatools;
public class Controller {
    public static String getString() {
	long cnt = 0;
	long start = System.currentTimeMillis();
	String time = " time:" + (System.currentTimeMillis() - start);
	return String.valueOf(cnt + time);
    }
}
