package io.orkk.vietnam.screen.intro


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentIntroBinding
import io.orkk.vietnam.utils.EventObserver
import io.orkk.vietnam.utils.safeNavigate


class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {

    private val introViewModel: IntroViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = introViewModel
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
        introViewModel.navigateToSignIn.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                if (isTaskRoot) {
                    findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToSignInFragment())
                }
            }
        })
    }
}