package io.orkk.vietnam.screen.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.local.LocationRepository
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.GpsUtils
import io.orkk.vietnam.utils.event.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : BaseViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?>
        get() = _location

    private val _navigateToHole = MutableLiveData<Event<Unit>>()
    val navigateToHole: LiveData<Event<Unit>>
        get() = _navigateToHole

    private val _navigateToBlockMode = MutableLiveData<Event<Unit>>()
    val navigateToBlockMode: LiveData<Event<Unit>>
        get() = _navigateToBlockMode


    fun requestLocationUpdate() {
        locationRepository.requestLocationUpdates { location ->
            GpsUtils.checkValidCoordinates(location)
            _location.value = location
        }
    }

    fun navigateToHole() {
        _navigateToHole.value = Event(Unit)
    }

    fun navigateToBlockMode() {
        _navigateToBlockMode.value = Event(Unit)
    }
}