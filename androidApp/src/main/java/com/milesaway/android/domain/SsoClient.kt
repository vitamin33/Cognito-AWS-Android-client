package com.milesaway.android.domain

import com.milesaway.android.domain.model.User

interface SsoClient {

    fun init()

    suspend fun signIn(userName: String, userPassword: String): Result<Boolean>

    suspend fun signUp(username: String, userEmail: String, userPassword: String): Result<Boolean>

    suspend fun confirmSignUp(username: String, confirmCode: String): Result<Unit>

    suspend fun resendConfirmSignUpCode(username: String): Result<Unit>

    suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit>

    suspend fun getAccessToken(): Result<String>

    suspend fun fetchUserAttributes(): Result<User>

    suspend fun logout(): Result<Unit>

}