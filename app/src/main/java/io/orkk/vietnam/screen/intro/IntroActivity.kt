package io.orkk.vietnam.screen.intro

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.ActivityIntroBinding
import io.orkk.vietnam.screen.BaseActivity
import io.orkk.vietnam.utils.PermissionUtils.Companion.ALL_ESSENTIAL_PERMISSION_REQUEST_CODE
import io.orkk.vietnam.utils.PermissionUtils.Companion.getAllEssentialPermissions
import io.orkk.vietnam.utils.PermissionUtils.Companion.getPermissionPopupResource
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import timber.log.Timber

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro), EasyPermissions.PermissionCallbacks {

    private val navController: NavController?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController?.setGraph(R.navigation.nav_intro, intent.extras)
        Timber.i("Intro Activity onCreate()")
        requestAllEssentialPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(ALL_ESSENTIAL_PERMISSION_REQUEST_CODE)
    private fun requestAllEssentialPermission() {
        if (EasyPermissions.hasPermissions(this, *getAllEssentialPermissions())) {
            // TODO : Move to next
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, ALL_ESSENTIAL_PERMISSION_REQUEST_CODE, *getAllEssentialPermissions())
                    .setRationale(this.resources.getString(getPermissionPopupResource(ALL_ESSENTIAL_PERMISSION_REQUEST_CODE)!!.second))
                    .setNegativeButtonText(this.resources.getString(R.string.common_cancel))
                    .setPositiveButtonText(this.resources.getString(R.string.common_confirm))
                    .build()
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, permissions: MutableList<String>) {}
    override fun onPermissionsDenied(requestCode: Int, permissions: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, permissions)) {
            AppSettingsDialog.Builder(this)
                .setTitle(this.resources.getString(getPermissionPopupResource(requestCode)!!.first))
                .setRationale(this.resources.getString(getPermissionPopupResource(requestCode)!!.second))
                .setNegativeButton(this.resources.getString(R.string.common_cancel))
                .setPositiveButton(this.resources.getString(R.string.common_confirm))
                .build()
                .show()
        }
    }
}