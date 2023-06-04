package com.ukaka.ridesharingapp.domain.usecases

import com.ukaka.ridesharingapp.domain.models.ValidationResult

class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {

        return if (password.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Password cannot be empty!"
            )
        } else ValidationResult(
            successful = true
        )
    }
}