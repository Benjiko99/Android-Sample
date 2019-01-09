package com.spiraclesoftware.androidsample.application.di

import com.spiraclesoftware.androidsample.application.MainActivity
import com.spiraclesoftware.androidsample.features.transaction.detail.TransactionDetailModule
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListModule
import com.spiraclesoftware.core.di.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            TransactionListModule::class,
            TransactionDetailModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}