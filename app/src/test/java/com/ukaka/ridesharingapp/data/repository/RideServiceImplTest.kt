package com.ukaka.ridesharingapp.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.ukaka.ridesharingapp.common.Resource
import com.ukaka.ridesharingapp.data.remote.UberAPI
import com.ukaka.ridesharingapp.domain.models.RideRequest
import com.ukaka.ridesharingapp.domain.models.RideResponse
import com.ukaka.ridesharingapp.domain.services.RideService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class RideServiceImplTest {
    private lateinit var rideService: RideService
    private lateinit var uberAPI: UberAPI
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8000)
        uberAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(UberAPI::class.java)
        rideService = RideServiceImpl(uberAPI)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uberApi parses correctly`() = runTest {
        val availableRides = RideResponse("1", "berlin", "arriving")

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(availableRides))
        mockWebServer.apply {
            enqueue(expectedResponse)
        }

        var actualResponse: RideResponse? = null
        val rideRequest = RideRequest(
            "1",
            "beijing",
            1.6f,
            108.6f,
            1.7f,
            132.5f
        )
        rideService.createRideRequest(rideRequest).collectLatest { result ->
            when(result) {
                is Resource.Success -> {
                    actualResponse = result.data!!
                }
                else -> {
                    //Do Nothing
                }
            }
        }

        assertThat(actualResponse).isNotNull()
    }
}