package com.spiraclesoftware.core.di

import com.spiraclesoftware.core.data.AppExecutors
import org.koin.dsl.module
import java.util.concurrent.Executors

val coreModule = module {

    single {
        AppExecutors(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool(3),
            AppExecutors.MainThreadExecutor()
        )
    }
}