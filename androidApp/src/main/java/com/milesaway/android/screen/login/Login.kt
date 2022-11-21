package com.milesaway.android.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcomposedemo.Routes
import com.milesaway.android.collectAsStateLifecycleAware
import com.milesaway.android.mvi.SIDE_EFFECTS_KEY
import com.milesaway.android.theme.Purple700
import com.milesaway.android.utils.showToast

@Composable
fun LoginPage(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginContract.Effect.NavigateToMain -> {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo = 0
                    }
                }
                is LoginContract.Effect.ShowToast -> {
                    showToast(context, effect.message)
                }
            }
        }
    }

    val state: LoginContract.State by viewModel.uiState.collectAsStateLifecycleAware()

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Sign up here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { navController.navigate(Routes.SignUp.route) },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple700
            )
        )
    }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = state.enteredUsername
        val password = state.enteredPassword

        Text(text = "Login", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Username") },
            value = username,
            onValueChange = { name ->
                viewModel.sendEvent(LoginContract.Event.UsernameValueChanged(name))
            })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { value ->
                viewModel.sendEvent(LoginContract.Event.PasswordValueChanged(value))
            })

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    viewModel.sendEvent(
                        LoginContract.Event.LoginButtonClicked(
                            username,
                            password
                        )
                    )
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { navController.navigate(Routes.ForgotPassword.route) },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            )
        )
    }
}