package io.orkk.vietnam.screen.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.FragmentLocationBinding
import io.orkk.vietnam.screen.BaseFragment
import io.orkk.vietnam.utils.event.EventObserver
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import io.orkk.vietnam.utils.extension.safeNavigate
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>(R.layout.fragment_location) {

    private val locationViewModel: LocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = locationViewModel
        }
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            launch {
                locationViewModel.location.collect {
                    dataBinding.tvLatitude.text = it?.latitude.toString()
                    dataBinding.tvLongitude.text = it?.longitude.toString()
                }
            }
        }

        locationViewModel.navigateToHole.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                if (isTaskRoot) {
                    findNavController().safeNavigate(LocationFragmentDirections.actionLocationFragmentToHoleFragment())
                }
            }
        })

        locationViewModel.navigateToBlockMode.observe(viewLifecycleOwner, EventObserver {
            activity?.run {
                if (isTaskRoot) {
                    findNavController().safeNavigate(LocationFragmentDirections.actionLocationFragmentToBlockModeFragment())
                }
            }
        })
    }
}