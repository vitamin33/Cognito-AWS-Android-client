package com.milesaway.android.screen.dashboard

import androidx.lifecycle.viewModelScope
import com.milesaway.android.domain.model.User
import com.milesaway.android.mvi.BaseViewModel
import com.milesaway.android.domain.SsoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(private val client: SsoClient):
    BaseViewModel<DashboardContract.Event, DashboardContract.State, DashboardContract.Effect>() {
    init {
        sendEvent(DashboardContract.Event.Init)
    }

    override fun handleEvent(event: DashboardContract.Event) {
        when(event) {
            is DashboardContract.Event.LogoutButtonClicked -> {
                launchLogoutUseCase()
            }
            DashboardContract.Event.Init -> {
                getUserInfoState()
            }
        }
    }

    private fun getUserInfoState() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<User>
            withContext(Dispatchers.IO) {
                result = client.fetchUserAttributes()
            }
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    setState { copy(username = it.username) }
                }
            } else {
                val message = result.exceptionOrNull()?.message ?: "Login error"
                sendEffect(DashboardContract.Effect.ShowToast(message))
                sendEffect(DashboardContract.Effect.NavigateToLogin)
            }
        }
    }

    private fun launchLogoutUseCase() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            var result: Result<Unit>
            withContext(Dispatchers.IO) {
                result = client.logout()
            }
            if (result.isSuccess) {
                sendEffect(DashboardContract.Effect.NavigateToLogin)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Logout error"
                sendEffect(DashboardContract.Effect.ShowToast(message))
            }

            setState {
                copy(isLoading = false)
            }
        }
    }

    override fun initState(): DashboardContract.State {
        return DashboardContract.State(
            "Empty name",
            isLoading = false
        )
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }
}