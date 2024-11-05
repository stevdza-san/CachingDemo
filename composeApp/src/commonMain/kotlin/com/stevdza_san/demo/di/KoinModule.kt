package com.stevdza_san.demo.di

import com.russhwolf.settings.Settings
import com.stevdza_san.demo.MainViewModel
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.data.local.DatabaseDriverFactory
import com.stevdza_san.demo.data.local.LocalDatabase
import com.stevdza_san.demo.data.remote.PostApi
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val targetModule: Module

val sharedModule = module {
    single<PostApi> { PostApi() }
    single<LocalDatabase> { LocalDatabase(get()) }
    single<Settings> { Settings() }
    single<PostSDK> {
        PostSDK(
            api = get(),
            database = get(),
            settings = get()
        )
    }
    viewModel { MainViewModel(sdk = get()) }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}