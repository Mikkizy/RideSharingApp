package com.ukaka.ridesharingapp.presentation.destination

import com.google.android.libraries.places.api.model.AutocompletePrediction

data class DestinationState(
    val destination: String = "",
    val predictions: AutocompletePrediction? = null,
    val isError: Boolean = false,
    val isLoading: Boolean = false
)
