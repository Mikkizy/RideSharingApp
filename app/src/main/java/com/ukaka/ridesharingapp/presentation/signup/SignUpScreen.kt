package com.ukaka.ridesharingapp.presentation.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
fun SignUpScreen(
    state: SignupState,
    onEvent: (SignupEvents) -> Unit,
    validationEvents: Flow<ValidationEvents>,
    navController: NavController
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collectLatest { event ->
            when (event) {
                is ValidationEvents.Success -> {
                    Toast.makeText(
                        context,
                        "SignUp successful",
                        Toast.LENGTH_LONG
                    ).show()

                    //Navigate to destination page
                    navController.navigate(Screen.DestinationScreen.route)
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color_white)
            .verticalScroll(scrollState)
            .padding(Dimensions.pagePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Icon(
                modifier = Modifier.clickable { navController.navigateUp() },
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(id = R.string.close_icon),
            )
        }

        AppHeader(
            modifier = Modifier.padding(top = 64.dp),
            subtitleText = stringResource(id = R.string.sign_up_for_free)
        )

        UsernameInputField(
            modifier = Modifier.padding(top = 16.dp),
            state = state,
            onEvent = onEvent
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

        SignUpContinueButton(
            modifier = Modifier.padding(top = 32.dp),
            onEvent = onEvent
        )
    }
}

@Composable
fun SignUpContinueButton(
    modifier: Modifier,
    onEvent: (SignupEvents) -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color_primary,
            contentColor = color_white
        ),
        onClick = { onEvent(SignupEvents.ClickedSignupButton) },
    ) {
        Text(
            text = stringResource(id = R.string.string_continue),
            style = typography.button
        )
    }
}

@Composable
fun UsernameInputField(
    modifier: Modifier = Modifier,
    state: SignupState,
    onEvent: (SignupEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = state.username,
            onValueChange = {
                onEvent(SignupEvents.EnteredUsername(it))
            },
            isError = state.usernameError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = stringResource(id = R.string.user_name)) }
        )
        if (state.usernameError != null) {
            Text(
                text = state.usernameError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun EmailInputField(
    modifier: Modifier = Modifier,
    state: SignupState,
    onEvent: (SignupEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = state.email,
            onValueChange = {
                onEvent(SignupEvents.EnteredEmail(it))
            },
            isError = state.emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text(text = stringResource(id = R.string.email)) }
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
    state: SignupState,
    onEvent: (SignupEvents) -> Unit
) {

    var showPassword by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = state.password,
            onValueChange = {
                onEvent(SignupEvents.EnteredPassword(it))
            },
            isError = state.passwordError != null,
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
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSignupScreen() {
    RideSharingAppTheme {
        SignUpScreen(
            state = SignupState(),
            onEvent = {},
            validationEvents = flow {  },
            navController = rememberNavController()
        )
    }
}