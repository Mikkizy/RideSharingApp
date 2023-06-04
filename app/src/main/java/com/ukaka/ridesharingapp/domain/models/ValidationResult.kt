package com.ukaka.ridesharingapp.domain.models

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
