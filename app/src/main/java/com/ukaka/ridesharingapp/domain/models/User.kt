package com.ukaka.ridesharingapp.domain.models

data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val password: String
)
