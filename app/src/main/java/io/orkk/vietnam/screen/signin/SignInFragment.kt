package io.orkk.vietnam.screen.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentSignInBinding
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.utils.event.EventObserver
import io.orkk.vietnam.utils.extension.safeNavigate
import io.orkk.vietnam.utils.DebounceTextWatcher
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import io.orkk.vietnam.utils.extension.setTextInputError
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val signInViewModel: SignInViewModel by viewModels()

    private val idTextChangedListener by lazy {
        DebounceTextWatcher(
            debouncePeriod = 0L,
            onEditTextChange = signInViewModel::checkIdValidation,
            onEditTextStateChange = signInViewModel::setIdEditTextState
        )
    }

    private val passwordTextChangedListener by lazy {
        DebounceTextWatcher(
            debouncePeriod = 0L,
            onEditTextChange = signInViewModel::checkPasswordValidation,
            onEditTextStateChange = signInViewModel::setPasswordEditTextState
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = signInViewModel
        }
    }

    override fun initListener() {
        super.initListener()
        with(dataBinding) {
            tiEtSignInId.addTextChangedListener(idTextChangedListener)
            tiEtSignInPassword.addTextChangedListener(passwordTextChangedListener)
        }
    }

    override fun removeListener() {
        super.removeListener()
        with(dataBinding) {
            tiEtSignInId.removeTextChangedListener(idTextChangedListener)
            tiEtSignInPassword.removeTextChangedListener(passwordTextChangedListener)
        }
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            launch {
                signInViewModel.idEditTextState.collect {
                    dataBinding.tiSignInId.apply {
                        setTextInputError(it)
                    }
                }
            }

            launch {
                signInViewModel.passwordEditTextState.collect {
                    dataBinding.tiSignInPassword.apply {
                        setTextInputError(it)
                    }
                }
            }

            signInViewModel.dataFlow.collect { data ->
                Timber.w("data is $data")
            }
        }

        signInViewModel.navigateToMain.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                if (isTaskRoot) {
                    findNavController().safeNavigate(SignInFragmentDirections.actionSignInFragmentToMainActivity())
                }
            }
        })
    }
}