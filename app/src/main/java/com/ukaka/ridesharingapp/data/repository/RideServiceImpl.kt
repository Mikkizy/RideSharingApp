package com.ukaka.ridesharingapp.data.repository

import com.ukaka.ridesharingapp.common.Resource
import com.ukaka.ridesharingapp.data.remote.UberAPI
import com.ukaka.ridesharingapp.domain.models.RideRequest
import com.ukaka.ridesharingapp.domain.models.RideResponse
import com.ukaka.ridesharingapp.domain.services.RideService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RideServiceImpl @Inject constructor(
    private val uberAPI: UberAPI
): RideService {
    override suspend fun createRideRequest(rideRequest: RideRequest): Flow<Resource<RideResponse>> = flow {
        emit(Resource.Loading())
        delay(2000L)
        try {
            val response = uberAPI.createRideRequest(rideRequest)
            if (response.isSuccessful) {
                response.body()?.let { rideResponse ->
                    return@let emit(Resource.Success(rideResponse))
                } ?:  emit( Resource.Error("An unknown error occurred!"))
            } else {
                emit( Resource.Error("Couldn't fetch data, try again!"))
            }
        } catch (e: Exception) {
            emit( Resource.Error("An error occurred! Check Internet Connection"))
        }
    }

    override suspend fun cancelRideRequest(request_id: String) {
        uberAPI.cancelRideRequest(request_id)
    }

    override suspend fun modifyRideResponse(request_id: String, status: String) {
        uberAPI.modifyRideResponse(request_id, status)
    }
}