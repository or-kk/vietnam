package io.orkk.vietnam.service.gps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import timber.log.Timber

@AndroidEntryPoint
class GpsService : LifecycleService() {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        initLocationManager()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ServiceCast")
    fun initLocationManager() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                Timber.w("GPS Location Latitude -> $latitude Longitude -> $longitude")
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Timber.w("GPS onStatusChanged $provider $status")
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
                Timber.w("GPS onProviderEnabled $provider")
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
                Timber.w("GPS onProviderDisabled $provider")
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.w("GPS permission is denied.")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0f,
            locationListener
        )
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(GPS_NOTIFICATION_CHANNEL_ID, GPS_NOTIFICATION_CHANNEL_NAME) else ""

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(GPS_NOTIFICATION_CONTENT_TITLE)
            .setContentText(GPS_NOTIFICATION_CONTENT_TEXT)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(GPS_NOTIFICATION_SERVICE_ID, notification)
        } else {
            startForeground(GPS_NOTIFICATION_SERVICE_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            return channelId
        }
        return ""
    }

    companion object {
        const val GPS_NOTIFICATION_SERVICE_ID = 101
        const val GPS_NOTIFICATION_CHANNEL_ID = "GPS"
        const val GPS_NOTIFICATION_CHANNEL_NAME = "GPS Service"
        const val GPS_NOTIFICATION_CONTENT_TITLE = "Vietnam"
        const val GPS_NOTIFICATION_CONTENT_TEXT = "GPS Service is connected."
    }
}