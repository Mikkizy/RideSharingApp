package com.ukaka.ridesharingapp.presentation.login

sealed class LoginEvents {
    data class EnteredEmail(val email: String): LoginEvents()
    data class EnteredPassword(val password: String): LoginEvents()
    object ClickedLoginButton: LoginEvents()
}
