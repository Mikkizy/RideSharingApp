package com.ukaka.ridesharingapp.data.remote

import com.ukaka.ridesharingapp.domain.models.RideRequest
import com.ukaka.ridesharingapp.domain.models.RideResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UberAPI {

    @Headers("Content-Type: application/json")
    @POST("requests")
    suspend fun createRideRequest(@Body rideRequest: RideRequest): Response<RideResponse>

    @PUT("sandbox/requests/{request_id}")
    suspend fun modifyRideResponse(
        @Path("request_id") requestID: String,
        @Body status: String
    )

    @DELETE("requests/{request_id}")
    suspend fun cancelRideRequest(@Path("request_id") requestID: String)
}