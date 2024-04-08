package io.orkk.vietnam.screen.signin

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.ActivitySignInBinding
import io.orkk.vietnam.screen.BaseActivity
import timber.log.Timber

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val navController : NavController?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController?.setGraph(R.navigation.nav_intro, intent.extras)
        Timber.i("SignIn Activity onCreate()")
    }
}