package io.orkk.vietnam.screen.intro.initial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.DialogInitialSettingBinding
import io.orkk.vietnam.screen.BaseDialogFragment

@AndroidEntryPoint
class InitialSettingDialogFragment() : BaseDialogFragment<DialogInitialSettingBinding>() {
    override val layoutId: Int = R.layout.dialog_initial_setting

    private val initialSettingViewModel: InitialSettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = initialSettingViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    override fun initView() {
        super.initView()
    }

    override fun initObserver() {
        super.initObserver()
    }
}