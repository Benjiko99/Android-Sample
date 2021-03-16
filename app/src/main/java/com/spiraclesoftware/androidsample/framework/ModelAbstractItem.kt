package com.spiraclesoftware.androidsample.framework

import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.ModelAbstractItem

/**
 * Provides a default [equals] and [hashCode] implementation.
 */
abstract class ModelAbstractItem<Model, VH>(model: Model) :
    ModelAbstractItem<Model, VH>(model) where VH : RecyclerView.ViewHolder {

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as ModelAbstractItem<*, *>
        if (model != other.model) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        return result
    }
}