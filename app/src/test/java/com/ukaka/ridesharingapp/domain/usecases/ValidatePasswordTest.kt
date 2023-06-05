package com.ukaka.ridesharingapp.domain.usecases

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {
    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun blankPasswordReturnsError() {
        val result = validatePassword("")
        assertThat(result.successful).isFalse()
    }
}