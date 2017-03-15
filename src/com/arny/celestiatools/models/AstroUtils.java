/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arny.celestiatools.models;

import java.math.BigDecimal;

/**
 *
 * @author i.sedoy
 */
public class AstroUtils {
    private static double a1,e1,i1,peri1,node1,M1;
    private static double a2,e2,i2,peri2,node2,M2;

    public static void setA1(double a1) {
        AstroUtils.a1 = a1;
    }

    public static void setE1(double e1) {
        AstroUtils.e1 = e1;
    }

    public static void setI1(double i1) {
        AstroUtils.i1 = i1;
    }

    public static void setPeri1(double peri1) {
        AstroUtils.peri1 = peri1;
    }

    public static void setNode1(double node1) {
        AstroUtils.node1 = node1;
    }

    public static void setM1(double M1) {
        AstroUtils.M1 = M1;
    }

    public static void setA2(double a2) {
        AstroUtils.a2 = a2;
    }

    public static void setE2(double e2) {
        AstroUtils.e2 = e2;
    }

    public static void setI2(double i2) {
        AstroUtils.i2 = i2;
    }

    public static void setPeri2(double peri2) {
        AstroUtils.peri2 = peri2;
    }

    public static void setNode2(double node2) {
        AstroUtils.node2 = node2;
    }

    public static void setM2(double M2) {
        AstroUtils.M2 = M2;
    }


    private static double Cos(double angle){
        return Math.cos(Math.toRadians(angle));
    }

    private static double Sin(double angle){
        return Math.sin(Math.toRadians(angle));
    }

    public static double getR1(){
        double delim = a1*(1-Math.pow(e1, 2));
        double delit = 1 + e1*Cos(M1);
        return delim/delit;
    }

    public static double getX1(){
        return getR1() * (Cos(node1) * Cos(node1 + M1) - Sin(node1) * Sin(node1 + M1)*Cos(i1));
    }

    public static double getY1(){
        return getR1() * (Sin(node1) * Cos(node1+M1) - Cos(node1) * Sin(node1+M1)*Cos(i1));
    }

    public static double getZ1(){
        return getR1() * Sin(node1 + M1) * Sin(i1);
    }

    public static double getR2(){
        double delim = a2*(1-Math.pow(e2, 2));
        double delit = 1 + e2*Cos(M2);
        return delim/delit;
    }

    public static double getX2(){
        return getR2()* (Cos(node2) * Cos(node2 + M2) - Sin(node2) * Sin(node2 + M2)*Cos(i2));
//        return getR2() * Cos(M2);
    }

    public static double getY2(){
        return getR2() * (Sin(node2) * Cos(node2+M2) - Cos(node2) * Sin(node2+M2)*Cos(i2));
    }

    public static double getZ2(){
        return getR2() * Sin(node2 + M2) * Sin(i2);
    }
    /**
     * Вычисление юлианской даты
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static double getJD(int year,int month,double day){
        int ytmp=0,mtmp=0;
        if (month==1 || month==2) {
            ytmp = year-1;
            mtmp = month + 12;
        }else{
            ytmp = year;
            mtmp = month;
        }
        int A = ytmp/100;
        int B = 2- A + (int)(A/4);
        int C = (int) (365.25*ytmp);
        int D = (int) (30.6001 *(mtmp+1));
        return B + C + D + day + 1720994.5;
    }

    public static double getRadiusFromAbsoluteMagn(double magn, double albedo) {
	double logr = Math.log10(albedo);
	double step = 0.5 * (6.259 - logr - (0.4 * magn));
	double result = Math.pow(10, step) / 2;
	return BaseUtils.roundUp(result, 3).doubleValue();
    }

    public static double getMOID(){
        double tmp1 = Math.pow((getX1() - getX2()),2);
        double tmp2 = Math.pow((getY1() - getY2()),2);
        double tmp3 = Math.pow((getZ1() - getZ2()),2);
        return Math.sqrt(tmp1 + tmp2 + tmp3);
    }

}
