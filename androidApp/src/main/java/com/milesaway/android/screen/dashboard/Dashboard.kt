package com.milesaway.android.screen.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.milesaway.android.Routes
import com.milesaway.android.collectAsStateLifecycleAware
import com.milesaway.android.component.CustomTopAppBar
import com.milesaway.android.mvi.SIDE_EFFECTS_KEY
import org.koin.androidx.compose.getViewModel

@Composable
fun Dashboard(navController: NavHostController) {

    val viewModel = getViewModel<DashboardViewModel>()

    val context = LocalContext.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DashboardContract.Effect.NavigateToLogin -> {
                    navController.navigate(Routes.Login.route) {
                        popUpTo = 0
                    }
                }
                is DashboardContract.Effect.ShowToast -> {
                    showToast(context, effect.message)
                }
            }
        }
    }

    val state: DashboardContract.State by viewModel.uiState.collectAsStateLifecycleAware()

    Box(modifier = Modifier.fillMaxSize()) {
        DashboardPage(navController, state) {
            viewModel.sendEvent(DashboardContract.Event.LogoutButtonClicked)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DashboardPage(
    navController: NavHostController,
    state: DashboardContract.State,
    onLogoutClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(navController, "Home", false)
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                } else {
                    null
                }
            )
        }, content = {
            Box(modifier = Modifier.background(Color.White)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Hi, ${state.username} !",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                    Button(onClick = {
                        onLogoutClicked()
                    }) {
                        Text(text = "Logout")
                    }
                }
            }
        })
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
