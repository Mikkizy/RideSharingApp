package com.ukaka.ridesharingapp.presentation

sealed class Screen(val route: String) {
    object LoginScreen: Screen("login")
    object SignupScreen: Screen("signup")
    object RideRequestScreen: Screen("ride_request/{destination}")
    object SplashScreen: Screen("splash")
    object DestinationScreen: Screen("destination")
}