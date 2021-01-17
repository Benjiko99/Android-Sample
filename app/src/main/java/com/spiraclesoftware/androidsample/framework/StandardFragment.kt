package com.spiraclesoftware.androidsample.framework

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.base.RainbowCakeViewModel

/**
 * Extends [RainbowCakeFragment] and takes care of [ViewBinding].
 */
abstract class StandardFragment<VB, VS, VM> :
    RainbowCakeFragment<VS, VM>()
        where VB : ViewBinding,
              VS : Any,
              VM : RainbowCakeViewModel<VS> {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    @SuppressLint("MissingSuperCall")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = provideViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}