package com.ukaka.ridesharingapp.presentation.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.maps.model.LatLng
import com.ukaka.ridesharingapp.common.Resource
import com.ukaka.ridesharingapp.common.ToastMessages
import com.ukaka.ridesharingapp.domain.models.AutoCompleteModel
import com.ukaka.ridesharingapp.domain.models.RideRequest
import com.ukaka.ridesharingapp.domain.services.GoogleService
import com.ukaka.ridesharingapp.domain.services.RideService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel @Inject constructor(
    private val googleService: GoogleService,
    private val rideService: RideService
): ViewModel() {

    private val _state = MutableStateFlow(DestinationState())
    val state = _state.asStateFlow()

    private val _autoCompleteList = MutableStateFlow<List<AutoCompleteModel>>(emptyList())
    val autoCompleteList: StateFlow<List<AutoCompleteModel>> get() = _autoCompleteList

    internal var toastHandler: ((ToastMessages) -> Unit)? = null

    fun onEvent(event: DestinationEvents) {
        when(event) {
            is DestinationEvents.EnteredDestination -> {
                _state.value = _state.value.copy(destination = event.destination)
                requestAutocompleteResults(event.destination)
            }
            is DestinationEvents.SelectedLocation -> {
                handleSearchItemClick(event.location)
            }
        }
    }

    private fun requestAutocompleteResults(query: String) = viewModelScope.launch {
        val autocompleteRequest = googleService.getAutocompleteResults(query)
        when (autocompleteRequest) {
            is Resource.Error -> {
                _state.value = _state.value.copy(isError = true)
                toastHandler?.invoke(ToastMessages.SERVICE_ERROR)
            }
            is Resource.Success -> {
                _autoCompleteList.value = autocompleteRequest.data!!.map { prediction ->
                    _state.value = _state.value.copy(predictions = prediction)
                    AutoCompleteModel(
                        address = prediction.getFullText(null).toString(),
                        prediction = prediction
                    )
                }
            }
            is Resource.Loading -> {
                _state.value = _state.value.copy(isLoading = true)
            }
        }
    }

    private fun handleSearchItemClick(selectedPlace: AutoCompleteModel) = viewModelScope.launch {
        val getCoordinates = googleService.getPlaceCoordinates(selectedPlace.prediction.placeId)

        when (getCoordinates) {
            is Resource.Error -> {
                _state.value = _state.value.copy(isError = true)
                toastHandler?.invoke(ToastMessages.UNABLE_TO_RETRIEVE_COORDINATES)
            }
            is Resource.Success -> {
                if (getCoordinates.data != null &&
                    getCoordinates.data.place.latLng != null
                ) {
                    val rideRequest = attemptToCreateRideRequest(getCoordinates.data)
                    rideService.createRideRequest(rideRequest)
                } else {
                    toastHandler?.invoke(ToastMessages.SERVICE_ERROR)
                }
            }
            else -> {}
        }
    }

    private var userLatLng = LatLng()

    private fun generateRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..37)
            .map {
                allowedChars.random()
            }
            .joinToString("")
    }

    private fun attemptToCreateRideRequest(response: FetchPlaceResponse): RideRequest {
        return RideRequest(
            fare_id = generateRandomString(),
            product_id = generateRandomString(),
            start_latitude = userLatLng.lat.toFloat(),
            start_longitude = userLatLng.lng.toFloat(),
            end_latitude = response.place.latLng!!.latitude.toFloat(),
            end_longitude = response.place.latLng!!.longitude.toFloat()
        )
    }
}