package com.milesaway.android.screen.login

import com.milesaway.android.mvi.UiEffect
import com.milesaway.android.mvi.UiEvent
import com.milesaway.android.mvi.UiState

interface LoginContract {
    sealed class Event : UiEvent {
        object Init : Event()
        data class UsernameValueChanged(val username: String) : Event()
        data class PasswordValueChanged(val password: String) : Event()
        data class LoginButtonClicked(val email: String, val password: String) : Event()
    }

    //UI view state
    data class State(
        val enteredUsername: String,
        val enteredPassword: String,
        val isLoading: Boolean,
        val isSignInComplete: Boolean,
        val isAuthorized: Boolean = false
    ) : UiState

    sealed class Effect : UiEffect {
        object NavigateToMain : Effect()
        data class ShowToast(val message: String) : Effect()
    }
}