package com.ukaka.ridesharingapp.domain.usecases

import com.ukaka.ridesharingapp.domain.models.ValidationResult

class ValidateUsername {
    operator fun invoke(username: String): ValidationResult {

        return if (username.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Username cannot be empty!"
            )
        } else ValidationResult(
            successful = true
        )
    }
}