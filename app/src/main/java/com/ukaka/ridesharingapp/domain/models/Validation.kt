package com.ukaka.ridesharingapp.domain.models

import com.ukaka.ridesharingapp.domain.usecases.ValidateEmail
import com.ukaka.ridesharingapp.domain.usecases.ValidatePassword
import com.ukaka.ridesharingapp.domain.usecases.ValidateUsername

data class Validation(
    val validateUsername: ValidateUsername,
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword
)
