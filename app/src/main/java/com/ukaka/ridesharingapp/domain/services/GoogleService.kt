package com.ukaka.ridesharingapp.domain.services

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import com.google.maps.model.Unit
import com.ukaka.ridesharingapp.common.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GoogleService @Inject constructor(
    @ApplicationContext context: Context,
    private val geoApiContext: GeoApiContext
) {

    private val client: PlacesClient by lazy {
        Places.createClient(context)
    }
    private var token: AutocompleteSessionToken? = null

    suspend fun getPlaceCoordinates(placeId: String): Resource<FetchPlaceResponse?> = withContext(Dispatchers.IO) {
        val placeFields: List<Place.Field> = listOf(
            Place.Field.LAT_LNG
        )
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        try {
            Resource.Success(awaitResult(client.fetchPlace(request)))
        } catch (e: Exception) {
            Resource.Error(e.toString())
        }
    }

    suspend fun getDistanceBetween(userLatLng: LatLng, comparedTo: LatLng): String = withContext(Dispatchers.IO) {
        Timber.tag("DIRECTIONS").d(userLatLng.toString())
        val dirResult =
            DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .units(Unit.METRIC)
                //Change this appropriately
                .region("ca")
                .origin(
                    userLatLng
                )
                .destination(
                    comparedTo
                )
                .await()

        if (dirResult.routes?.first() != null &&
            dirResult.routes.isNotEmpty() &&
            dirResult.routes.first().legs.isNotEmpty()
        ) {
            dirResult.routes.first().legs.first().distance.humanReadable
        } else {
            "Error"
        }
    }

    suspend fun getAutocompleteResults(query: String): Resource<List<AutocompletePrediction>> =
        withContext(
            Dispatchers.IO
        ) {
            if (token == null) token = AutocompleteSessionToken.newInstance()

            val request = FindAutocompletePredictionsRequest.builder()
                    //Obviously change this according to distribution of the app
                .setCountries(listOf("NG", "CA", "US"))
                .setTypesFilter(listOf(PlaceTypes.ADDRESS))
                .setSessionToken(token)
                .setQuery(query)
                .build()

            try {
                Resource.Loading(null)
                val task = awaitResult(client.findAutocompletePredictions(request))
                Resource.Success(task.autocompletePredictions)
            } catch (e: Exception) {
                Resource.Error(e.toString())
            }
        }

    private suspend fun <T> awaitResult(task: Task<T>): T = suspendCoroutine { continuation ->
        task.addOnCompleteListener {
            if (task.isSuccessful) continuation.resume(task.result!!)
            else continuation.resumeWithException(task.exception!!)
        }
    }
}