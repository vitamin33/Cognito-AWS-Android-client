package com.milesaway.android.screen.signup

import androidx.lifecycle.viewModelScope
import com.milesaway.android.mvi.BaseViewModel
import com.milesaway.android.domain.SsoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val client: SsoClient):
    BaseViewModel<SignUpContract.Event, SignUpContract.State, SignUpContract.Effect>() {

    override fun handleEvent(event: SignUpContract.Event) {
        when(event) {
            is SignUpContract.Event.SignUpButtonClicked -> {
                launchSignUpUseCase(event.username, event.password)
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
        }
    }

    private fun launchSignUpUseCase(email: String, password: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<Unit>
            withContext(Dispatchers.IO) {
                result = client.signIn(email, password)
            }
            if (result.isSuccess) {
                sendEffect(SignUpContract.Effect.NavigateToFinishSignUp)
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
            isLoading = false,
            isSignInComplete = false
        )
    }

    companion object {
        const val TAG = "SignUpViewModel"
    }
}