package io.orkk.vietnam.utils.location

import android.location.Location
import timber.log.Timber
import javax.inject.Inject

class GpsManager {

    @Inject lateinit var locationManager: LocationManager

    private var previousLocation: Location? = null
    private var previousGpsTimeMs = System.currentTimeMillis()
    private var currentGpsTimeMs = System.currentTimeMillis()
    private var gpsTimeGapMs: Long = 0
    private var receiveToPreviousDistance = 0.0
    private var startGpsTime = 0L
    private var activeGpsTime = 0L
    private var isActiveGps = true
    private var errorCount = 0

    fun checkValidCoordinates(location: Location) {
        val receiveLocation = Location("pA").apply {
            latitude = (location.latitude.toInt() * 100000 * 0.00001)
            longitude = (location.longitude.toInt() * 100000 * 0.00001)
        }
        val currentLocation = Location("pA")
        var averageDistance = 0.0

        try {
            if (!locationManager.isAreaGolfClub(receiveLocation)) {
                Timber.i("checkValidCoordinates -> Coordinates outside the golf course area, invalidated")
                return
            }

            if (previousLocation == null) {
                previousLocation = receiveLocation
            }

            currentGpsTimeMs = System.currentTimeMillis()
            gpsTimeGapMs = currentGpsTimeMs - previousGpsTimeMs

            receiveToPreviousDistance = receiveLocation.distanceTo(previousLocation!!).toDouble()

            if (receiveLocation.latitude != 0.0 && receiveLocation.longitude != 0.0) {
                activeGpsTime = currentGpsTimeMs
                if (isActiveGps) {
                    startGpsTime = currentGpsTimeMs
                    isActiveGps = false
                }
            } else {
                isActiveGps = true
            }

            if (gpsTimeGapMs > GAP_BASE_TIME_MS) {
                currentLocation.set(receiveLocation)
                receiveToPreviousDistance = 1.0
                previousGpsTimeMs = currentGpsTimeMs
                errorCount = 0
            } else {
                if (gpsTimeGapMs > 0) {
                    averageDistance = (receiveToPreviousDistance * 1000.0) / gpsTimeGapMs.toDouble()

                    if (averageDistance.isNaN()) {
                        Timber.i("Distance is NaN.")
                    } else {
                        if (averageDistance > 20.0) {
                            Timber.i("Average distance over 20km.")
                            if (previousLocation!!.latitude == 0.0 || previousLocation!!.longitude == 0.0) {
                                previousLocation = receiveLocation
                                currentLocation.set(receiveLocation)
                                receiveToPreviousDistance = 1.0
                                previousGpsTimeMs = currentGpsTimeMs
                                errorCount = 0
                            } else {
                                errorCount++

                                if (errorCount > 20) {
                                    errorCount = 0
                                    currentLocation.set(receiveLocation)
                                    receiveToPreviousDistance = 1.0
                                    previousGpsTimeMs = currentGpsTimeMs
                                    errorCount = 0
                                    Timber.i("Recognized as new coordinates over 72km")
                                } else {
                                    currentLocation.set(previousLocation!!)
                                }
                            }
                        } else {
                            currentLocation.set(receiveLocation)
                            previousGpsTimeMs = currentGpsTimeMs
                            errorCount = 0
                        }
                    }
                }
            }

            if (previousLocation!!.latitude != 0.0 && previousLocation!!.longitude != 0.0) {
                if (averageDistance >= 40 && averageDistance < 50) {
                    Timber.v("Below 180km (under inspection)")
                } else if (averageDistance >= 50) {
                    Timber.v("Over 180km (under inspection)")
                }
            }
            previousLocation = currentLocation
        } catch (e: Exception) {
            Timber.e("checkValidCoordinates -> ${e.message}")
        }

        try {
            locationManager.whereAmI(currentLocation.latitude, currentLocation.longitude)
        } catch (e: java.lang.Exception) {
            Timber.e("checkValidCoordinates -> ${e.message}")
        }
    }

    companion object {
        private const val GAP_BASE_TIME_MS = 600000
    }
}