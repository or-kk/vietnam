package io.orkk.vietnam.screen.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentSignInBinding
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.utils.event.EventObserver
import io.orkk.vietnam.utils.extension.safeNavigate

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in){

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = signInViewModel
        }
    }

    override fun initObserver() {
        super.initObserver()
        signInViewModel.navigateToMain.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                if (isTaskRoot) {
                    findNavController().safeNavigate(SignInFragmentDirections.actionSignInFragmentToMainActivity())
                }
            }
        })
    }
}