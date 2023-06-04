package com.ukaka.ridesharingapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ukaka.ridesharingapp.R
import com.ukaka.ridesharingapp.common.AppHeader
import com.ukaka.ridesharingapp.common.Dimensions
import com.ukaka.ridesharingapp.common.ValidationEvents
import com.ukaka.ridesharingapp.presentation.Screen
import com.ukaka.ridesharingapp.ui.theme.RideSharingAppTheme
import com.ukaka.ridesharingapp.ui.theme.color_primary
import com.ukaka.ridesharingapp.ui.theme.color_white
import com.ukaka.ridesharingapp.ui.theme.typography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvents) -> Unit,
    validationEvents: Flow<ValidationEvents>
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collectLatest { event ->
            when (event) {
                is ValidationEvents.Success -> {
                    Toast.makeText(
                        context,
                        "Login successful",
                        Toast.LENGTH_LONG
                    ).show()

                    //Navigate to ride request page
                    navController.navigate(Screen.DestinationScreen.route)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color_white)
            .padding(Dimensions.pagePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        AppHeader(
            modifier = Modifier.padding(top = 64.dp),
            subtitleText = stringResource(id = R.string.need_a_ride)
        )

        EmailInputField(
            modifier = Modifier.padding(top = 16.dp),
            state = state,
            onEvent = onEvent
        )

        PasswordInputField(
            modifier = Modifier.padding(top = 16.dp),
            state = state,
            onEvent = onEvent
        )

        LoginContinueButton(
            modifier = Modifier.padding(top = 32.dp),
            handleLogin = { onEvent(LoginEvents.ClickedLoginButton) }
        )

        SignupText(
            modifier = Modifier.padding(top = 32.dp),
            signUp = { navController.navigate(Screen.SignupScreen.route) }
        )
    }
}

@Composable
fun EmailInputField(
    modifier: Modifier = Modifier,
    state: LoginState,
    onEvent: (LoginEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = {
                onEvent(LoginEvents.EnteredEmail(it))
            },
            isError = state.emailError != null,
            placeholder = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    state: LoginState,
    onEvent: (LoginEvents) -> Unit
) {

    var showPassword by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = state.password,
        onValueChange = {
           onEvent(LoginEvents.EnteredPassword(it))
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(text = stringResource(id = R.string.password)) },
        trailingIcon = {
            val image = if (showPassword)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (showPassword) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)
            IconButton(onClick = { showPassword = !showPassword}){
                Icon(imageVector  = image, description)
            }
        }
    )
}

@Composable
fun LoginContinueButton(
    modifier: Modifier,
    handleLogin: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color_primary,
            contentColor = color_white
        ),
        onClick = { handleLogin() },
    ) {
        Text(
            text = stringResource(id = R.string.string_continue),
            style = typography.button
        )
    }
}

@Composable
fun SignupText(
    modifier: Modifier,
    signUp: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = signUp
    ) {
        Text(
            style = typography.subtitle2,
            text = buildAnnotatedString {
                append(stringResource(id = R.string.no_account))
                append(" ")
                withStyle(
                    SpanStyle(
                        color = color_primary,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(stringResource(id = R.string.sign_up))
                }
                append(" ")
                append(stringResource(id = R.string.here))
            }
        )
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    RideSharingAppTheme {
        LoginScreen(
            state = LoginState(),
            onEvent = {},
            validationEvents = flow { }
        )
    }
}

