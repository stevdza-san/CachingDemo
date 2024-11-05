package com.stevdza_san.demo.di

import com.stevdza_san.demo.data.local.AndroidDatabaseDriverFactory
import com.stevdza_san.demo.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}