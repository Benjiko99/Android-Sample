package com.spiraclesoftware.airbankinterview.di

import com.spiraclesoftware.airbankinterview.MainActivity
import com.spiraclesoftware.airbankinterview.transaction.detail.di.TransactionDetailModule
import com.spiraclesoftware.airbankinterview.transaction.list.di.TransactionListModule
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