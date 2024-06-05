package io.orkk.vietnam.screen.intro.initial

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.DialogInitialSettingBinding
import io.orkk.vietnam.screen.BaseDialogFragment
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import timber.log.Timber

@AndroidEntryPoint
class InitialSettingDialogFragment() : BaseDialogFragment<DialogInitialSettingBinding>() {
    override val layoutId: Int = R.layout.dialog_initial_setting

    private val initialSettingViewModel: InitialSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRemoteConfig()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = initialSettingViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    private fun fetchRemoteConfig() {
        initialSettingViewModel.run {
            fetchClubInfoConfig()
            fetchUrlInfoConfig()
        }
    }

    override fun initView() {
        super.initView()
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            initialSettingViewModel.clubInfoList.observe(viewLifecycleOwner, Observer { configList ->
                configList.forEach { config ->
                    Timber.d("Firebase remote config -> club index : ${config.clubIndex}, club name : ${config.clubName}")
                }
            })

            initialSettingViewModel.clubInfoFetchError.observe(viewLifecycleOwner, Observer { exception ->
                Timber.e("Firebase remote config -> exception -> $exception")
                Toasty.error(requireActivity(), R.string.fetch_remote_config_fail_message, Toast.LENGTH_SHORT, false).show()
            })

            initialSettingViewModel.urlInfoList.observe(viewLifecycleOwner, Observer { configList ->
                configList.forEach { config ->
                    Timber.d("Firebase remote config -> club index : ${config.clubIndex}, download url : ${config.downloadUrl}")
                }
            })

            initialSettingViewModel.urlInfoFetchError.observe(viewLifecycleOwner, Observer { exception ->
                Timber.e("Firebase remote config -> exception -> $exception")
                Toasty.error(requireActivity(), R.string.fetch_remote_config_fail_message, Toast.LENGTH_SHORT, false).show()
            })
        }
    }
}