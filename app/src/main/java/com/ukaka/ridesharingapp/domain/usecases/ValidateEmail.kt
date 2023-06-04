package com.ukaka.ridesharingapp.domain.usecases

import com.ukaka.ridesharingapp.domain.models.ValidationResult

class ValidateEmail {
    operator fun invoke(email: String): ValidationResult {

        return if (email.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Email cannot be empty!"
            )
        } else ValidationResult(
            successful = true
        )
    }
}