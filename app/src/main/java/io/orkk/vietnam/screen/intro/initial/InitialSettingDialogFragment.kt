package io.orkk.vietnam.screen.intro.initial

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.DialogInitialSettingBinding
import io.orkk.vietnam.screen.BaseDialogFragment
import io.orkk.vietnam.utils.event.EventObserver
import io.orkk.vietnam.utils.extension.adjustDialogFragment
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import io.orkk.vietnam.utils.extension.safeNavigate
import timber.log.Timber

@AndroidEntryPoint
class InitialSettingDialogFragment() : BaseDialogFragment<DialogInitialSettingBinding>() {
    override val layoutId: Int = R.layout.dialog_initial_setting

    private val initialSettingViewModel: InitialSettingViewModel by viewModels()

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            initialSettingViewModel.setSelectedClub(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

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
        initListener()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        context?.adjustDialogFragment(this@InitialSettingDialogFragment, ADJUST_SCREEN_WIDTH, ADJUST_SCREEN_HEIGHT)
        isCancelable = false
    }

    private fun fetchRemoteConfig() {
        initialSettingViewModel.run {
            fetchClubInfoConfig()
            fetchUrlInfoConfig()
            fetchAppdataUpdateInfoConfig()
        }
    }

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        dataBinding.spGolfCourse.onItemSelectedListener = spinnerListener
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            with(initialSettingViewModel) {
                clubInfoFetchError.observe(viewLifecycleOwner, Observer { exception ->
                    Timber.e("Firebase remote config club info -> exception -> $exception")
                    Toasty.error(requireActivity(), R.string.fetch_remote_config_club_info_fail_message, Toast.LENGTH_SHORT, false).show()
                })

                selectedClubInfo.observe(viewLifecycleOwner, Observer {
                    Timber.e("select club info index -> ${it.clubIndex} name -> ${it.clubName}")
                })

                urlInfoFetchedList.observe(viewLifecycleOwner, Observer { configList ->
                    configList.forEach { config ->
                        Timber.d("Firebase remote config -> club index : ${config.clubIndex}, download url : ${config.downloadUrl}")
                    }
                })

                urlInfoFetchError.observe(viewLifecycleOwner, Observer { exception ->
                    Timber.e("Firebase remote config url info -> exception -> $exception")
                    Toasty.error(requireActivity(), R.string.fetch_remote_config_url_info_fail_message, Toast.LENGTH_SHORT, false).show()
                })

                appdataUpdateInfoList.observe(viewLifecycleOwner, Observer { configList ->
                    configList.forEach {
                        Timber.d("appdata download info list : ${it.clubIndex} download list : ${it.downloadFileList}")
                    }
                })

                appdataUpdateInfoFetchError.observe(viewLifecycleOwner, Observer { exception ->
                    Timber.e("Firebase remote config url info -> exception -> $exception")
                    Toasty.error(requireActivity(), R.string.fetch_remote_appdata_download_info_fail_message, Toast.LENGTH_SHORT, false).show()
                })
            }
        }

        initialSettingViewModel.navigateToDownload.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                findNavController().safeNavigate(InitialSettingDialogFragmentDirections.actionInitialSettingDialogFragmentToDownloadDialogFragment())
            }
        })
    }

    companion object {
        const val ADJUST_SCREEN_WIDTH = 0.8f
        const val ADJUST_SCREEN_HEIGHT = 0.45f
    }
}