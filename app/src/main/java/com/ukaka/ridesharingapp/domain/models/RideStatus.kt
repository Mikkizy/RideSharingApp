package com.ukaka.ridesharingapp.domain.models

enum class RideStatus(val value: String) {
    PROCESSING("processing"),
    ACCEPTED("accepted"),
    ARRIVING("arriving"),
    DRIVER_CANCELED("driver_canceled"),
    COMPLETED("completed")
}