package com.milesaway.android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.milesaway.android.Routes
import com.milesaway.android.screen.dashboard.Dashboard
import com.milesaway.android.screen.login.ForgotPassword
import com.milesaway.android.screen.login.LoginPage
import com.milesaway.android.screen.login.LoginViewModel
import com.milesaway.android.screen.signup.SignUp
import org.koin.androidx.compose.getViewModel

@Composable
fun MainApp(){
    val navController = rememberNavController()
    val loginViewModel = getViewModel<LoginViewModel>()

    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            LoginPage(navController = navController, loginViewModel)
        }

        composable(
            "${Routes.SignUp.route}?signInComplete={signInComplete}",
            arguments = listOf(
                navArgument("signInComplete") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val signInComplete = backStackEntry.arguments?.getInt("signInComplete") ?: -1
            SignUp(navController = navController, signInComplete)
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