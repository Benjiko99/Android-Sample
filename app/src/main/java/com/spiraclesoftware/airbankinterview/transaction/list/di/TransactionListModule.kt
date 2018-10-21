package com.spiraclesoftware.airbankinterview.transaction.list.di

import com.spiraclesoftware.airbankinterview.di.FragmentScoped
import com.spiraclesoftware.airbankinterview.transaction.list.ui.TransactionListFragment
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector

/**
 * Module where classes needed to create the [TransactionListFragment] are defined.
 */
@Module
internal abstract class TransactionListModule {

    /**
     * Generates an [AndroidInjector] for the [TransactionListFragment].
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTransactionListFragment(): TransactionListFragment
}