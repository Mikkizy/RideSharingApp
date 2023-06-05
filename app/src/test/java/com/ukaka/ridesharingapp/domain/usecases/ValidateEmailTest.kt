package com.ukaka.ridesharingapp.domain.usecases

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateEmailTest {
    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun blankEmailReturnsError() {
        val result = validateEmail("")
        assertThat(result.successful).isFalse()
    }
}