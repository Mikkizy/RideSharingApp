package com.ukaka.ridesharingapp.domain.services

import com.ukaka.ridesharingapp.common.Resource
import com.ukaka.ridesharingapp.domain.models.RideRequest
import com.ukaka.ridesharingapp.domain.models.RideResponse
import kotlinx.coroutines.flow.Flow

interface RideService {

    suspend fun createRideRequest(rideRequest: RideRequest): Flow<Resource<RideResponse>>

    suspend fun cancelRideRequest(request_id: String)

    suspend fun modifyRideResponse(request_id: String, status: String)
}