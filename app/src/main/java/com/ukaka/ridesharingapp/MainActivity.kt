package com.ukaka.ridesharingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.ukaka.ridesharingapp.common.Constants.MAPS_API_KEY
import com.ukaka.ridesharingapp.common.ToastMessages
import com.ukaka.ridesharingapp.common.changeSystemBarsColor
import com.ukaka.ridesharingapp.common.clearAndNavigate
import com.ukaka.ridesharingapp.presentation.Screen
import com.ukaka.ridesharingapp.presentation.destination.DestinationScreen
import com.ukaka.ridesharingapp.presentation.destination.DestinationViewModel
import com.ukaka.ridesharingapp.presentation.login.LoginScreen
import com.ukaka.ridesharingapp.presentation.login.LoginViewModel
import com.ukaka.ridesharingapp.presentation.permissions.locationFlow
import com.ukaka.ridesharingapp.presentation.ride_request.RequestScreen
import com.ukaka.ridesharingapp.presentation.ride_request.RequestViewModel
import com.ukaka.ridesharingapp.presentation.signup.SignUpScreen
import com.ukaka.ridesharingapp.presentation.signup.SignupViewModel
import com.ukaka.ridesharingapp.presentation.splash.SplashScreen
import com.ukaka.ridesharingapp.ui.theme.RideSharingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(this, MAPS_API_KEY)
        setContent {
            RideSharingAppTheme {
                // A surface container using the 'background' color from the theme

                val systemUiController = rememberSystemUiController()
                LaunchedEffect(Unit) {
                    systemUiController.changeSystemBarsColor()
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }

                    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
                        composable(Screen.SplashScreen.route) {
                            SplashScreen {
                                systemUiController.changeSystemBarsColor()

                                navController.clearAndNavigate(
                                    clearDestination = Screen.SplashScreen.route,
                                    navigateToDestination = Screen.LoginScreen.route
                                )
                            }
                        }
                        composable(Screen.LoginScreen.route) {
                            val loginViewModel = hiltViewModel<LoginViewModel>()
                            val loginState by remember {
                                loginViewModel.state
                            }.collectAsStateWithLifecycle()

                            LoginScreen(
                                state = loginState,
                                onEvent = loginViewModel::onEvent,
                                validationEvents = loginViewModel.validationEvents
                            )
                        }
                        composable(Screen.SignupScreen.route) {
                            val signupViewModel = hiltViewModel<SignupViewModel>()
                            val signupState by remember {
                                signupViewModel.state
                            }.collectAsStateWithLifecycle()

                            SignUpScreen(
                                state = signupState,
                                onEvent = signupViewModel::onEvent,
                                validationEvents = signupViewModel.validationEvents
                            )
                        }
                        composable(Screen.DestinationScreen.route) {

                            val destinationViewModel = hiltViewModel<DestinationViewModel>()

                            val destinationState by remember {
                                destinationViewModel.state
                            }.collectAsStateWithLifecycle()

                            val autocompleteList by remember {
                                destinationViewModel.autoCompleteList
                            }.collectAsStateWithLifecycle()

                            destinationViewModel.toastHandler = { toastMessage ->
                               val message = when (toastMessage) {
                                   ToastMessages.UNABLE_TO_RETRIEVE_COORDINATES -> getString(R.string.unable_to_receive_coordinates)
                                   ToastMessages.SERVICE_ERROR -> getString(R.string.service_error)
                               }
                                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                            }

                            DestinationScreen(
                                state = destinationState,
                                autoCompleteList = autocompleteList,
                                onEvents = destinationViewModel::onEvent
                            )
                        }
                        composable(
                            route = Screen.RideRequestScreen.route,
                            arguments = listOf(
                                navArgument("destination") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val fusedLocationClient: FusedLocationProviderClient by lazy {
                                LocationServices.getFusedLocationProviderClient(this@MainActivity)
                            }
                            val requestViewModel = hiltViewModel<RequestViewModel>()
                            fun fetchLocationUpdates() {
                                lifecycleScope.launch {
                                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                        fusedLocationClient.locationFlow().collect { location ->
                                            location?.let { userLocation ->
                                                requestViewModel.setCurrentLatLng(LatLng(userLocation.latitude, userLocation.longitude))
                                            }
                                        }
                                    }
                                }
                            }
                            val currentLocation by remember {
                                requestViewModel.currentLatLng
                            }.collectAsStateWithLifecycle()
                            val requestState by remember {
                                requestViewModel.state
                            }.collectAsStateWithLifecycle()
                            val destination = it.arguments?.getString("destination")
                            RequestScreen(
                                state = requestState,
                                destination = destination ?: "",
                                snackbarHostState = snackbarHostState,
                                onEvent = requestViewModel::onEvent,
                                fetchLocationUpdates = { fetchLocationUpdates() },
                                navController = navController,
                                currentLocation = currentLocation
                            )
                        }
                    }
                }
            }
        }
    }
}