package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.base.RainbowCakeViewModel

class TransactionDetailViewModel(
    private val transactionDetailPresenter: TransactionDetailPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Initial) {

    fun load() = execute {
        viewState = TransactionDetailReady(transactionDetailPresenter.getData())
    }

}
