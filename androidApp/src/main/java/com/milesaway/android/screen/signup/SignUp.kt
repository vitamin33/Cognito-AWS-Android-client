package com.milesaway.android.screen.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.milesaway.android.Routes
import com.milesaway.android.collectAsStateLifecycleAware
import com.milesaway.android.component.CustomTopAppBar
import com.milesaway.android.mvi.SIDE_EFFECTS_KEY
import com.milesaway.android.utils.showToast
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SignUp(navController: NavHostController, signInComplete: Int) {
    val context = LocalContext.current
    val viewModel = getViewModel<SignUpViewModel>(
        parameters = { parametersOf(signInComplete) }
    )

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SignUpContract.Effect.NavigateToLogin -> {
                    navController.navigate(Routes.Login.route) {
                        popUpTo = 0
                    }
                }
                is SignUpContract.Effect.ShowToast -> {
                    showToast(context, effect.message)
                }
            }
        }
    }

    val state: SignUpContract.State by viewModel.uiState.collectAsStateLifecycleAware()
    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithTopBar(navController, viewModel, state)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldWithTopBar(
    navController: NavHostController,
    viewModel: SignUpViewModel,
    state: SignUpContract.State
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(navController, "Signup", true)
        }, content = {
            when (state.signInState) {
                SignUpContract.SignInState.Initial -> SignInCompleteView(viewModel, state)
                SignUpContract.SignInState.SignInComplete -> SignInCompleteView(viewModel, state)
                SignUpContract.SignInState.SignInConfirmation -> SignInConfirmationView(
                    viewModel,
                    state
                )
            }
        })
}


@Composable
fun SignInCompleteView(viewModel: SignUpViewModel, state: SignUpContract.State) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = state.enteredUsername
        val password = state.enteredPassword
        val email = state.enteredEmail

        Text(
            text = "Sign up",
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Email") },
            value = email,
            onValueChange = { value ->
                viewModel.sendEvent(SignUpContract.Event.EmailValueChanged(value))
            })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Username") },
            value = username,
            onValueChange = { name ->
                viewModel.sendEvent(SignUpContract.Event.UsernameValueChanged(name))
            })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { value ->
                viewModel.sendEvent(SignUpContract.Event.PasswordValueChanged(value))
            })

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    viewModel.sendEvent(
                        SignUpContract.Event.SignUpButtonClicked(
                            username,
                            state.enteredEmail,
                            password
                        )
                    )
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}

@Composable
fun SignInConfirmationView(viewModel: SignUpViewModel, state: SignUpContract.State) {
    val username = state.enteredUsername
    val confirmationCode = state.enteredConfirmationCode
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sign up",
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row {
            TextField(
                label = { Text(text = "Confirmation code") },
                value = confirmationCode,
                onValueChange = { name ->
                    viewModel.sendEvent(SignUpContract.Event.ConfirmCodeValueChanged(name))
                })
            Button(
                onClick = {
                    viewModel.sendEvent(
                        SignUpContract.Event.ResendSignupButtonClicked
                    )
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.resendButtonPressed
            ) {
                Text(text = "Send")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    viewModel.sendEvent(
                        SignUpContract.Event.ConfirmButtonClicked(
                            confirmationCode
                        )
                    )
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Confirm")
            }
        }
    }
}