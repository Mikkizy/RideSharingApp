package com.ukaka.ridesharingapp.presentation.ride_request

sealed class RequestEvents {
    object ConfirmOrder: RequestEvents()
    object CancelRide: RequestEvents()
}
