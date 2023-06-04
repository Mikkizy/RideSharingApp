package com.ukaka.ridesharingapp.presentation.signup

sealed class SignupEvents {
    data class EnteredUsername(val username: String): SignupEvents()
    data class EnteredEmail(val email: String): SignupEvents()
    data class EnteredPassword(val password: String): SignupEvents()
    object ClickedSignupButton: SignupEvents()
}
