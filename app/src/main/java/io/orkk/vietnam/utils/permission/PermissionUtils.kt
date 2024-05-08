package io.orkk.vietnam.utils.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import io.orkk.vietnam.R

class PermissionUtils {

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun getAllEssentialPermissions() = getStoragePermissions() + getLocationPermissions() + getNotificationPermission()

        // api >= 33
        private fun getStoragePermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }

        private fun getLocationPermissions() = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private fun getNotificationPermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else arrayOf()

        fun getPermissionPopupResource(requestCode: Int): Pair<Int, Int>? {
            return when (requestCode) {
                ALL_ESSENTIAL_PERMISSION_REQUEST_CODE -> Pair(R.string.essential_permission_required_information_title, R.string.essential_permission_required_information_rationale)
                STORAGE_PERMISSION_REQUEST_CODE -> Pair(R.string.storage_permission_required_information_title, R.string.storage_permission_required_information_rationale)
                LOCATION_PERMISSION_REQUEST_CODE -> Pair(R.string.location_permission_required_information_title, R.string.location_permission_required_information_rationale)
                else -> null
            }
        }

        const val ALL_ESSENTIAL_PERMISSION_REQUEST_CODE = 101
        private const val STORAGE_PERMISSION_REQUEST_CODE = 201
        private const val LOCATION_PERMISSION_REQUEST_CODE = 301
    }
}