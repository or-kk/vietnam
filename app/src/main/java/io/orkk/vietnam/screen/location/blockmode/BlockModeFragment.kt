package io.orkk.vietnam.screen.location.blockmode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentBlockModeBinding
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class BlockModeFragment : BaseFragment<FragmentBlockModeBinding>(R.layout.fragment_block_mode) {

    private val blockModeViewModel: BlockModeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = blockModeViewModel
        }
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            blockModeViewModel.currentScreenOrientation.observe(viewLifecycleOwner, Observer {
                activity?.run {
                    requestedOrientation = it
                }
            })
        }
    }
}