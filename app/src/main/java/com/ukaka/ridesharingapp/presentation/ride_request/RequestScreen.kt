package com.ukaka.ridesharingapp.presentation.ride_request

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ukaka.ridesharingapp.common.Dimensions
import com.ukaka.ridesharingapp.domain.models.RideStatus
import com.ukaka.ridesharingapp.presentation.permissions.PermissionAction
import com.ukaka.ridesharingapp.presentation.permissions.PermissionDialog
import com.ukaka.ridesharingapp.presentation.shared.TopBar
import com.ukaka.ridesharingapp.ui.theme.RideSharingAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(
    state: RequestState,
    currentLocation: LatLng,
    destination: String,
    snackbarHostState: SnackbarHostState,
    onEvent: (RequestEvents) -> Unit,
    fetchLocationUpdates: () -> Unit,
    navController: NavController
) {
    Scaffold(topBar = {
        TopBar(title = "Request Ride") {
            navController.navigateUp()
        }
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Dimensions.pagePadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MapScreenContent(
                    snackbarHostState = snackbarHostState,
                    currentLocation = currentLocation,
                    fetchLocationUpdates = fetchLocationUpdates
                )
                Spacer(modifier = Modifier.height(Dimensions.twoSpacers))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(2.5f)
                    ) {
                        Text(text = "My Destination")
                        Text(
                            text = destination,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = { onEvent(RequestEvents.CancelRide) }
                    ) {
                        Text(text = "Cancel")
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.oneSpacer))
                val rideStatus = remember {
                    state.rideStatus
                }
                Text(
                    text = when (rideStatus) {
                        RideStatus.COMPLETED -> "Ride Completed"
                        RideStatus.ACCEPTED -> "Ride Accepted"
                        RideStatus.ARRIVING -> "Driver is close"
                        RideStatus.DRIVER_CANCELED -> "Ride Cancelled"
                        RideStatus.PROCESSING -> "Processing Ride"
                    },
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Dimensions.oneSpacer))
                if (state.showOrderButton) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        ),
                        onClick = { onEvent(RequestEvents.ConfirmOrder) }
                    ) {
                        Text(text = "Confirm Order")
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .wrapContentHeight(Alignment.Bottom)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun MapScreenContent(
    snackbarHostState: SnackbarHostState,
    fetchLocationUpdates: () -> Unit,
    currentLocation: LatLng
) {
    val scope = rememberCoroutineScope()
// 2
    val context = LocalContext.current
// 3
    var showMap by rememberSaveable {
        mutableStateOf(false)
    }
// 4
    PermissionDialog(
        context = context,
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        permissionRationale = "This app requires the location permission to be granted.",
        snackbarHostState = snackbarHostState
    ) { permissionAction ->
        when (permissionAction) {
            is PermissionAction.PermissionDenied -> {
                showMap = false
            }
            is PermissionAction.PermissionGranted -> {
                showMap = true
                scope.launch {
                    snackbarHostState.showSnackbar("Location permission granted!")
                }
                fetchLocationUpdates.invoke()
            }
        }
    }
    if (showMap) {
        MapView(currentLocation)
    }
}

@Composable
fun MapView(location: LatLng) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = location),
        )
    }

}

@Preview
@Composable
fun RequestScreenPreview() {
    RideSharingAppTheme {
        RequestScreen(
            state = RequestState(),
            currentLocation = LatLng(1.35, 103.87),
            snackbarHostState = remember {
                SnackbarHostState()
            },
            onEvent = {},
            fetchLocationUpdates = { /*TODO*/ },
            navController = rememberNavController(),
            destination = "8, Shoyinka Street, Itire, Ikate, Lagos."
        )
    }
}