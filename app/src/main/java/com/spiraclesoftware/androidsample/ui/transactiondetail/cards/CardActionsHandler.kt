package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

interface CardActionsHandler {
    fun onOpenCardDetail()
    fun onDownloadStatement()
    fun onSelectCategory()
    fun onAddAttachment()
    fun onRemoveAttachment(url: String)
    fun onViewAttachment(url: String)
    fun onChangeNote()
}
