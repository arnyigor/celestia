package com.arny.celestiatools.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

public class MathUtils {


    public static String simpleDoubleFormat(double d) {
        if (d == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(340);
        return df.format(d).replace(",",".");
    }

    public static String toExpo(double number, int precision) {
        if (precision <= 0) {
            precision = 1;
        }
        StringBuilder prec = new StringBuilder();
        for (int i = 0; i < precision; i++) {
            prec.append("0");
        }
        return new DecimalFormat("0." + prec.toString() + "E0").format(number).replace(",", ".");
    }

    /**
     * дробная часть числа
     *
     * @param x
     * @return
     */
    public static double fracal(double x) {
        return x - (int) x;
    }

    /**
     * целая часть числа
     *
     * @param x
     * @return
     */
    public static int intact(double x) {
        return (int) x;
    }

    /**
     * Отстаток от деления
     *
     * @param x
     * @param y
     * @return
     */
    public static double modulo(double x, double y) {
        return y * (fracal(x / y));
    }

    public static double round(double val, int scale) {
        return new BigDecimal(val).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double val) {
        return new BigDecimal(val).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	public static double summ(double num1, double num2, int scale) {
		return new BigDecimal(num1).add(new BigDecimal(num2)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double minus(double num1, double num2, int scale) {
		return new BigDecimal(num1).subtract(new BigDecimal(num2)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double multiply(double num1, double num2, int scale) {
		return new BigDecimal(num1).multiply(new BigDecimal(num2)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double divide(double num1, double num2, int scale) {
		return new BigDecimal(num1).divide(new BigDecimal(num2),BigDecimal.ROUND_HALF_UP).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

    public static long randLong(long min, long max) {
        Random rnd = new Random();
        if (min > max) {
            throw new IllegalArgumentException("min>max");
        }
        if (min == max) {
            return min;
        }
        long n = rnd.nextLong();
        n = n == Long.MIN_VALUE ? 0 : n < 0 ? -n : n;
        n = n % (max - min);
        return min + n;
    }

    public static double randDouble(double min, double max) {
        Random rnd = new Random();
        double range = max - min;
        double scaled = rnd.nextDouble() * range;
        return scaled + min; // == (rand.nextDouble() * (max-min)) + min;
    }

    public static int randInt(int min, int max) {
        Random rnd = new Random();
        int range = max - min + 1;
        return rnd.nextInt(range) + min;
    }

    public static double Cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public static double Acos(double rad) {
        return Math.toDegrees(Math.acos(rad));
    }

    public static double Sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public static double Asin(double rad) {
        return Math.toDegrees(Math.asin(rad));
    }

    public static double Tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    public static double Atan(double rad) {
        return Math.toDegrees(Math.atan(rad));
    }

    public static double Atan2(double rad1,double rad2) {
        return Math.toDegrees(Math.atan2(rad1,rad2));
    }

    public static double Sqrt(double num) {
        return Math.sqrt(num);
    }

    public static double Exp(double num, double exp) {
        return Math.pow(num, exp);
    }

    public static double Abs(double num) {
        return Math.abs(num);
    }

}
