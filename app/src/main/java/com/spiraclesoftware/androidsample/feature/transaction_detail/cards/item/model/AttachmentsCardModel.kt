package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import android.net.Uri
import com.spiraclesoftware.androidsample.framework.Model

data class AttachmentsCardModel(
    val attachments: List<Uri>,
    val uploads: List<Uri>
) : Model
