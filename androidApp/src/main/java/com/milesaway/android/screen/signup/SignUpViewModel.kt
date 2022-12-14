package com.milesaway.android.screen.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.milesaway.android.mvi.BaseViewModel
import com.milesaway.android.domain.SsoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val signInComplete: Boolean?,
    private val client: SsoClient
) :
    BaseViewModel<SignUpContract.Event, SignUpContract.State, SignUpContract.Effect>() {

    init {
        Log.d(TAG, "SignUpViewModel, signInComplete: $signInComplete")

        setState { copy(isSignInComplete = signInComplete) }
    }

    override fun handleEvent(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.SignUpButtonClicked -> {
                launchSignUpUseCase(event.username, event.email, event.password)
            }
            is SignUpContract.Event.PasswordValueChanged -> {
                setState { copy(enteredPassword = event.password) }
            }
            is SignUpContract.Event.UsernameValueChanged -> {
                setState { copy(enteredUsername = event.username) }
            }
            is SignUpContract.Event.EmailValueChanged -> {
                setState { copy(enteredEmail = event.email) }
            }
            is SignUpContract.Event.ConfirmButtonClicked -> {
                launchConfirmUseCase(event.username, event.confirmCode)
            }
            is SignUpContract.Event.ConfirmCodeValueChanged -> {
                setState { copy(enteredConfirmationCode = event.code) }
            }
        }
    }

    private fun launchConfirmUseCase(username: String, confirmCode: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<Unit>
            withContext(Dispatchers.IO) {
                result = client.confirmSignUp(username, confirmCode)
            }
            if (result.isSuccess) {
                sendEffect(SignUpContract.Effect.NavigateToLogin)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Confirmation error"
                sendEffect(SignUpContract.Effect.ShowToast(message))
            }

            setState {
                copy(isLoading = false)
            }
        }
    }

    private fun launchSignUpUseCase(username: String, email: String, password: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<Boolean>
            withContext(Dispatchers.IO) {
                result = client.signUp(username, email, password)
            }
            if (result.isSuccess) {
                result.getOrNull()?.let { signInComplete ->
                    setState { copy(isSignInComplete = signInComplete) }
                    if (signInComplete) {
                        sendEffect(SignUpContract.Effect.NavigateToLogin)
                    }
                }
            } else {
                val message = result.exceptionOrNull()?.message ?: "Login error"
                sendEffect(SignUpContract.Effect.ShowToast(message))
            }

            setState {
                copy(isLoading = false)
            }
        }
    }

    override fun initState(): SignUpContract.State {
        return SignUpContract.State(
            "",
            "",
            "",
            "",
            isLoading = false,
            isSignInComplete = signInComplete
        )
    }

    companion object {
        const val TAG = "SignUpViewModel"
    }
}