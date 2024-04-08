package io.orkk.vietnam.screen.intro

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.screen.BaseActivity
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.ActivityIntroBinding
import timber.log.Timber

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    private val navController : NavController?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController?.setGraph(R.navigation.nav_intro, intent.extras)
        Timber.i("Intro Activity onCreate()")
    }
}