package com.arny.celestiatools.models

class CelestiaAsteroid {
    var name: String? = null
    var orbitType: String? = null
    var updateTime: String? = null
    var radius: Double = 0.toDouble()
    var period: Double = 0.toDouble()
    var sma: Double = 0.toDouble()
    var inc: Double = 0.toDouble()
    var node: Double = 0.toDouble()
    var ecc: Double = 0.toDouble()
    var peric: Double = 0.toDouble()
    var ma: Double = 0.toDouble()
    var epoch: Double = 0.toDouble()

    override fun toString(): String {
        return "Name" + name +
                "\nOrbitType:" + orbitType +
                "\nradius:" + radius +
                "\nperiod:" + period +
                "\nsma:" + sma +
                "\ninc:" + inc +
                "\nnode:" + node +
                "\necc:" + ecc +
                "\nperic:" + peric +
                "\nMa:" + ma +
                "\nepoch" + epoch +
                "\nupdateTime" + updateTime
    }
}
