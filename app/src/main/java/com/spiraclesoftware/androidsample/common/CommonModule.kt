package com.spiraclesoftware.androidsample.common

import com.spiraclesoftware.androidsample.common.formatter.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonModule = module {

    single { ExceptionFormatter(androidContext()) }

    single { MoneyFormatter() }

    single { TransactionCategoryFormatter() }

    single { TransactionStatusCodeFormatter() }

    single { TransactionStatusFormatter() }

    single { TransferDirectionFilterFormatter() }

}