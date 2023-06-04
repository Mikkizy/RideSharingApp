package com.ukaka.ridesharingapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukaka.ridesharingapp.common.ValidationEvents
import com.ukaka.ridesharingapp.domain.models.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val getValidations: Validation,
): ViewModel() {
    private var _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvents>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: SignupEvents) {
        when(event) {
            is SignupEvents.EnteredUsername -> {
                _state.value = _state.value.copy(username = event.username)
            }
            is SignupEvents.EnteredEmail -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is SignupEvents.EnteredPassword -> {
                _state.update {
                    it.copy(password = event.password)
                }
            }
            is SignupEvents.ClickedSignupButton -> {
                val entriesAreValid = isUserEntryValid()
                if (entriesAreValid) {
                    viewModelScope.launch {
                        validationEventChannel.send(ValidationEvents.Success)
                    }
                }
            }
        }
    }

    /**
     * This function allows you to authenticate a user by vetting their entries.
     * It returns true if the entries are valid, and false if they are invalid.
     */
    private fun isUserEntryValid(): Boolean {

        val usernameResult = getValidations.validateUsername(_state.value.username)
        val emailResult = getValidations.validateEmail(_state.value.email)
        val passwordResult = getValidations.validatePassword(_state.value.password)

        /**
         * This value checks if there is any error in both fields.
         */
        val errorInEntry = listOf(usernameResult, emailResult, passwordResult).any { !it.successful }

        _state.value = _state.value.copy(
            usernameError = usernameResult.errorMessage,
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage
        )

        return !errorInEntry
    }
}