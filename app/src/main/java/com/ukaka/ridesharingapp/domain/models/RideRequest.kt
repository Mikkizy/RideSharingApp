package com.ukaka.ridesharingapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class RideRequest(
    val fare_id: String,
    val product_id: String,
    val start_latitude: Float,
    val start_longitude: Float,
    val end_latitude: Float,
    val end_longitude: Float
)
