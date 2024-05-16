package io.orkk.vietnam.model

data class GeoPoint(
    var x: Double = 0.0, // lon
    var y: Double = 0.0, // lat
    var z: Double = 0.0 // lat
) {
    constructor(x: Double, y: Double) : this(x, y, 0.0)

    override fun toString(): String {
        return "$y,$x"
    }
}
