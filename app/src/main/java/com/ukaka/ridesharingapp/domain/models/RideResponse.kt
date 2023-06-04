package com.ukaka.ridesharingapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class RideResponse(
    val request_id: String,
    val product_id: String,
    val status: String
)
