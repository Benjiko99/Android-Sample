package com.spiraclesoftware.androidsample.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.spiraclesoftware.androidsample.R
import com.google.android.material.textfield.TextInputLayout as MaterialTextInputLayout

class TextInputLayout : MaterialTextInputLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.textInputStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputLayout,
            defStyleAttr,
            R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox
        ).use { initStyledAttributes(it) }
    }

    private fun initStyledAttributes(attrs: TypedArray) {
    }

}