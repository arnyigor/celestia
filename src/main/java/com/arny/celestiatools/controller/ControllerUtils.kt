package com.arny.celestiatools.controller

import com.arny.celestiatools.models.CelestiaAsteroid
import com.arny.celestiatools.utils.astronomy.AstroUtils
import com.arny.celestiatools.utils.ifNull
import org.json.simple.JSONObject

fun convertJsonAsteroid(astroJsonObject: JSONObject, asteroid: CelestiaAsteroid): CelestiaAsteroid {
    var name = astroJsonObject["Name"]
    if (name == null) {
        name = astroJsonObject["Principal_desig"].toString()
    } else {
        name = name.toString()
        if (astroJsonObject["Principal_desig"] != null) {
            name += ":" + astroJsonObject["Principal_desig"].toString()
        }
    }
    asteroid.name = name
    asteroid.orbitType = astroJsonObject["Orbit_type"].ifNull("")
    val magn = astroJsonObject["H"].ifNull(25.0)
    val radius = AstroUtils.getRadiusFromAbsoluteMagn(magn, 0.15)
    asteroid.radius = radius
    asteroid.period = astroJsonObject["Orbital_period"].ifNull(0.0)
    asteroid.sma = astroJsonObject["a"].ifNull(0.0)
    asteroid.inc = astroJsonObject["i"].ifNull(0.0)
    asteroid.node = astroJsonObject["Node"].ifNull(0.0)
    asteroid.ecc = astroJsonObject["e"].ifNull(0.0)
    asteroid.peric = astroJsonObject["Peri"].ifNull(0.0)
    asteroid.ma = astroJsonObject["M"].ifNull(0.0)
    asteroid.epoch = astroJsonObject["Epoch"].ifNull(0.0)
    return asteroid
}