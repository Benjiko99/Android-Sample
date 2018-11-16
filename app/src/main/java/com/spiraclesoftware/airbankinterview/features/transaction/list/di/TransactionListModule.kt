package com.spiraclesoftware.airbankinterview.features.transaction.list.di

import androidx.lifecycle.ViewModel
import com.spiraclesoftware.core.di.FragmentScoped
import com.spiraclesoftware.core.di.ViewModelKey
import com.spiraclesoftware.airbankinterview.features.transaction.list.ui.TransactionListFragment
import com.spiraclesoftware.airbankinterview.features.transaction.list.ui.TransactionListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

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

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [TransactionListViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(TransactionListViewModel::class)
    internal abstract fun bindTransactionListViewModel(viewModel: TransactionListViewModel): ViewModel
}