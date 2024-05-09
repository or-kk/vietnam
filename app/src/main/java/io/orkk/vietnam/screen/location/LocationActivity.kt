package io.orkk.vietnam.screen.location

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.ActivityLocationBinding
import io.orkk.vietnam.screen.BaseActivity
import timber.log.Timber

@AndroidEntryPoint
class LocationActivity : BaseActivity<ActivityLocationBinding>(R.layout.activity_location) {
    private val navController : NavController?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController?.setGraph(R.navigation.nav_location, intent.extras)
        Timber.i("Location Activity onCreate()")
    }
}