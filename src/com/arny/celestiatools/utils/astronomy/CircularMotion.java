package com.arny.celestiatools.utils.astronomy;

public class CircularMotion {
        private double mass;
        private double radius;
        private double hp;
        private double uskorenie;
        private double v1;
        private double v2;
        private int hour;
        private int min;
        private int sec;

        public CircularMotion(double mass, double radius, double Hp) {
            this.mass = mass;
            this.radius = radius;
            hp = Hp;
        }

        public double getUskorenie() {
            return uskorenie;
        }

        public double getV1() {
            return v1;
        }

        public double getV2() {
            return v2;
        }

        public int getHour() {
            return hour;
        }

        public int getMin() {
            return min;
        }

        public int getSec() {
            return sec;
        }

        public CircularMotion calc() {
            uskorenie = (AstroConst.Gconst * mass) / Math.pow((radius + hp), 2);
            v1 = Math.sqrt((AstroConst.Gconst * mass) / (radius + hp));
            v2 = Math.sqrt(2) * v1;
            double Ptime=Math.sqrt((4 * Math.pow(Math.PI, 2)* Math.pow((radius + hp),3))/ (AstroConst.Gconst * mass))/2;
            hour = (int) (Ptime / 3600);
            min = (int) ((Ptime - hour * 3600) / 60);
            sec = (int) (Ptime - hour * 3600 - min * 60);
            return this;
        }
    }