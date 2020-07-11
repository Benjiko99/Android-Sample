package com.spiraclesoftware.androidsample.ui.transactiondetail

import android.os.Bundle
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.spiraclesoftware.androidsample.R

class TransactionDetailFragment : RainbowCakeFragment<TransactionDetailViewState, TransactionDetailViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_transaction_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Setup views
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: TransactionDetailViewState) {
        // TODO Render state
    }

}
