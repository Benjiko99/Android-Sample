package com.spiraclesoftware.androidsample.application.di

import com.spiraclesoftware.androidsample.features.rates.ratesModule
import com.spiraclesoftware.androidsample.features.transaction.transactionModule

val featureModules = listOf(
    transactionModule,
    ratesModule
)