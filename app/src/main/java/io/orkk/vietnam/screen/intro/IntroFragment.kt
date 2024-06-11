package io.orkk.vietnam.screen.intro


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentIntroBinding
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.screen.intro.initial.InitialSettingViewModel
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import io.orkk.vietnam.utils.extension.safeNavigate
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {

    private val introViewModel: IntroViewModel by viewModels()
    private val initialSettingViewModel: InitialSettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = introViewModel
            initialViewModel = initialSettingViewModel
        }
    }

    override fun initView() {
        super.initView()
        showLoading()
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            launch {
                introViewModel.loading.observe(viewLifecycleOwner, Observer {
                    if (it) showLoading() else hideLoading()
                })
            }

            launch {
                initialSettingViewModel.isInitialSetting.collect {
                    if (it != null) {
                        if (it) findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment())
                        else findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToInitialDialogFragment())
                        hideLoading()
                    }
                }
            }
        }
    }
}