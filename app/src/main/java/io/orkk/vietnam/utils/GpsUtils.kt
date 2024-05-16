package io.orkk.vietnam.utils

import android.location.Location
import io.orkk.vietnam.model.GolfClubData
import timber.log.Timber

class GpsUtils {

    companion object {
        private const val GAP_BASE_TIME_MS = 600000

        private var previousLocation: Location? = null
        private var previousGpsTimeMs = System.currentTimeMillis()
        private var currentGpsTimeMs = System.currentTimeMillis()
        private var gpsTimeGapMs: Long = 0
        private var receiveToPreviousDistance = 0.0
        private var startGpsTime = 0L
        private var activeGpsTime = 0L
        private var mCheckActGPSTime = true // GPS 작동 체크
        private var errorCount = 0

        fun checkValidCoordinates(location: Location) {
            val receiveLocation = Location("pA").apply {
                latitude = (location.latitude.toInt() * 100000 * 0.00001)
                longitude = (location.longitude.toInt() * 100000 * 0.00001)
            }
            val currentLocation = Location("pA")
            var averageDistance = 0.0

            try {
                if (!GpsUtils().isAreaGolfClub(receiveLocation)) {
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
                    if (mCheckActGPSTime) {
                        startGpsTime = currentGpsTimeMs
                        mCheckActGPSTime = false
                    }
                } else {
                    mCheckActGPSTime = true
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
                GpsUtils().whereAmI(currentLocation.latitude, currentLocation.longitude)
            } catch (e: java.lang.Exception) {
                Timber.e("checkValidCoordinates -> ${e.message}")
            }
        }
    }

    private fun whereAmI(latitude: Double, longitude: Double) {

    }

    private fun isAreaGolfClub(location: Location): Boolean {
        return (location.longitude < GolfClubData().mWMaxGeo!!.x
                && location.longitude > GolfClubData().mWMinGeo!!.x
                && location.latitude < GolfClubData().mWMaxGeo!!.y
                && location.latitude > GolfClubData().mWMinGeo!!.y)
    }
}