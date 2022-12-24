package com.milesaway.android.screen.login

import androidx.lifecycle.viewModelScope
import com.milesaway.android.domain.MilesAwayCache
import com.milesaway.android.mvi.BaseViewModel
import com.milesaway.android.domain.SsoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val client: SsoClient,
    private val milesAwayCache: MilesAwayCache
    ):
    BaseViewModel<LoginContract.Event, LoginContract.State, LoginContract.Effect>() {
    init {
        sendEvent(LoginContract.Event.Init)
    }

    override fun handleEvent(event: LoginContract.Event) {
        when(event) {
            is LoginContract.Event.LoginButtonClicked -> {
                launchLoginUseCase(event.email, event.password)
            }
            is LoginContract.Event.PasswordValueChanged -> {
                setState { copy(enteredPassword = event.password) }
            }
            is LoginContract.Event.UsernameValueChanged -> {
                setState { copy(enteredUsername = event.username) }
            }
            LoginContract.Event.Init -> {
                checkAuthState()
            }
        }
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<String>
            withContext(Dispatchers.IO) {
                result = client.getAccessToken()
            }
            if (result.isSuccess) {
                sendEffect(LoginContract.Effect.NavigateToMain)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Login error"
                sendEffect(LoginContract.Effect.ShowToast(message))
            }
        }
    }

    private fun launchLoginUseCase(username: String, password: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<Boolean>
            withContext(Dispatchers.IO) {
                result = client.signIn(username, password)
                if (result.isSuccess) {
                    milesAwayCache.saveUsername(username)
                }
            }
            if (result.isSuccess) {
                result.getOrNull()?.let { confirmed ->
                    if (confirmed) {
                        sendEffect(LoginContract.Effect.NavigateToMain)
                    } else {
                        sendEffect(LoginContract.Effect.NavigateToSignUp)
                    }
                }
            } else {
                val message = result.exceptionOrNull()?.message ?: "Login error"
                sendEffect(LoginContract.Effect.ShowToast(message))
            }

            setState {
                copy(isLoading = false)
            }
        }
    }

    override fun initState(): LoginContract.State {
        return LoginContract.State(
            "",
            "",
            isLoading = false,
            isSignInComplete = false
        )
    }

    companion object {
        const val TAG = "LoginViewModel"
    }
}