package com.milesaway.android.screen.dashboard

import com.milesaway.android.mvi.UiEffect
import com.milesaway.android.mvi.UiEvent
import com.milesaway.android.mvi.UiState

interface DashboardContract {
    sealed class Event : UiEvent {
        object Init : Event()
        object LogoutButtonClicked : Event()
    }

    //UI view state
    data class State(
        val username: String,
        val isLoading: Boolean,
    ) : UiState

    sealed class Effect : UiEffect {
        object NavigateToLogin : Effect()
        data class ShowToast(val message: String) : Effect()
    }
}