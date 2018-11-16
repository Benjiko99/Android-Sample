package com.spiraclesoftware.airbankinterview.features.transaction.detail

import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.features.transaction.detail.ui.TransactionDetailFragment
import com.spiraclesoftware.airbankinterview.features.transaction.detail.ui.TransactionDetailViewModel
import com.spiraclesoftware.core.di.FragmentScoped
import com.spiraclesoftware.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Module where classes needed to create the [TransactionDetailFragment] are defined.
 */
@Module
internal abstract class TransactionDetailModule {

    /**
     * Generates an [AndroidInjector] for the [TransactionDetailFragment].
     */
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTransactionDetailFragment(): TransactionDetailFragment

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [TransactionDetailViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(TransactionDetailViewModel::class)
    internal abstract fun bindTransactionDetailViewModel(viewModel: TransactionDetailViewModel): ViewModel
}