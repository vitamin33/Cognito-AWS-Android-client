package com.milesaway.android.screen.login.domain

interface SsoClient {

    fun init()

    suspend fun signIn(userName: String, userPassword: String): Result<Unit>

    suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit>

    suspend fun getAccessToken(): Result<String>

    suspend fun logout(): Result<Unit>

}