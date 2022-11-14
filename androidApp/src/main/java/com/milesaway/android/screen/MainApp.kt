package com.milesaway.android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposedemo.Routes
import com.milesaway.android.screen.login.ForgotPassword
import com.milesaway.android.screen.login.LoginPage
import com.milesaway.android.screen.login.LoginViewModel
import com.milesaway.android.screen.login.SignUp
import org.koin.androidx.compose.getViewModel

@Composable
fun MainApp(){
    val navController = rememberNavController()
    val viewModel = getViewModel<LoginViewModel>()

    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            LoginPage(navController = navController, viewModel)
        }

        composable(Routes.SignUp.route) {
            SignUp(navController = navController)
        }

        composable(Routes.ForgotPassword.route) { navBackStack ->
            ForgotPassword(navController = navController)
        }

        composable(Routes.Dashboard.route) {
            Dashboard(navController = navController)
        }

        composable(Routes.Home.route) {
            Home(navController = navController)
        }
    }
}