package com.spiraclesoftware.androidsample.features.transaction_detail.cards

import android.net.Uri

interface CardActionsHandler {
    fun onOpenCardDetail()
    fun onDownloadStatement()
    fun onSelectCategory()
    fun onAddAttachment()
    fun onRemoveAttachment(uri: Uri)
    fun onViewAttachment(uri: Uri)
    fun onCancelUpload(uri: Uri)
    fun onChangeNote()
}
