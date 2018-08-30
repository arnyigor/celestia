package com.arny.celestiatools.utils.circularmotion


fun calcCircularOrbit(mass: Double, radius: Double, Hp: Double): CircularMotion {
    return CircularMotion(mass, radius, Hp)
}

fun calcEllipseOrbit(Mass: Double, Radius: Double, Hp: Double, Ha: Double, Vp: Double, ecc: Double, sma: Double, period: Double): EllipseMotion {
    return EllipseMotion(Mass, Radius, Hp, Ha, Vp, ecc, sma, period)
}

