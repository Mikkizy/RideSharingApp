package com.ukaka.ridesharingapp.domain.usecases

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateUsernameTest {
    private lateinit var validateUsername: ValidateUsername

    @Before
    fun setUp() {
        validateUsername = ValidateUsername()
    }

    @Test
    fun blankUsernameReturnsError() {
        val result = validateUsername("")
        assertThat(result.successful).isFalse()
    }
}