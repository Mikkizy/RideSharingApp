package com.ukaka.ridesharingapp.presentation.destination

import com.ukaka.ridesharingapp.domain.models.AutoCompleteModel

sealed class DestinationEvents {
    data class EnteredDestination(val destination: String): DestinationEvents()
    data class SelectedLocation(val location: AutoCompleteModel): DestinationEvents()
}
