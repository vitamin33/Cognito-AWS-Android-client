package com.milesaway.android.screen.signup

import com.milesaway.android.mvi.UiEffect
import com.milesaway.android.mvi.UiEvent
import com.milesaway.android.mvi.UiState

interface SignUpContract {
    sealed class Event : UiEvent {
        data class EmailValueChanged(val email: String) : Event()
        data class UsernameValueChanged(val username: String) : Event()
        data class PasswordValueChanged(val password: String) : Event()
        data class ConfirmCodeValueChanged(val code: String) : Event()
        data class SignUpButtonClicked(val username: String, val email: String, val password: String) : Event()
        data class ConfirmButtonClicked(val confirmCode: String) : Event()
        object ResendSignupButtonClicked : Event()
    }

    //UI view state
    data class State(
        val enteredUsername: String,
        val enteredEmail: String,
        val enteredPassword: String,
        val enteredConfirmationCode: String,
        val isLoading: Boolean,
        val signInState: SignInState,
        val resendButtonPressed: Boolean
    ) : UiState

    sealed class Effect : UiEffect {
        object NavigateToLogin : Effect()
        data class ShowToast(val message: String) : Effect()
    }

    sealed interface SignInState {
        object Initial : SignInState
        object SignInComplete : SignInState
        object SignInConfirmation : SignInState

        companion object {
            fun createState(isSignInComplete: Boolean?) : SignInState {
                return if (isSignInComplete == null) {
                    Initial
                } else if (isSignInComplete) {
                    SignInComplete
                } else {
                    SignInConfirmation
                }
            }
        }
    }
}