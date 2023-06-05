package com.ukaka.ridesharingapp.presentation.ride_request

import com.ukaka.ridesharingapp.domain.models.RideStatus

data class RequestState(
    val rideStatus: RideStatus = RideStatus.PROCESSING,
    val showOrderButton: Boolean = true
)
