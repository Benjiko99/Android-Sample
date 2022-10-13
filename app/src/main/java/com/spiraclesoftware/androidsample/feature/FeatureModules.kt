package com.spiraclesoftware.androidsample.feature

import com.spiraclesoftware.androidsample.feature.category_select.categorySelectModule
import com.spiraclesoftware.androidsample.feature.profile.profileModule
import com.spiraclesoftware.androidsample.feature.text_input.textInputModule
import com.spiraclesoftware.androidsample.feature.transaction_detail.transactionDetailModule
import com.spiraclesoftware.androidsample.feature.transaction_list.transactionListModule

val featureModules = listOf(
    categorySelectModule,
    profileModule,
    textInputModule,
    transactionDetailModule,
    transactionListModule,
)