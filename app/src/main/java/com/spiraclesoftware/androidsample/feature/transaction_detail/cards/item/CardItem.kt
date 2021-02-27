package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.mikepenz.fastadapter.items.ModelAbstractItem

abstract class ModelCardItem<Model, VH>(model: Model) :
    ModelAbstractItem<Model, VH>(model) where VH : RecyclerView.ViewHolder

abstract class ModelBindingCardItem<Model, Binding>(model: Model) :
    ModelAbstractBindingItem<Model, Binding>(model) where Binding : ViewBinding
