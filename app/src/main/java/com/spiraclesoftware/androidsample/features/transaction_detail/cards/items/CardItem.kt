package com.spiraclesoftware.androidsample.features.transaction_detail.cards.items

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.items.AbstractItem

abstract class CardItem<VH> : AbstractItem<VH>() where VH : RecyclerView.ViewHolder

abstract class BindingCardItem<Binding> : AbstractBindingItem<Binding>() where Binding : ViewBinding
