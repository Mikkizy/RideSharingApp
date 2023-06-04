package com.ukaka.ridesharingapp.presentation.ride_request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.ukaka.ridesharingapp.domain.models.RideStatus
import com.ukaka.ridesharingapp.domain.services.RideService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val rideService: RideService
): ViewModel() {

    private val _state = MutableStateFlow(RequestState())
    val state = _state.asStateFlow()

    private val _currentLatLng = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLatLng: StateFlow<LatLng> get() = _currentLatLng

    fun setCurrentLatLng(latLng: LatLng) {
        _currentLatLng.value = latLng
    }

    fun onEvent(event: RequestEvents) {
        when (event) {
            is RequestEvents.CancelRide -> { viewModelScope.launch {
                rideService.cancelRideRequest("")
                _state.value = _state.value.copy(rideStatus = RideStatus.DRIVER_CANCELED)
            }

            }
            is RequestEvents.ConfirmOrder -> { viewModelScope.launch {
                _state.value = _state.value.copy(showOrderButton = false)
                rideService.modifyRideResponse("", RideStatus.ARRIVING.value)
                _state.value = _state.value.copy(rideStatus = RideStatus.ACCEPTED)
                delay(3000L)
                _state.value = _state.value.copy(rideStatus = RideStatus.COMPLETED)
            }
            }
        }
    }
}