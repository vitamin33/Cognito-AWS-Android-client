package com.milesaway.android.di

import com.milesaway.android.screen.dashboard.DashboardViewModel
import com.milesaway.android.screen.login.LoginViewModel
import com.milesaway.android.screen.login.domain.SsoClient
import com.milesaway.android.screen.login.domain.SsoClientImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    single<SsoClient> { SsoClientImpl(androidContext()) }
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}
