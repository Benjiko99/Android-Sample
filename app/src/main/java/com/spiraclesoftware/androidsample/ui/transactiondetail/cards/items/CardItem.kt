package com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items

import androidx.databinding.ViewDataBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

abstract class CardItem<T> : AbstractBindingItem<T>() where T : ViewDataBinding
