package com.spiraclesoftware.androidsample.framework.core

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.viewbinding.ViewBinding
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.framework.extensions.color
import com.spiraclesoftware.androidsample.framework.extensions.colorAttr

/**
 * Extends [RainbowCakeFragment] and takes care of [ViewBinding].
 */
abstract class StandardFragment<VB, VS, VM> :
    RainbowCakeFragment<VS, VM>()
        where VB : ViewBinding,
              VS : Any,
              VM : RainbowCakeViewModel<VS> {

    open var themeResId: Int = R.style.AppTheme

    private val themedContext
        get() = ContextThemeWrapper(requireContext(), themeResId)

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    @SuppressLint("MissingSuperCall")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        applyStatusBarColor()

        val themedInflater = inflater.cloneInContext(themedContext)
        _binding = provideViewBinding(themedInflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun applyStatusBarColor() {
        val window = requireActivity().window!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = themedContext.colorAttr(android.R.attr.colorBackground)
        } else {
            window.statusBarColor = themedContext.color(android.R.color.black)
        }
    }
}