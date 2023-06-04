package com.ukaka.ridesharingapp.presentation.permissions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED)
}

fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {

    val activity = context as Activity?
    if (activity == null)
        Log.d("Maps Util", "Activity is null")

    return ActivityCompat.shouldShowRequestPermissionRationale(
        activity!!,
        permission
    )
}

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow() = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            try {
                trySend(result.lastLocation)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
    requestLocationUpdates(createLocationRequest(), callback, Looper.getMainLooper())
        .addOnFailureListener { e ->
            close(e)
        }

    awaitClose {
        removeLocationUpdates(callback)
    }
}

fun createLocationRequest(): LocationRequest {
    return LocationRequest.Builder(
        PRIORITY_HIGH_ACCURACY,
        20000
    ).apply {
       setMinUpdateIntervalMillis(10000)
    }.build()
}