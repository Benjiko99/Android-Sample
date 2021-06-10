package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.spiraclesoftware.androidsample.framework.core.ModelAbstractBindingItem
import com.spiraclesoftware.androidsample.framework.core.ModelAbstractItem

abstract class ModelCardItem<Model, VH>(model: Model) :
    ModelAbstractItem<Model, VH>(model) where VH : RecyclerView.ViewHolder

abstract class ModelBindingCardItem<Model, Binding>(model: Model) :
    ModelAbstractBindingItem<Model, Binding>(model) where Binding : ViewBinding
