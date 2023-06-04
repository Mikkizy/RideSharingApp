package com.ukaka.ridesharingapp.domain.models

import com.google.android.libraries.places.api.model.AutocompletePrediction

data class AutoCompleteModel(
    val address: String,
    val prediction: AutocompletePrediction
)