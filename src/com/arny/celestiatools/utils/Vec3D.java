package com.arny.celestiatools.utils;
public class Vec3D {
	private double m_Vec[] = new double[3];
	public Vec3D() {
	}

	public Vec3D(double X,double Y,double Z) {
		this.m_Vec[0] = X;
		this.m_Vec[1] = Y;
		this.m_Vec[2] = Z;
	}
}
