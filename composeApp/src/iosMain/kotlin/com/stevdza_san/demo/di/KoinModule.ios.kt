package com.stevdza_san.demo.di

import com.stevdza_san.demo.data.local.DatabaseDriverFactory
import com.stevdza_san.demo.data.local.IOSDatabaseDriverFactory
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}