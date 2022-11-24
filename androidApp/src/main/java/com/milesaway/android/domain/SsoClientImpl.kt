package com.milesaway.android.domain

import android.content.Context
import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.amplifyframework.core.Amplify
import com.milesaway.android.domain.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SsoClientImpl constructor(
    private val applicationContext: Context
) : SsoClient {

    override fun init() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.d(TAG, "AmplifySsoClient Amplify initializing")
        } catch (e: Exception) {
            Log.d(TAG, "Amplify init error", e)
        }
    }

    override suspend fun signIn(userName: String, userPassword: String): Result<Boolean> {
        return suspendCoroutine { continuation ->
            try {
                Amplify.Auth.signIn(userName, userPassword, { signInResult ->
                    when {
                        signInResult.isSignInComplete -> {
                            continuation.resume(Result.success(true))
                        }
                        signInResult.nextStep.signInStep == AuthSignInStep.CONFIRM_SIGN_UP -> {
                            continuation.resume(Result.success(false))
                        }
                        signInResult.nextStep.signInStep == AuthSignInStep.CONFIRM_SIGN_IN_WITH_NEW_PASSWORD -> {
                            continuation.resume(Result.failure(Exception("Confirm sign in with new password")))
                        }
                        else -> {
                            Log.e(
                                TAG,
                                "SignIn",
                                Exception("Unexpected login next step - ${signInResult.nextStep.signInStep}")
                            )
                            continuation.resume(Result.failure(Exception("Unexpected login next step - ${signInResult.nextStep.signInStep}")))
                        }
                    }
                }, { authException ->
                    if (authException is AuthException.NotAuthorizedException) {
                        continuation.resume(Result.failure(Exception("Wrong credential exception")))
                    } else if (authException is AuthException.UserNotConfirmedException) {
                        continuation.resume(Result.success(false))
                    } else {
                        Log.e(TAG, "Amplify signIn auth", authException)
                        continuation.resume(Result.failure(Exception(authException)))
                    }

                })
            } catch (authException: Exception) {
                Log.e(TAG, "Unexpected signIn exception", authException)
                continuation.resume(Result.failure(Exception(authException)))
            }
        }
    }

    override suspend fun signUp(
        username: String,
        userEmail: String,
        userPassword: String
    ): Result<Boolean> {
        return suspendCoroutine { continuation ->
            try {
                val options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), userEmail)
                    .build()
                Amplify.Auth.signUp(username, userPassword, options, { result ->
                    when {
                        result.isSignUpComplete -> {
                            continuation.resume(Result.success(true))
                        }
                        result.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP -> {
                            continuation.resume(Result.success(false))
                        }
                        else -> {
                            Log.e(
                                TAG,
                                "SignUp",
                                Exception("Unexpected sign up next step - ${result.nextStep.signUpStep}")
                            )
                            continuation.resume(Result.failure(Exception("Unexpected sign up next step - ${result.nextStep.signUpStep}")))
                        }
                    }
                }, { authException ->
                    Log.e(TAG, "Amplify signUp auth", authException)
                    continuation.resume(Result.failure(Exception(authException)))
                })
            } catch (authException: Exception) {
                Log.e(TAG, "Unexpected signIn exception", authException)
                continuation.resume(Result.failure(Exception(authException)))
            }
        }
    }

    override suspend fun confirmSignUp(username: String, confirmCode: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            try {
                Amplify.Auth.confirmSignUp(username, confirmCode, { result ->
                    when {
                        result.isSignUpComplete -> {
                            continuation.resume(Result.success(Unit))
                        }
                        result.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP -> {
                            continuation.resume(Result.failure(Exception("Confirm sign up")))
                        }
                        else -> {
                            Log.e(
                                TAG,
                                "SignUp",
                                Exception("Unexpected sign up next step - ${result.nextStep.signUpStep}")
                            )
                            continuation.resume(Result.failure(Exception("Unexpected sign up next step - ${result.nextStep.signUpStep}")))
                        }
                    }
                }, { authException ->
                    Log.e(TAG, "Amplify signUp auth", authException)
                    continuation.resume(Result.failure(Exception(authException)))
                })
            } catch (authException: Exception) {
                Log.e(TAG, "Unexpected signIn exception", authException)
                continuation.resume(Result.failure(Exception(authException)))
            }
        }
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            try {
                Amplify.Auth.confirmSignIn(newPassword, {
                    continuation.resume(Result.success(Unit))
                }, { error ->
                    Log.e(TAG, "Amplify Update password confirm signIn error", error)
                    continuation.resume(Result.failure(Exception(error)))
                })
            } catch (exception: Exception) {
                Log.e(TAG, "Password update failed unexpected exception", exception)
                continuation.resume(Result.failure(Exception(exception)))
            }
        }
    }

    override suspend fun getAccessToken(): Result<String> {
        return suspendCoroutine { continuation ->
            try {
                Amplify.Auth.fetchAuthSession(
                    { result: AuthSession ->
                        val cognitoAuthSession = result as AWSCognitoAuthSession
                        if (cognitoAuthSession.userPoolTokens.type == AuthSessionResult.Type.FAILURE) {
                            // Handling no session here.
                            Log.e(TAG, "Amplify no session error", Exception("Amplify no session"))
                            continuation.resume(Result.failure(Exception("Amplify no session")))
                        } else {
                            cognitoAuthSession.userPoolTokens.value?.accessToken?.let { token ->
                                continuation.resume(Result.success(token))
                            } ?: run {
                                Log.e(TAG, "Amplify token is empty", Exception("Token is empty"))
                                continuation.resume(Result.failure(Exception("Token is empty")))
                            }
                        }
                    }, { error ->
                        Log.e(TAG, "Amplify get access token exception", error)
                        continuation.resume(Result.failure(Exception(error)))
                    })
            } catch (exception: Exception) {
                Log.e(TAG, "Amplify get access token failed unexpected exception", exception)
                continuation.resume(Result.failure(Exception(exception)))
            }
        }
    }

    override suspend fun fetchUserAttributes(): Result<User> {
        return suspendCoroutine { continuation ->
            try {
                Amplify.Auth.fetchUserAttributes({ userAttributesList ->
                    val user = User(
                        username = userAttributesList.getByKey(AuthUserAttributeKey.name())
                            ?: "empty"
                    )
                    continuation.resume(Result.success(user))
                }, { exception ->
                    Log.e(TAG, "Fetch user Attributes exception", exception)
                    continuation.resume(
                        Result.failure(
                            Exception(
                                "Fetch user Attributes exception",
                                exception
                            )
                        )
                    )

                })
            } catch (exception: Exception) {
                Log.e(TAG, "Fetch user attributes exception", exception)
                continuation.resume(
                    Result.failure(
                        Exception(
                            "Fetch user attributes exception",
                            exception
                        )
                    )
                )
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return suspendCoroutine { continuation ->
            try {
                Amplify.Auth.signOut({
                    continuation.resume(Result.success(Unit))
                    Log.d(TAG, "Amplify Signed out successfully")
                }, { authException ->
                    Log.e(TAG, "Amplify Sign out failed", authException)
                    continuation.resume(Result.failure(Exception("Logout exception")))
                })
            } catch (error: Exception) {
                Log.e(TAG, "Amplify logout unexpected exception", error)
                continuation.resume(Result.failure(Exception("Amplify logout exception", error)))
            }
        }
    }

    private fun List<AuthUserAttribute>.getByKey(key: AuthUserAttributeKey): String? {
        return this.firstOrNull { it.key == key }?.value
    }

    companion object {
        const val TAG = "SsoClientImpl"
    }
}