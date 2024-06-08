package io.orkk.vietnam.screen.intro


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentIntroBinding
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.screen.intro.initial.InitialSettingViewModel
import io.orkk.vietnam.utils.event.EventObserver
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import io.orkk.vietnam.utils.extension.safeNavigate

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
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            initialSettingViewModel.isExistClubInfo.collect {
                if (it) findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment())
                else findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToInitialDialogFragment())
            }
        }

        introViewModel.navigateToSignIn.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                if (isTaskRoot) {
//                    findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment())
                    findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToDownloadDialogFragment())
                }
            }
        })
    }
}