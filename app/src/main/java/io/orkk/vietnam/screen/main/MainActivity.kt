package io.orkk.vietnam.screen.main

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.screen.BaseActivity
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.ActivityMainBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("mainactivityy")
    }

    override fun initView() {
        super.initView()
    }

    override fun initViewModel() {
        super.initViewModel()
    }

    override fun initListener() {
        super.initListener()
    }
}