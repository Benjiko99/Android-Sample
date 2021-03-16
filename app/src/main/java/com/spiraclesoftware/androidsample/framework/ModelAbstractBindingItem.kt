package com.spiraclesoftware.androidsample.framework

import androidx.viewbinding.ViewBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Provides a default [equals] and [hashCode] implementation.
 */
abstract class ModelAbstractBindingItem<Model, Binding>(model: Model) :
    ModelAbstractBindingItem<Model, Binding>(model) where Binding : ViewBinding {

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as ModelAbstractBindingItem<*, *>
        if (model != other.model) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        return result
    }
}