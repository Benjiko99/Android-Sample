package com.spiraclesoftware.androidsample.framework.extensions

import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil

/**
 * Allows passing a list of models to [FastAdapterDiffUtil]
 */
operator fun <A : ModelAdapter<Model, Item>, Model, Item : GenericItem> FastAdapterDiffUtil.set(
    adapter: A,
    models: List<Model>
): A {
    return set(adapter, adapter.intercept(models))
}
