package com.milesaway.android

sealed class Routes(val route: String) {
    object SignUp : Routes("SignUp")
    object ForgotPassword : Routes("ForgotPassword")
    object Login : Routes("Login")
    object Dashboard : Routes("Dashboard")
    object Home : Routes("Home")
}

fun String.addSignInComplete(signInComplete: Boolean) : String {
    return "$this/$signInComplete"
}