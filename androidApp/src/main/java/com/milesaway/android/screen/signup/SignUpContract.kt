package com.milesaway.android.screen.signup

import com.milesaway.android.mvi.UiEffect
import com.milesaway.android.mvi.UiEvent
import com.milesaway.android.mvi.UiState

interface SignUpContract {
    sealed class Event : UiEvent {
        data class EmailValueChanged(val email: String) : Event()
        data class UsernameValueChanged(val username: String) : Event()
        data class PasswordValueChanged(val password: String) : Event()
        data class SignUpButtonClicked(val username: String, val password: String) : Event()
    }

    //UI view state
    data class State(
        val enteredUsername: String,
        val enteredEmail: String,
        val enteredPassword: String,
        val isLoading: Boolean,
        val isSignInComplete: Boolean,
    ) : UiState

    sealed class Effect : UiEffect {
        object NavigateToFinishSignUp : Effect()
        data class ShowToast(val message: String) : Effect()
    }
}