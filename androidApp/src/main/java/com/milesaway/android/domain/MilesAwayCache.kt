package com.milesaway.android.domain

interface MilesAwayCache {

    suspend fun saveUsername(username: String?)

    suspend fun getUsername(): String?

    suspend fun logout()

    sealed class AuthStatus {
        object SignedIn : AuthStatus()
        object SignedOut : AuthStatus()
    }
}