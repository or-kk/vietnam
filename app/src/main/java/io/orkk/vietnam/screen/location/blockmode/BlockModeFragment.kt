package io.orkk.vietnam.screen.location.blockmode

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentBlockModeBinding
import io.orkk.vietnam.databinding.LayoutBlockModeSectionPar3Binding
import io.orkk.vietnam.databinding.LayoutBlockModeSectionPar4Binding
import io.orkk.vietnam.databinding.LayoutBlockModeSectionPar5Binding
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

        initBlockModeViewUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initBlockModeViewUI() {
        val column = if (blockModeViewModel.currentScreenOrientation.value == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) 1 else 3
        val row = if (blockModeViewModel.currentScreenOrientation.value == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) 1 else 2
        with (dataBinding) {
            glBlockMode.apply {
                columnCount = column
                rowCount = row
                val inflater = LayoutInflater.from(requireActivity())

                for (i in 0..8) {
                    val layoutParams2 = GridLayout.LayoutParams().apply {
                        width = 0
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        columnSpec = GridLayout.spec(i % column, GridLayout.FILL, 1f)
                        rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                    }

                    when (blockModeViewModel.currentParInfo[i]) {
                        3 -> {
                            val binding = LayoutBlockModeSectionPar3Binding.inflate(inflater)
                            val rootView = binding.root
                            rootView.layoutParams = layoutParams2
                            binding.tvHoleNumber.text = (i + 1).toString()

                            addView(rootView)
                        }

                        4 -> {
                            val binding = LayoutBlockModeSectionPar4Binding.inflate(inflater)
                            val rootView = binding.root
                            rootView.layoutParams = layoutParams2
                            binding.tvHoleNumber.text = (i + 1).toString()

                            addView(rootView)
                        }

                        5 -> {
                            val binding = LayoutBlockModeSectionPar5Binding.inflate(inflater)
                            val rootView = binding.root
                            rootView.layoutParams = layoutParams2
                            binding.tvHoleNumber.text = (i + 1).toString()

                            addView(rootView)
                        }
                    }
                }
            }
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